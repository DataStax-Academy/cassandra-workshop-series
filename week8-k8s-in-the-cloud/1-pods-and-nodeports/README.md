![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

## Exercise 1 - Pods and Nodeports

### 1 - Explore the cluster

We have three worker nodes:

```
kubectl get nodes
NAME                                                STATUS   ROLES    AGE     VERSION
gke-my-first-cluster-1-default-pool-b98b0390-4zfw   Ready    <none>   6m20s   v1.17.9-gke.600
gke-my-first-cluster-1-default-pool-b98b0390-jvnw   Ready    <none>   6m19s   v1.17.9-gke.600
gke-my-first-cluster-1-default-pool-b98b0390-jw8m   Ready    <none>   6m18s   v1.17.9-gke.600
```

### 2 - Check for external IP addresses

To get to the external IP addresses:

```
$ kubectl get nodes -o wide
NAME                                                STATUS   ROLES    AGE     VERSION           INTERNAL-IP   EXTERNAL-IP       OS-IMAGE                             KERNEL-VE
RSION   CONTAINER-RUNTIME
gke-my-first-cluster-1-default-pool-b98b0390-4zfw   Ready    <none>   6m37s   v1.17.9-gke.600   10.128.0.3    35.238.50.146     Container-Optimized OS from Google   4.19.112+
        docker://19.3.6
gke-my-first-cluster-1-default-pool-b98b0390-jvnw   Ready    <none>   6m36s   v1.17.9-gke.600   10.128.0.2    35.225.144.217    Container-Optimized OS from Google   4.19.112+
        docker://19.3.6
gke-my-first-cluster-1-default-pool-b98b0390-jw8m   Ready    <none>   6m35s   v1.17.9-gke.600   10.128.0.4    130.211.127.192   Container-Optimized OS from Google   4.19.112+
        docker://19.3.6
```

We will need the external IP addresses to access our cluster from outside of the network.

### 2 - Optional Step: Publish your own application to docker hub

Before you can create pods using your application docker images, you need to publish them to an accessible repository. We published them to Dockerhub, so that you can use them for your pods.  

https://hub.docker.com/u/datastaxdevs

We will use two images in our pods:

`datastaxdevs/cws-week8-frontend:latest`

`datastaxdevs/cws-week8-backend:latest`

### 3 - Create the Kuberrnetes yamls for the backend

As before, we will set up a namespace, a config map, a backend pod and a UI pod.

Note that other than in previous exercises using `kind`, you will need to use *double quotes* in your yamls, not single quotes.

Create a file named `configMap.yaml`

```
touch configMap.yaml
```

Choose the Open Editor option

<img width="600" alt="Screenshot 2020-08-18 at 11 15 29" src="https://user-images.githubusercontent.com/20337262/90501377-37e4f900-e144-11ea-81b2-8354734bf806.png">

Here is the file content. We will fill in the BASE_ADDRESS once we know it:

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: env-config
data:
  BASE_ADDRESS: ""
  USE_ASTRA: "true"
```

Autosave is enabled in the editor. Go back to Terminal.

As we don't know the BASE_ADDRESS yet, we will only create the `backend.yaml` and pod:

```
touch backend.yaml
```

`backend.yaml` content:

```
apiVersion: v1
kind: Pod
metadata:
  name: astra-backend
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
        name: env-config
```

Note that we set up a label for this pod, `astra: backend`. We will use this label to point services to this pod.

### 4 - Create namespace and backend pod

```
kubectl create namespace my-app
kubectl -n my-app apply -f configMap.yaml
kubectl -n my-app apply -f backend.yaml
```

Check your pod is running:

```
 kubectl get pods -n  my-app
NAME            READY   STATUS    RESTARTS   AGE
astra-backend   1/1     Running   0          15s
```

### 5 - Create and configure a NodePort service

For our UI to work properly with the backend, we need the backend to be serving on a publicly accessible IP address and port.  

We have several options for this. In this workshop we will explore NodePort and Cluster Ips, and this exercise is about `NodePorts`.

Create the file:

```
touch nodeport-backend.yaml
```

Content of the nodeport-backend.yaml:

```
kind: Service
apiVersion: v1
metadata:
  name: nodeport-backend
spec:
  type: NodePort
  ports:
    - port: 5000
      nodePort: 30355
  externalTrafficPolicy: Local
  selector:
    astra: backend
```

With the `selector` option, we are pointing this service to astra-backend pod.

With the `ports` option, we are mapping the port 5000 to the port 30355 of the worker node where the pod is running.  Note that you need to know on which worker node the pod is running for this to work, which is not ideal. 

We will  remove this node binding in the next exercise, but for now we stay with this.

Let's apply the nodeport service:

```
kubectl -n my-app apply -f nodeport-backend.yaml
```

### 6 -  Look up the external IP address

On which worker node is the pod running?

```
$ kubectl get pods -n  my-app -o wide
NAME            READY   STATUS    RESTARTS   AGE   IP          NODE                                                NOMINATED NODE   READINESS GATES
astra-backend   1/1     Running   0          63s   10.40.2.3   gke-my-first-cluster-1-default-pool-b98b0390-jw8m   <none>           <none>
```

The node is called:

```
gke-my-first-cluster-1-default-pool-b98b0390-jw8m
```


What is the external IP address of this worker node?

Refer to the output that we inspected earlier with 

```
kubectl get nodes -o wide
```

Make a note of the external IP address.

### 7 - Create the required firewall rules

Before we can access the port 30355 from outside, we will need to create a firewall rule that allows the access.

```
gcloud compute firewall-rules create test-node-port --allow tcp:30355
```

We are now able to test external access to the Kubernetes pod. With your local browser, navigate to `http://<your_external_address>:30355`

If your test is successful, you should see this:

```
Hi, I am the Python backend API. Please connect me to Astra via UI.
```

### 8 - Update BASE_ADDRESS in the config, create UI pod

Ok, now we also know what the BASE_ADDRESS needs to be for the UI:

```
BASE_ADDRESS = "http://<your_external_address>:30355/api"
```

Modify the configMap.yaml with the correct base address:

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: env-config
data:
  BASE_ADDRESS: "http://<your_external_address>:30355/api"
  USE_ASTRA: "true"
```

Apply the configMap.yaml again

```
$ kubectl -n my-app apply -f configMap.yaml
configmap/env-config configured
```

Create the ui.yaml

```
touch ui.yaml
```

```
apiVersion: v1
kind: Pod
metadata:
  name: astra-ui
  labels:
    astra: ui
spec:
  containers:
  - name: astra-ui
    image: datastaxdevs/cws-week8-frontend:latest
    envFrom:
    - configMapRef:
        name: env-config
```

Note the label `astra: ui`. We will need this label to point another nodeport service to the UI.

Create the pod:

```
kubectl -n my-app apply -f ui.yaml
```

The UI image is much bigger than the backend image, so it takes a bit longer to create the pod:

```
$ kubectl -n my-app get pods
NAME            READY   STATUS              RESTARTS   AGE
astra-backend   1/1     Running             0          27m
astra-ui        0/1     ContainerCreating   0          17s
```

```
$ kubectl -n my-app get pods
NAME            READY   STATUS    RESTARTS   AGE
astra-backend   1/1     Running   0          28m
astra-ui        1/1     Running   0          64s
```

Now we need to create another nodeport service for the UI.

```
touch nodeport-ui.yaml
```

Note the selector for the ui, and this time we are mapping to port 30333

```
kind: Service
apiVersion: v1
metadata:
  name: nodeport-ui
spec:
  type: NodePort
  ports:
    - port: 3000
      nodePort: 30333
  externalTrafficPolicy: Local
  selector:
    astra: ui
```

### 9 - Create the Ui NodePort and firewall rule

```
kubectl -n my-app  apply -f nodeport-ui.yaml
```

Look up where the UI pod is running:

```
$ kubectl -n my-app get pods -o wide
NAME            READY   STATUS    RESTARTS   AGE     IP          NODE                                                NOMINATED NODE   READINESS GATES
astra-backend   1/1     Running   0          31m     10.40.2.3   gke-my-first-cluster-1-default-pool-b98b0390-jw8m   <none>           <none>
astra-ui        1/1     Running   0          3m51s   10.40.0.4   gke-my-first-cluster-1-default-pool-b98b0390-jvnw   <none>           <none>
```

Look up the external IP address and make a note.

Create another firewall rule

```
gcloud compute firewall-rules create test-node-port2 --allow tcp:30333
```

Test access: With your browser navigate to the external IP address for the UI, port 30333

You should see your usual UI with the dialog that allows to upload your credentials to access Astra. The serving machine is not very powerful, and it might take a bit of time for the UI to render.

Once the UI dialog is served, connect to Astra for your usual tests. 

The nodeport was very easy to set up, but the obvious disadvantage is that it is per worker node. If the pod gets launched on a different worker node, then we would need to look up the IP of this node first and point the UI to a different base address. 

## Ok great, you've explored pods and nodeports. Now, let's jump into deployments and load balancers [HERE](../2-deployments-and-load-balancers).
