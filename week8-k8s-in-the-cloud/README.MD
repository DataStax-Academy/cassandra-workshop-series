# Porting my application to Google Cloud Platform
# Summary
Cloud Native App needs to live in the cloud
Advantages: Publicly accessible via public IP addresses
# This is an example for hosting on Google Cloud Platform
Other solutions are available, for example AWS or Azure. 
# Prerequisites
To follow these steps, you will need to have your own GCP account. 
A GCP account can be created for free, and with a new account you get a one-year free trial phase with $300 worth of credits to experiment with the GCP products. You will need some of these credits to follow along. 
The cost of having a small kubernetes cluster for a few days came to about £2 per day.
You will need your own cloud account. This is not an exercise that you can do locally or with a loaned cloud instance.

# Setup steps
## Create GCP account

Sign in or set up a new account on Google Cloud Platform:
https://cloud.google.com/

You will need to provide a valid credit card in order to complete the account setup. If you cannot provide a credit card or bank account, you will not be able to proceed with this exercise. In this case, just watch and learn for future reference. 

Once you are signed in, proceed to the Console. The Console is your starting point in GCP, and we will get back to the Console again and again.

## Create a project

A first default project is created for you, named My First Project. We are using this project to host our Kubernetes cluster. 

## Billing
You entered your credit card, so you want to keep a good eye on the costs, even during the free trial. One way is to export your billing. 

You can access the billing overview from the Console in the left hand side panel. 

Reports shows how much you've been charged so far.

<img width="522" alt="Screenshot 2020-08-16 at 04 53 08" src="https://user-images.githubusercontent.com/20337262/90419685-d757ac80-e0ae-11ea-8f25-25410f05f405.png">

To get a more detailed understanding of the cost, I set up daily billing exports to csv. Before we can enable billing export, we need to create a storage bucket where the csv files will be exported to. 

### Set up billing storage bucket

On the left hand side panel in the Console (accessible by the hamburger button in the top left corner), select Storage:

<img width="526" alt="Screenshot 2020-08-16 at 04 57 14" src="https://user-images.githubusercontent.com/20337262/90419818-040bc400-e0af-11ea-9948-7ab61c690a7c.png">

Go into the storage browser and create a bucket:

<img width="583" alt="Screenshot 2020-08-16 at 04 58 05" src="https://user-images.githubusercontent.com/20337262/90419884-1980ee00-e0af-11ea-8524-8f7cd2ab70fc.png">

Choose a unique name (I chose workshop-billing) and accept all defaults. This is how the configuration looks in my case:

<img width="630" alt="Screenshot 2020-08-16 at 04 59 04" src="https://user-images.githubusercontent.com/20337262/90419956-374e5300-e0af-11ea-899a-f9adc4bc2628.png">

Remember its name, we will give it to the billing export.

### Set up billing export
On the left hand side panel in the Console, go back to Billing, select Billing export and then FILE EXPORT

Configure to use your created bucket for daily exports. You will have to wait a day before you can see an export.

<img width="711" alt="Screenshot 2020-08-16 at 05 05 20" src="https://user-images.githubusercontent.com/20337262/90419999-47fec900-e0af-11ea-9242-99aa1fa5b624.png">

## Create your Google Kubernetes Engine cluster
On the left hand side panel in the Console, go to Kubernetes Engine > Clusters

<img width="451" alt="Screenshot 2020-08-16 at 05 09 18" src="https://user-images.githubusercontent.com/20337262/90420046-551bb800-e0af-11ea-8817-4862c6322305.png">

Select CREATE CLUSTER

For my first experiment I accepted all the defaults (apart from region, where I chose a European region), which gave me a cluster costing about £1.5 per day.

For this exercise, I am following the guide for the recommended starter cluster:

<img width="783" alt="Screenshot 2020-08-16 at 05 15 11" src="https://user-images.githubusercontent.com/20337262/90420081-649b0100-e0af-11ea-8372-afec9e250e2e.png">

It takes a bit of time until this is created (3-5mins). Here you see `my-first-cluster-1` being created.

<img width="802" alt="Screenshot 2020-08-18 at 11 09 04" src="https://user-images.githubusercontent.com/20337262/90500909-7332f800-e143-11ea-9392-fe597b65b835.png">

Once created, you see the allocated resource:



Click on the connect button to open a console to control this cluster

Select to run in Cloud Shell:

<img width="783" alt="Screenshot 2020-08-16 at 05 22 27" src="https://user-images.githubusercontent.com/20337262/90420274-a5931580-e0af-11ea-934d-0f23f9623d47.png">

<img width="1364" alt="Screenshot 2020-08-16 at 05 23 27" src="https://user-images.githubusercontent.com/20337262/90420298-adeb5080-e0af-11ea-892c-cc6f7bd2b84a.png">

# Exercise 1 - Minimal deployment

## Explore the cluster

We have three worker nodes:

```
kubectl get nodes
NAME                                                STATUS   ROLES    AGE     VERSION
gke-my-first-cluster-1-default-pool-b98b0390-4zfw   Ready    <none>   6m20s   v1.17.9-gke.600
gke-my-first-cluster-1-default-pool-b98b0390-jvnw   Ready    <none>   6m19s   v1.17.9-gke.600
gke-my-first-cluster-1-default-pool-b98b0390-jw8m   Ready    <none>   6m18s   v1.17.9-gke.600
```

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

We will need the external IP addresses to access our cluster from anywhere.

## Optional: Publish your application to docker hub

Before you can create pods using your application docker images, you need to publish them to an accessible repository. I published them to Dockerhub, so that you can use them for your pods.  

https://hub.docker.com/r/bswynnerton/workshop/tags

I published them both into the same repo and tagged by function, one with UI tag, one with backend tag.

# Create the yamls

As before, we will set up a namespace, a config map, a backend pod and a UI pod.

Note that other than kind, you will need to use doublequotes in your yamls, not single quotes.

Create a file named configMap.yaml

```
touch configMap.yaml
```

Choose the Open Editor option


<img width="1369" alt="Screenshot 2020-08-16 at 05 23 43" src="https://user-images.githubusercontent.com/20337262/90420350-c3607a80-e0af-11ea-966c-35e2941a050d.png">

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

As we don't know the BASE_ADDRESS yet, we will only create the backend.yaml and pod:

```
touch backend.yaml
```

backend.yaml content:

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
    image: bswynnerton/workshop:backend      
    ports:
        - containerPort: 5000
    envFrom:
    - configMapRef:
        name: env-config
```

Note that we set up a label for this pod, `astra: backend`. We will use this label to point services to this pod.

### Create namespace and pods

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

For our UI to work properly with the backend, we need the backend to be serving on a publicly accessible IP address and port.  

We have several options for this. In this workshop we will explore NodePort and Ingress, and this exercise is about NodePorts.

Let's set up the nodeport-backend.yaml:

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

For now we stay with this.

Let's apply the nodeport service:

```
kubectl -n my-app apply -f nodeport-backend.yaml
```



On which worker node is it running?

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

The external IP address is:

```
130.211.127.192
```

Before we can access the port 30355 from outside, we will need to create a firewall rule that allows the access.

```
gcloud compute firewall-rules create test-node-port --allow tcp:30355
```

Ok, let's test: With your browser, navigate to:

http://130.211.127.192:30355/

If your test is successful, you should see this:

```
Hi, I am the Python backend API. Please connect me to Astra via UI.
```

Ok, now we also know what the BASE_ADDRESS needs to be for the UI:

```
BASE_ADDRESS = "http://130.211.127.192:30355/api"
```

Modify the configMap.yaml with the correct base address:

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: env-config
data:
  BASE_ADDRESS: "http://130.211.127.192:30355/api"
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
    image: bswynnerton/workshop:ui
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

Create the nodeport:

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

The external IP address is:

```
35.225.144.217
```

Create another firewall rule

```
gcloud compute firewall-rules create test-node-port --allow tcp:30333
```

Test access:

```
http://35.225.144.217:30333/
```

You should see your usual UI with the dialog that allows to upload your credentials to access Astra.

You can proceed to further tests from here.

The nodeport was very easy to set up, but the obvious disadvantage is that it is per worker node. If the pod gets launched on a different worker node, then we would need to look up the IP of this node first and point the UI to a different base address. 


# Exercise 2 - Remove node binding

Nodeports are easy to set up, but they have a big disadvantage: they are bound to a particular kubernetes worker node. If a pod gets deleted and recreated, it might get scheduled on a different worker node, and we won't be able to reach it via the nodeport.

A way to abstract this node binding is to deploy the pods as deployments and to use load balancer services to address the pods in the deployment and correctly route any external traffic.

Let's delete the cluster that we had so far:

<img width="926" alt="Screenshot 2020-08-18 at 09 56 24" src="https://user-images.githubusercontent.com/20337262/90498580-6a8cf280-e140-11ea-8cb6-f3be7874a34b.png">

This might take a little bit (3-5 mins)

Then we create a new minimal cluster based on the recommendations, again this might take 3-5mins. You can do this while the other cluster is deleting.

<img width="883" alt="Screenshot 2020-08-18 at 09 59 14" src="https://user-images.githubusercontent.com/20337262/90498703-8abcb180-e140-11ea-86db-be4b12fb854d.png">

Connect to the terminal and create a namespace:

```
kubectl create namespace my-app
```

let's create different config maps for backend and UI.

backendConfig.yaml

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: backend-config
data:
  USE_ASTRA: "true"
```

apply this in the namespace `my-app`

```
kubectl -n my-app apply -f backendConfig.yaml
```

Next, instead of creating a single pod for the backend, we create a deployment that specifies how many replicas we want to run for a pod. Due to architecture of the backend app (and the way it stores the Astra credentials) we can only have one replica. But in other cases, you might want to run with 3 or so.

Here is the deployment yaml `astra-backend-deployment.yaml`

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
        image: bswynnerton/workshop:backend
        ports:
        - containerPort: 5000
        envFrom:
        - configMapRef:
            name: backend-config
```

let's apply this is the namespace too:

```
kubectl -n my-app apply -f astra-backend-deployment.yaml
```

The pods start very quicly:

```
$ kubectl get pods -n my-app
NAME                             READY   STATUS    RESTARTS   AGE
astra-backend-7bbf8d5644-gs6ws   1/1     Running   0          13s
```

We can now see the deployment in our workloads:

<img width="955" alt="Screenshot 2020-08-18 at 10 15 10" src="https://user-images.githubusercontent.com/20337262/90498780-a627bc80-e140-11ea-8e62-916b73a59404.png">

Click on the deployment to see its details, and then scroll down until you see the EXPOSE option for exposing services:

<img width="1118" alt="Screenshot 2020-08-18 at 10 16 33" src="https://user-images.githubusercontent.com/20337262/90498836-b63f9c00-e140-11ea-87a8-5e41e9ed1871.png">

Enter the target port 5000 and click the EXPOSE button.

<img width="819" alt="Screenshot 2020-08-18 at 10 18 06" src="https://user-images.githubusercontent.com/20337262/90498882-c48db800-e140-11ea-988b-28d98bf8596b.png">

This will create a new service `astra-backend`. You'll see a few spinning wheels while this is happening.

<img width="675" alt="Screenshot 2020-08-18 at 10 18 23" src="https://user-images.githubusercontent.com/20337262/90498954-d53e2e00-e140-11ea-953b-e05c90ee7dea.png">

When done, it will look like this:

<img width="682" alt="Screenshot 2020-08-18 at 10 20 03" src="https://user-images.githubusercontent.com/20337262/90498986-e129f000-e140-11ea-8776-fe6d8862c0d0.png">

Now click on the external endpoint, this opens a new window, and we see the homepage of the Python backend.

<img width="660" alt="Screenshot 2020-08-18 at 10 20 54" src="https://user-images.githubusercontent.com/20337262/90499035-f272fc80-e140-11ea-9423-3684acf8e97d.png">

How does this all look in kubernetes structure?

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

Let's inspect the astra-backend-service that we created:

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

Let's do the same steps for the UI. Again, let's use a UI specific configMap, `uiConfig.yaml`

Note that we now use the external endpoint for the astra-backend-service as the base address in the UI config. The external contact point is now bound to the lifetime of the ingress service, no longer the pod on the worker node. 

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: ui-config
data:
  BASE_ADDRESS: "http://35.225.144.217/api"
  USE_ASTRA: "true"
```

We will also use a new deployment for the UI, `astra-ui-deployment`. Again we are opting for just one replica here (but we could probably use more, not sure it makes that much sense for UIs to have more than 1). 

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
        image: bswynnerton/workshop:ui
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

From the Workloads we get to the astra-ui deployment details, scroll down to the EXPOSE . button and expose.

We specify target port 3000, expose and wait for the load balancer to be created. Once it is created, we can open it in a new window, and we should see the UI.

From here it is as before: Enter your credentials in the dialog and connect to Astra!


# Exercise 3 - Scale



