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

![](screenshot-2020-08-16-at-04-53-08.png)

To get a more detailed understanding of the cost, I set up daily billing exports to csv. Before we can enable billing export, we need to create a storage bucket where the csv files will be exported to. 

### Set up billing storage bucket

On the left hand side panel in the Console (accessible by the hamburger button in the top left corner), select Storage:

![](screenshot-2020-08-16-at-04-57-14.png)

Go into the storage browser and create a bucket:

![](screenshot-2020-08-16-at-04-58-05.png)

Choose a unique name (I chose workshop-billing) and accept all defaults. This is how the configuration looks in my case:

![](screenshot-2020-08-16-at-04-59-04.png)

Remember its name, we will give it to the billing export.

### Set up billing export
On the left hand side panel in the Console, go back to Billing, select Billing export and then FILE EXPORT

Configure to use your created bucket for daily exports. You will have to wait a day before you can see an export.

![](screenshot-2020-08-16-at-05-05-20.png)

## Create your Google Kubernetes Engine cluster
On the left hand side panel in the Console, go to Kubernetes Engine > Clusters

![](screenshot-2020-08-16-at-05-09-18.png)

Select CREATE CLUSTER

For my first experiment I accepted all the defaults (apart from region, where I chose a European region), which gave me a cluster costing about £1.5 per day.

For this exercise, I am following the guide for the recommended starter cluster:

![](screenshot-2020-08-16-at-05-15-11.png)

It takes a bit of time until this is created. In this screenshot you see cluster-1 set up and running (this is the cluster with all the defaults) and my-first-cluster-1 being created.

![](screenshot-2020-08-16-at-05-17-23.png)

Once created, you see the difference in allocated resource:

![](screenshot-2020-08-16-at-05-20-25.png)

Click on the connect button to open a console to control this cluster

Select to run in Cloud Shell:

![](screenshot-2020-08-16-at-05-22-27.png)

![](screenshot-2020-08-16-at-05-23-43.png)

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


![](screenshot-2020-08-16-at-05-45-51.png)

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

# Exercise 3 - Scale



