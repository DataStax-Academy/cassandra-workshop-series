![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨ Application Development Your App in Kubernetes ✨
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

`Cassandra Workshop Series` are an interactive experience. Datastax Developer Advocates share some knowledge about Apache Cassandra™ NoSQL database and how you build Cloud Native applications. You interact with them through chats *([youtube](https://www.youtube.com/channel/UCAIQY251avaMv7bBv5PCo-A) and [discord](https://discord.com/widget?id=685554030159593522&theme=dark))*, quizzes (menti.com), and exercises. 

In this repository, you'll find everything you need related to `week 6` of the **Cassandra Worskhop Series**. For simplicity all exercise instructions will be listed in a single `README` for each language: Java, Python. We recommended you to have some previous experience with the language you choose. 

## Workshop Materials

| Materials  | Description and Links
|---|---|
| SlideDeck | [HERE](./materials/presentation-week6.pdf) |
| Chat with us on Discord | [DISCORD](https://bit.ly/cassandra-workshop) |
| Ask Question during week | [COMMUNITY](https://community.datastax.com/spaces/172/index.html) |
| Homework | [HERE](https://community.datastax.com/questions/7416/materials-and-homework-for-week-6.html) |


## Table of Contents
| Exercises | Description and Links
|---|---|
| Exercise 1 : Setup kind | [Instructions](#1-setup-kind) |
| Exercise 2 : Start Cassandra | [Instructions](#2-start-cassandra) |
| Exercise 3 : Containerize Your App | [Instructions](#3-containerize-your-app) |
| Exercise 4: Build and Load Your Container Images into Kind | [Instructions](#4-build-and-load-your-container-images-into-kind) |
| Exercise 5 : Setting up your Enviroment | [Instructions](#5-setting-up-your-enviroment) |
| Exercise 6 : Test | [Instructions](#6-test) |
| Exercise 7 : Cleaning Up | [Instructions](#7-cleaning-up) |

In this week we will explore taking our app from running on bare metal to running our very own kuberneties deployment. 

**⚠️ Important**
```
As a prerequisites you need DOCKER, KUBECTL and KIND install on the machine. 
Hardware requirements are at least 6GB allocated to Docker Memory. 
This work has been done last week. We provide you the steps as a reminder below
```
[installation steps as a reminders](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week5-Cass-in-k8s#create-a-kubernetes-cluster)

#### 1. Setup kind

The first thing you need to do is get the week 6 kuberneties cluster up and running. To do that you will need to start in the root directory of the project.  Then run the following

✅  **Step1a:** Navigate to working folder
```bash
cd week6-App-in-k8s/kubefiles
```

✅  **Step1b:** Create and configure the kind cluster
```bash
kind create cluster --name kind-cassandra --config 01-kind-config.yaml
```

✅  **Step1c:** Set the kubectl context to this new cluster
```bash
kubectl cluster-info --context kind-kind-cassandra
```
#### 2. Start Cassandra

✅  **Step2a:** Create the namespace `cass-operator`
```bash
kubectl create ns cass-operator
```

✅  **Step2b:** Create the storage class
```bash
kubectl -n cass-operator apply -f 02-storageclass-kind.yaml
```

✅  **Step2c:** Setup the Cassandra operator this might take some time
```bash
kubectl -n cass-operator apply -f 03-install-cass-operator-v1.3.yaml
```

✅  **Step2d:** Setup a single-node Cassandra cluster
```bash
kubectl -n cass-operator apply -f 04-cassandra-cluster-1nodes.yaml
```

This should setup the kuberneties kind cluster with a single cassandra node in the cass-operator namespace.

✅  **Step2e:** To verify that all is up an running from another terminal run:

```bash
watch kubectl -n cass-operator get pods
```

📗 Expected output when done. ⚠️ Expect this to take 5 mins. 

```bash
$ kubectl -n cass-operator get pods
NAME                             READY   STATUS    RESTARTS   AGE
cass-operator-56fcb9ff47-cw9k6   1/1     Running   0          11m
cluster1-dc1-default-sts-0       2/2     Running   0          11m
```


#### 3. Containerize Your App

In order to get our app into k8s we will need to contanerize it.  We will be using docker to create two containers.  One for the UI and one for the backend.  

In order to build the docker images, a file named `Dockerfile` needs to be present in the working directory where we build the images. This is a reserved name, and it starts with an uppercase D.

📗 For the python backend the `Dockerfile` will look like the following

```vim
FROM python:alpine3.7
COPY . /app
WORKDIR /app
RUN pip install -r requirements.txt
EXPOSE 9090
CMD ["python", "getting_started_with_astra.py"]
```

📗  The Java backend `Dockerfile`:

```vim
FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 9090
```

📗  For the frontend the `Dockerfile` will look like the following

```vim
FROM node:12
COPY . /app
WORKDIR /app
RUN npm install
EXPOSE 3000
CMD [ "npm", "run", "start" ]
```

Note that these Dockerfiles do essentially the same: They start with a minimum requirement, copy the working directory, install the dependencies, expose a port and define the start command for the container. 


#### 4. Build and Load Your Container Images into Kind

✅  **Step4a:**  Next you will need to build your docker images with the following commands.  Keep in mind these commands can take the better part of 10 minutes. In order to be able to use them in our kind cluster, we need to tag these images (tagging is not optional). We are using the tag `my-image` here in this example.  Choose your favorate language and the corrisponding commands below:

```bash
cd ../
docker build ./getting-started-with-astra-ui -t astra-ui:my-image
```

**![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-java.png)**

```bash
mvn clean package dockerfile:build
```
**![PYTHON](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-python.png)**

```bash
docker build ./getting-started-with-astra-python -t astra-backend:my-image
```
✅  **Step4b:** As an optional step, we can test our docker containers for the connection with Astra, before we use them in the kubernetes cluster.

```bash
docker run -e USE_ASTRA='true' -p 9090:9090 astra-backend:my-image
docker run -e USE_ASTRA='true' -e BASE_ADDRESS='http://localhost:9090/api' -p 3000:3000 astra-ui:my-image
```

*WARNING - Update the code in your cluster - see here for more details: https://community.datastax.com/questions/7478/index.html*

Navigate to [http://localhost:9090](http://localhost:9090) and to [http://localhost:3000](http://localhost:3000), complete the connection dialog with your details, and connect to your Astra database. 

⚠️ If you're using a DataStax cloud instance, remember to use the EC2 instance name in place of `localhost`, for example: `http://wksv136191.cws-week6.datastaxtraining.com:9090/`.

Stop the docker containers:

```bash
docker ps
docker stop <container_id>
```

✅  **Step4c:** Now we will load the built images into kind. ⚠️ This can take a few minutes. 

``` bash
kind load docker-image astra-ui:my-image --name kind-cassandra
kind load docker-image astra-backend:my-image --name kind-cassandra
```

📗 Expected output when done:

```bash
$ kind load docker-image astra-backend:my-image --name kind-cassandra
Image: "astra-backend:my-image" with ID "sha256:ae76fd47c57d8708e2f3ffaf6610e23e28414b4c093883737de60adcf823529c" not yet present on node "kind-cassandra-control-plane", loading...
Image: "astra-backend:my-image" with ID "sha256:ae76fd47c57d8708e2f3ffaf6610e23e28414b4c093883737de60adcf823529c" not yet present on node "kind-cassandra-worker", loading...

$ kind load docker-image astra-ui:my-image --name kind-cassandra
Image: "astra-ui:my-image" with ID "sha256:a82388e953be8383ce9cf3d42a7c8f6de60ee90b5dde0cc6639519f9bf3e0394" not yet present on node "kind-cassandra-control-plane", loading...
Image: "astra-ui:my-image" with ID "sha256:a82388e953be8383ce9cf3d42a7c8f6de60ee90b5dde0cc6639519f9bf3e0394" not yet present on node "kind-cassandra-worker", loading...
$ 
```

#### 5. Setting up your Enviroment

✅ **Step5a:** Since we have all the images built now let's setup the enviromental variables for the containers.  To do this we will need to retrieve the cassandra credentials from the secret store so that we can tell our app to use them when connecting. Run the following command to retrieve the info from the secret

```bash
kubectl get secret cluster1-superuser -n cass-operator -o yaml
```
📗 Find the line with the credentials. It should look something like this

```bash
data:
 password: QzNkRmh0c1I2OHNJSllic19jSV9UejVURkM0WFZpblMtT19mMmpOOUxjMXg2bUhxSW9razJR
 username: Y2x1c3RlcjEtc3VwZXJ1c2Vy
```

✅ **Step5b:** Copy the string from the password field.  You might notice this is a base64 encoded string.  We will need to decode so that we can use it.  Paste the string from the password field instead of the REPLACEME text in the following command. (Mac users: use the `-D` option for the decode.) ⚠️ Important, you can't skip this step, as the password changes with each installation.

```bash
echo REPLACEME | base64 -d && echo ""
```

The output you see is your password for the database.  Copy that password into the `kubefiles/05-configMap.yaml` into the corresponding field.

✅ **Step5c:** Next you will need to get your connection point for the database.  We will use the cluster service for that.

To find the name of the service:

```bash
kubectl get services -n cass-operator
```

📗 Expected output:

```bash
$ kubectl get services -n cass-operator
NAME                                  TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
cass-operator-metrics                 ClusterIP   10.96.69.243     <none>        8383/TCP,8686/TCP   5h57m
cassandradatacenter-webhook-service   ClusterIP   10.103.222.183   <none>        443/TCP             5h57m
cluster1-dc1-all-pods-service         ClusterIP   None             <none>        <none>              5h57m
cluster1-dc1-service                  ClusterIP   None             <none>        9042/TCP,8080/TCP   5h57m
cluster1-seed-service                 ClusterIP   None             <none>        <none>              5h57m
```

The service to use is the `cluster1-dc1-service`. Note that its type is a ClusterIP. 

In order to use it from our pods in a different namespace, we need to provide the DNS name of the service:

```bash
cluster1-dc1-service.cass-operator.svc.cluster.local
```

Once you put that value under connection points in the 05-configMap.yaml you should have something that looks this.

```vim
apiVersion: v1
kind: ConfigMap
metadata:
  name: env-config
data:
  BASE_ADDRESS: 'http://localhost:9090/api'
  USE_ASTRA: 'false'
  CONNECTION_POINTS: 'cluster1-dc1-service.cass-operator.svc.cluster.local'
  KEYSPACE: 'killrvideo'
  USERNAME: 'cluster1-superuser'
  PASSWORD:'C3dFhtsR68sIJYbs_cI_Tz5TFC4XVinS-O_f2jN9Lc1x6mHqIokk2Q'
```

⚠️ If you're using a DataStax cloud instance, remember to use the EC2 instance name in place of `localhost`, for example: `http://wksv136191.cws-week6.datastaxtraining.com:9090/api`.

![WARNING - Update the code in your cluster](https://community.datastax.com/storage/attachments/1292-workshop-week6-pull-code-update.png)

Feel free to look at the 06-backend.yaml and the 07-ui.yaml to see the specific configuration option used there.  

✅ **Step5d:** Last item will be to install our schema in the new cassandra cluster we have setup.  To do this we will need to use the password we decoded from the previous section. Replace the CHANGEME in the following commands with the password. Use the following commands to access cqlsh. 

```bash
kubectl exec -it -n cass-operator cluster1-dc1-default-sts-0 /bin/bash
```

```bash
cqlsh -u cluster1-superuser -p CHANGEME
```

Then simply copy pase the schema from the root of week6 into the cqlsh prompt and we are good to go. 

```sql
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

✅  **Step5e:** Last we need to exit cqlsh, and then kubernetes cassandra pod.

```bash
exit
exit
```

✅  **Step5f:** Now that we are all set up it is time to turn everything on.  We will create a new namespace `my-app` for our app.

```bash
kubectl create ns my-app
```
✅ **Step5g:** Then we need to start applying the yaml files to the namespace.

```bash
cd kubefiles
kubectl -n my-app apply -f 05-configMap.yaml
kubectl -n my-app apply -f 06-backend.yaml
kubectl -n my-app apply -f 07-ui.yaml
```

✅  **Step5h:** Lastly we need to forward the ports to the host so that we can hit the services from our browser.

```bash
kubectl -n my-app port-forward --address 0.0.0.0 pod/astra-backend 9090 &
kubectl -n my-app port-forward --address 0.0.0.0 pod/astra-ui 3000 &
```

✅  **Step5i:** Navigate to [http://localhost:3000](http://localhost:3000) for the UI and [http://localhost:9090](http://localhost:9090) for the backend.

![WARNING - Update the code in your cluster](https://community.datastax.com/storage/attachments/1292-workshop-week6-pull-code-update.png)

⚠️ You will still need a plugin that unblocks CORS requests for you. 

CONGRATULATIONS!!! you now have a full application running in kubernetes.

### 6. Test

✅  **Step6a:**  From the UI at [http://localhost:3000](http://localhost:3000)  setup and run a test journey.  Once it completes run the below command to check what data into the table. Be sure to replace CHANGEME with your saved decoded password from before.

```bash
kubectl exec -it -n cass-operator cluster1-dc1-default-sts-0 -- cqlsh -e "select * from killrvideo.spacecraft_journey_catalog limit 5;" -u cluster1-superuser -p CHANGEME
```

### 7. Cleaning Up

✅  **Step7a:** To clean up the app simply run the following commands

```bash
kubectl delete ns cass-operator
kubectl delete ns my-app
kind delete cluster --name=kind-cassandra
```

Go forth and be awesome.
