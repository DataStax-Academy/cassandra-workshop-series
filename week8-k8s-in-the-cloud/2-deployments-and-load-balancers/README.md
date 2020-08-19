![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

## Exercise 2 - Deployments and Load Balancers

Nodeports are easy to set up, but they have a big disadvantage: they are bound to a particular kubernetes worker node. If a pod gets deleted and recreated, it might get scheduled on a different worker node, and we won't be able to reach it anymore  via the nodeport.

A way to abstract this node binding is to deploy the pods as deployments and to use load balancer services to address the pods in the deployment and correctly route any external traffic.

### 0 - Clean up from the previous exercise

Let's reconfigure our kubernetes cluster for this exercise:

```
kubectl  delete namespace my-app
```
Wait a moment until all pods and services are deleted. 


Delete your firewall rules that you created earlier:

```
gcloud compute firewall-rules delete test-node-port
```

You will see an output like this:

```
The following firewalls will be deleted:
 - [test-node-port]
Do you want to continue (Y/n)?  Y
Deleted [https://www.googleapis.com/compute/v1/projects/majestic-stage-286312/global/firewalls/test-node-port].
```

Do the same for the other rule you set up.

### 1 - Recreate the namespace

```
kubectl create namespace my-app
```

### 2 - Create separate config map for backend

```
touch backendConfig.yaml
```

Content for `backendConfig.yaml`

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: backend-config
data:
  USE_ASTRA: "true"
```

Apply this in the namespace `my-app`

```
kubectl -n my-app apply -f backendConfig.yaml
```

### 3 - Create backend deployment

Next, instead of creating a single pod for the backend, we create a deployment that specifies how many replicas we want to run for a pod. Due to architecture of the backend app (and the way it stores the Astra credentials) we can only have one replica. But in other cases, you might want to run with 3 or so.

```
touch astra-backend-deployment.yaml
```

Content for `astra-backend-deployment.yaml`

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: astra-backend
spec:
  replicas: 1
  selector:
    matchLabels:
      astra: backend
  template:
    metadata:
      labels:
        astra: backend
    spec:
      containers:
      - name: astra-backend
        image: datastaxdevs/cws-week8-backend:latest
        ports:
        - containerPort: 5000
        envFrom:
        - configMapRef:
            name: backend-config
```

let's apply the deployment in the namespace `my-app` too:

```
kubectl -n my-app apply -f astra-backend-deployment.yaml
```

The pods start very quicly:

```
$ kubectl get pods -n my-app
NAME                             READY   STATUS    RESTARTS   AGE
astra-backend-7bbf8d5644-gs6ws   1/1     Running   0          13s
```

### 4 - Inspect the workload and expose the deployment

We can now see the deployment in our workloads:

<img width="600" alt="Screenshot 2020-08-18 at 10 15 10" src="https://user-images.githubusercontent.com/20337262/90498780-a627bc80-e140-11ea-8e62-916b73a59404.png">

Click on the deployment to see its details, and then scroll down until you see the `EXPOSE` option for exposing services:

<img width="600" alt="Screenshot 2020-08-18 at 10 16 33" src="https://user-images.githubusercontent.com/20337262/90498836-b63f9c00-e140-11ea-87a8-5e41e9ed1871.png">

Enter the target port 5000 and click the `EXPOSE` button.

<img width="600" alt="Screenshot 2020-08-18 at 10 18 06" src="https://user-images.githubusercontent.com/20337262/90498882-c48db800-e140-11ea-988b-28d98bf8596b.png">

This will create a new service `astra-backend-service`. You'll see a few spinning wheels while this is happening.

<img width="600" alt="Screenshot 2020-08-18 at 10 18 23" src="https://user-images.githubusercontent.com/20337262/90498954-d53e2e00-e140-11ea-953b-e05c90ee7dea.png">

When done, it will look like this:

<img width="600" alt="Screenshot 2020-08-18 at 10 20 03" src="https://user-images.githubusercontent.com/20337262/90498986-e129f000-e140-11ea-8776-fe6d8862c0d0.png">

Now click on the external endpoint, this opens a new window, and we see the homepage of the Python backend.

<img width="600" alt="Screenshot 2020-08-18 at 10 20 54" src="https://user-images.githubusercontent.com/20337262/90499035-f272fc80-e140-11ea-9423-3684acf8e97d.png">

### 5 - Inspect the Kubernetes namespace

How does this all look in kubernetes?

Let's check out everything that got created in the `my-app` namespace

```
$ kubectl get all -n my-app
pod/astra-backend-7bbf8d5644-gs6ws   1/1     Running   0          8m32s
NAME                            TYPE           CLUSTER-IP   EXTERNAL-IP      PORT(S)        AGE
service/astra-backend-service   LoadBalancer   10.0.1.59    35.225.144.217   80:30035/TCP   4m6s
NAME                            READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/astra-backend   1/1     1            1           8m33s
NAME                                       DESIRED   CURRENT   READY   AGE
replicaset.apps/astra-backend-7bbf8d5644   1         1         1       8m33s
```

Let's inspect the `astra-backend-service `that we created:

```
$ kubectl get service -n  my-app astra-backend-service -o yaml
apiVersion: v1
kind: Service
metadata:
  annotations:
    cloud.google.com/neg: '{"ingress":true}'
  creationTimestamp: "2020-08-18T09:18:16Z"
  finalizers:
  - service.kubernetes.io/load-balancer-cleanup
  name: astra-backend-service
  namespace: my-app
  resourceVersion: "6973"
  selfLink: /api/v1/namespaces/my-app/services/astra-backend-service
  uid: c20e3ebd-a7eb-48fc-9052-de808a684f29
spec:
  clusterIP: 10.0.1.59
  externalTrafficPolicy: Cluster
  ports:
  - nodePort: 30035
    port: 80
    protocol: TCP
    targetPort: 5000
  selector:
    astra: backend
  sessionAffinity: None
  type: LoadBalancer
status:
  loadBalancer:
    ingress:
    - ip: 35.225.144.217
```

It is tied to the astra backend deployment through the selector, and it is using an ingress with an external IP address.

### 6 - Create UI deployment and expose it

Let's do the same steps for the UI. Again, let's use a UI specific configMap, `uiConfig.yaml`

```
touch uiConfig.yaml
```

Note that we now use the external endpoint for the astra-backend-service as the base address in the UI config. The external contact point is now bound to the lifetime of the service, no longer the pod on the worker node. 

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: ui-config
data:
  BASE_ADDRESS: "http://<your_external_endpoint>/api"
  USE_ASTRA: "true"
```

We will also use a new deployment for the UI, `astra-ui-deployment`. Again we are opting for just one replica here (but we could probably use more, not sure it makes that much sense for UIs to have more than 1). 

```
touch astra-ui-deployment.yaml
```

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: astra-ui
spec:
  replicas: 1
  selector:
    matchLabels:
      astra: ui
  template:
    metadata:
      labels:
        astra: ui
    spec:
      containers:
      - name: astra-ui
        image: datastaxdevs/cws-week8-frontend:latest
        ports:
        - containerPort: 3000
        envFrom:
        - configMapRef:
            name: ui-config
```

Apply both the config map and the deployment into the namespace `my-app`

```
kubectl -n my-app apply -f uiConfig.yaml
kubectl -n my-app  apply -f astra-ui-deployment.yaml
```

the UI always takes a bit longer to create because of the size of the image:

```
$ kubectl  -n my-app get pods
NAME                             READY   STATUS              RESTARTS   AGE
astra-backend-7bbf8d5644-gs6ws   1/1     Running             0          23m
astra-ui-7fd95545db-pg9lh        0/1     ContainerCreating   0          48s
```

Once the deployment is created we repeat the same steps to create the external contact point.

From the Workloads we get to the astra-ui deployment details, scroll down to the `EXPOSE` button and expose.

We specify target port 3000, expose and wait for the load balancer to be created. Once it is created, we can open it in a new window, and we should see the UI.

No firewall rules need to get created.

From here it is as before: Enter your credentials in the dialog and connect to Astra! 

## BOOM! You've now deployed an application powered by Kubernetes using the Google Kubernetes Engine and backed by DataStax Astra. Nothing is stopping you now. Go, create, and bring us all your awesome!

![Well Done](../../materials/images/welldone.jpg?raw=true)



