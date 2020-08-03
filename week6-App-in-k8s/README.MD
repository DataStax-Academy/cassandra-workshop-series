week6

In this week we will explore taking our app from running on bare metal to running our very own kuberneties deployment.

The first thing you need to do is get the week 6 kuberneties cluster up and running. To do that you will need to start in the root directory of the project.  Then run the following
```
cd week6-App-in-k8s/kubefiles
kind create cluster --name kind-cassandra --config 01-kind-config.yaml
kubectl cluster-info --context kind-kind-cassandra
kubectl create ns cass-operator
kubectl -n cass-operator apply -f 02-storageclass-kind.yaml
kubectl -n cass-operator apply -f 03-install-cass-operator-v1.3.yaml
kubectl -n cass-operator apply -f 04-cassandra-cluster-1nodes.yaml
```

This should setup the kuberneties kind cluster with a single cassandra node in the cass-operator namespace.

In order to get our app into K8 we will need to contanerize it.  We will be using docker to create two containers.  One for the UI and one for the backend.  

For the python backend the file will look like the following
```
FROM python:alpine3.7
COPY . /app
WORKDIR /app
RUN pip install -r requirements.txt
EXPOSE 5000
CMD ["python", "getting_started_with_astra.py"]
```

For the frontend the file will look like the following
```
FROM node:12
COPY . /app
WORKDIR /app
RUN npm install
EXPOSE 3000
CMD [ "npm", "run", "start" ]
```

Next you will need to build your docker images with the following commands 
```
cd ../
docker build ./getting-started-with-astra-ui -t astra-ui:my-image
docker build ./getting-started-with-astra-python -t astra-backend:my-image
```
Now we will load the built images into kind
```
kind load docker-image astra-ui:my-image --name kind-cassandra
kind load docker-image astra-backend:my-image --name kind-cassandra
```
Since we have all the images built now let's setup the enviromental variables for the containers.  To do this we will need to retreave the cassandar credentials from the secret store so that we can tell our app to use them when connecting. Run the following command to retreave the info from the secret

```
$ kubectl get secret cluster1-superuser -n cass-operator -o yaml
```
Find the line with the credentials.  It should look something like this
```
data:
 password: QzNkRmh0c1I2OHNJSllic19jSV9UejVURkM0WFZpblMtT19mMmpOOUxjMXg2bUhxSW9razJR
 username: Y2x1c3RlcjEtc3VwZXJ1c2Vy
```
Copy the string from the password field.  You might notice this is a base64 encoded string.  We will need to decode so that we can use it.  Paste the string from the password field instead of the REPLACEME text in the following command.
```
echo REPLACEME | base64 -d && echo ""
```
The output you see is your password for the database.  Copy that password into the 05-configMap.yaml into the corrisponding field 

Next you will need to get your connection point for the database.  This can be done by running the following command and retreaving the address value
```
kubectl get pods -o wide -n cass-operator
```
Once you put that value under connection points in the 05-configMap.yaml you should have something that looks like this with different values in place of CHANGEME.
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: env-config
data:
  BASE_ADDRESS: 'http://localhost:5000/api'
  USE_ASTRA: 'true'
  CONNECTION_POINTS: 'CHANGEME'
  KEYSPACE: 'killrvideo'
  USERNAME: 'cluster1-superuser'
  PASSWORD: 'CHANGEME'
```
Feel free to look at the 06-backend.yaml and the 07-ui.yaml to see the spacific configuration option used there.  

Now that we are all set up it is time to turn everything on.  We will create a new namespace for our app.
```
kubectl create ns my-app
```
Then we need to start applying the yaml files to the namespace.

```
cd kubefiles
kubectl -n my-app apply -f configMap.yaml
kubectl -n my-app apply -f ui.yaml
kubectl -n my-app apply -f backend.yaml
```
Lastly we need to forward the ports so that we can hit the services from our browser.
```
kubectl -n my-app port-forward pods/astra-backend 5000:5000
kubectl -n my-app  port-forward pods/astra-ui 3000:3000
```

Navigate to [localhost:3000](localhost:3000) for the UI and [localhost:5000](localhost:5000) for the backend.

CONGRAGULATIONS!!! you now have a full application running in kuberneties.