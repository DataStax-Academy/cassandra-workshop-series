![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨ Application Development Your App in Kuberneties ✨
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

`Cassandra Workshop Series` are an interactive experience. Datastax Developer Advocates share some knowledge about Apache Cassandra™ NoSQL database and how you build Cloud Native applications. You interact with them through chats *([youtube](https://www.youtube.com/channel/UCAIQY251avaMv7bBv5PCo-A) and [discord](https://discord.com/widget?id=685554030159593522&theme=dark))*, quizzes (menti.com), and exercises. 

In this repository, you'll find everything you need related to `week 6` of the **Cassandra Worskhop Series**. For simplicity all exercise instructions will be listed in a single `README` for each language: Java, Python. We recommended you to have some previous experience with the language you choose. 

Now if you are NOT familiar with any of those, we recommended to use Python, the is the simplest. Ready ? go !

## Workshop Materials

| Materials  | Description and Links
|---|---|
| TODOSlidedeck | [SLIDEDECK](./materials/presentation-week6.pdf) |
| Chat with us on Discord | [DISCORD](https://bit.ly/cassandra-workshop) |
| Ask Question during week | [COMMUNITY](https://community.datastax.com/spaces/172/index.html) |
| TODOHomework | TODO[here](https://community.datastax.com/questions/6377/materials-and-homework-for-week-3.html) |


## Table of Contents
| Exercises | Description and Links
|---|---|
| 1. Exercise : Setup kind | [Setup kind](#1-setup-kind) |
| 2. Exercise : Start Cassandra | [Start Cassandra](#2-start-cassandra) |

In this week we will explore taking our app from running on bare metal to running our very own kuberneties deployment. 

Note:The examples below are running the python backend.

#### Setup kind

The first thing you need to do is get the week 6 kuberneties cluster up and running. To do that you will need to start in the root directory of the project.  Then run the following

Navigate to working folder
```
cd week6-App-in-k8s/kubefiles
```

Create and configure the kind cluster
```
kind create cluster --name kind-cassandra --config 01-kind-config.yaml
```

Set the kubectl context to this new cluster
```
kubectl cluster-info --context kind-kind-cassandra
```
#### Start Cassandra

Create the namespace cass-operator
```
kubectl create ns cass-operator
```

Create the storage class
```
kubectl -n cass-operator apply -f 02-storageclass-kind.yaml
```

Setup the Cassandra operator
```
kubectl -n cass-operator apply -f 03-install-cass-operator-v1.3.yaml
```

Setup a single-node Cassandra cluster
```
kubectl -n cass-operator apply -f 04-cassandra-cluster-1nodes.yaml
```

This should setup the kuberneties kind cluster with a single cassandra node in the cass-operator namespace.

To verify that all is up an running:

```
watch kubectl -n cass-operator get pods
```

Expected output when done:

```
$ kubectl -n cass-operator get pods
NAME                             READY   STATUS    RESTARTS   AGE
cass-operator-56fcb9ff47-cw9k6   1/1     Running   0          11m
cluster1-dc1-default-sts-0       2/2     Running   0          11m
```




#### Containerize Your App
In order to get our app into k8s we will need to contanerize it.  We will be using docker to create two containers.  One for the UI and one for the backend.  

In order to build the docker images, a file named `Dockerfile` needs to be present in the working directory where we build the images. This is a reserved name, and it starts with an uppercase D.

For the python backend the `Dockerfile` will look like the following
```
FROM python:alpine3.7
COPY . /app
WORKDIR /app
RUN pip install -r requirements.txt
EXPOSE 5000
CMD ["python", "getting_started_with_astra.py"]
```

For the frontend the `Dockerfile` will look like the following
```
FROM node:12
COPY . /app
WORKDIR /app
RUN npm install
EXPOSE 3000
CMD [ "npm", "run", "start" ]
```

Note that both these Dockerfiles do essentially the same: They start with a minimum requirement, copy the working directory, install the dependencies, expose a port and define the start command for the container. 


#### Build and Load Your Container Images into Kind

Next you will need to build your docker images with the following commands.  Keep in mind these commands can take the better part of 10 minutes. In order to be able to use them in our kind cluster, we need to tag these images (tagging is not optional). We are using the tag `my-image` here in this example:

```
cd ../
docker build ./getting-started-with-astra-ui -t astra-ui:my-image
docker build ./getting-started-with-astra-python -t astra-backend:my-image
```

Now we will load the built images into kind. This can take some time...

```
kind load docker-image astra-ui:my-image --name kind-cassandra
kind load docker-image astra-backend:my-image --name kind-cassandra
```

#### Setting up your Enviroment
Since we have all the images built now let's setup the enviromental variables for the containers.  To do this we will need to retrieve the cassandra credentials from the secret store so that we can tell our app to use them when connecting. Run the following command to retrieve the info from the secret

```
kubectl get secret cluster1-superuser -n cass-operator -o yaml
```
Find the line with the credentials.  It should look something like this
```
data:
 password: QzNkRmh0c1I2OHNJSllic19jSV9UejVURkM0WFZpblMtT19mMmpOOUxjMXg2bUhxSW9razJR
 username: Y2x1c3RlcjEtc3VwZXJ1c2Vy
```
Copy the string from the password field.  You might notice this is a base64 encoded string.  We will need to decode so that we can use it.  Paste the string from the password field instead of the REPLACEME text in the following command. (Mac users: use the `-D` option for the decode.)

```
echo REPLACEME | base64 -d && echo ""
```
The output you see is your password for the database.  Copy that password into the `kubefiles/05-configMap.yaml` into the corresponding field.

Next you will need to get your connection point for the database.  This can be done by running the following command and retrieving the address value from the IP column
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
  CONNECTION_POINTS: '10.32.42.121'
  KEYSPACE: 'killrvideo'
  USERNAME: 'cluster1-superuser'
  PASSWORD:'C3dFhtsR68sIJYbs_cI_Tz5TFC4XVinS-O_f2jN9Lc1x6mHqIokk2Q'
```
Feel free to look at the 06-backend.yaml and the 07-ui.yaml to see the specific configuration option used there.  

Last item will be to install our schema in the new cassandra cluster we have setup.  To do this we will need to use the password we decoded from the previous section. Replace the CHANGEME in the following commands with the password. Use the following commands to access cqlsh. 

```
kubectl exec -it -n cass-operator cluster1-dc1-default-sts-0 /bin/bash
cqlsh -u cluster1-superuser -p CHANGEME
```

Then simply copy pase the schema from the root of week6 into the cqlsh prompt and we are good to go. 

```
CREATE KEYSPACE killrvideo WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'dc1' : 1 };

CREATE TABLE IF NOT EXISTS killrvideo.spacecraft_journey_catalog (
    spacecraft_name text,
    journey_id timeuuid,
    start timestamp,
    end timestamp,
    active boolean,
    summary text,
    PRIMARY KEY ((spacecraft_name), journey_id)
) WITH CLUSTERING ORDER BY (journey_id desc);

CREATE TABLE IF NOT EXISTS killrvideo.spacecraft_speed_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    speed double,
    reading_time timestamp,
    speed_unit text,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

CREATE TABLE IF NOT EXISTS killrvideo.spacecraft_temperature_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    temperature double,
    temperature_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

CREATE TABLE IF NOT EXISTS killrvideo.spacecraft_pressure_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    pressure double,
    pressure_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

CREATE TYPE IF NOT EXISTS killrvideo.location_udt (
    x_coordinate double,
    y_coordinate double,
    z_coordinate double);

CREATE TABLE IF NOT EXISTS killrvideo.spacecraft_location_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    location frozen<location_udt>,
    location_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);
```
Last we need to exit cqlsh and the kubernetes node
```
exit
exit
```

Now that we are all set up it is time to turn everything on.  We will create a new namespace for our app.
```
kubectl create ns my-app
```
Then we need to start applying the yaml files to the namespace.

```
cd kubefiles
kubectl -n my-app apply -f 05-configMap.yaml
kubectl -n my-app apply -f 06-backend.yaml
kubectl -n my-app apply -f 07-ui.yaml
```
Lastly we need to forward the ports to the host so that we can hit the services from our browser.
```
kubectl -n my-app port-forward pods/astra-backend 5000:5000 &
kubectl -n my-app  port-forward pods/astra-ui 3000:3000 &
```

Navigate to [http://localhost:3000](http://localhost:3000) for the UI and [http://localhost:5000](http://localhost:5000) for the backend.

Note: you will still need a plugin that unblocks CORS requests for you. 

CONGRAGULATIONS!!! you now have a full application running in kubernetes.