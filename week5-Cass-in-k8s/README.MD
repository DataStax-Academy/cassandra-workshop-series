![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨ Cassandra in Kubernetes✨

[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

`Cassandra Workshop Series` are an interactive experience. Datastax Developer Advocates share some knowledge about Apache Cassandra™ NoSQL database and how you build Cloud Native applications. You interact with them through chats *([youtube](https://www.youtube.com/channel/UCAIQY251avaMv7bBv5PCo-A) and [discord](https://discord.com/widget?id=685554030159593522&theme=dark))*, quizzes (menti.com), and exercises. 

In this repository, you'll find everything you need related to `week 5` of the **Cassandra Worskhop Series**. For simplicity all exercise instructions will be listed in a single `README`. 

## Workshop Materials

| Materials  | Description and Links
|---|---|
| Slidedeck | [SLIDEDECK](./materials/presentation-week5.pdf) |
| Chat with us on Discord | [DISCORD](https://bit.ly/cassandra-workshop) |
| Ask Question during week | [COMMUNITY](https://community.datastax.com/spaces/172/index.html) |
| Recording LIVESTREAM NA, LATAM |  📅 [July 29th 12EDT](#) |
| Recording LIVESTREAM APAC, EMEA | 📅 [July 30th 12:30 IST](#) |
| Homework | [here](#) |

## Exercises

| Title  | Description
|---|---|
| **1. Installation and prerequisites** | [Instructions](#1-setup-your-cluster) |
| **2. Working with the Cassandra Operator** | [Instructions](#2-install-cass-operator)  |
| **3. Export metrics in Prometheus Grafana** | [Instructions](#3-prometheus_grafana)  |
| **4. Browse your cluster** | [Instructions](#4-browse-your-cluster)  |
| **5. Scale up our cluster** | [Instructions](#5-scale-up-our-cluster)  |

## 1. Create a Kubernetes Cluster

This guide will show you how to install a **Kubernetes** cluster on a single machine in order to later, deploy an Apache Cassandra™ database. It be composed of one *master* and multiple *workers*. 

**IMPORTANT** If you are using our cloud test instance (like `wks78879.k8s-workshop.datastaxtraining.com`), please start with the Step 7!

### 1.1 Install Docker

Docker is an open-source project that automates the deployment of software applications inside containers by providing an additional layer of abstraction and automation of OS-level virtualization on Linux.

![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : To install on **windows** please use the following installer [Docker Dekstop for Windows Installer](https://download.docker.com/win/stable/Docker%20Desktop%20Installer.exe)

![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : To install on **MAC OS**  use [Docker Dekstop for MAC Installer](https://download.docker.com/mac/stable/Docker.dmg) or [homebrew](https://docs.brew.sh/Installation) with following commands:
```bash
# Fetch latest version of homebrew and formula.
brew update              
# Tap the Caskroom/Cask repository from Github using HTTPS.
brew tap caskroom/cask                
# Searches all known Casks for a partial or exact match.
brew search docker                    
# Displays information about the given Cask
brew cask info docker
# Install the given cask.
brew cask install docker              
# Remove any older versions from the cellar.
brew cleanup
# Validate installation
docker -v
```

![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) : To install on linux (centOS) you can use the following commands
```bash
# Remove if already install
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
# Utils
sudo yum install -y yum-utils

# Add docker-ce repo
sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
# Install
sudo dnf -y  install docker-ce --nobest
# Enable service
sudo systemctl enable --now docker
# Get Status
systemctl status  docker

# Logout....Lougin
exit
# Create user
sudo usermod -aG docker $USER
newgrp docker

# Validation
docker images
docker run hello-world
docker -v
```

### 1.2 Install Docker Compose

Docker Compose is a tool for defining and running multi-container Docker applications. It uses YAML files to configure the application's services and performs the creation and start-up process of all the containers with a single command. The `docker-compose` CLI utility allows users to run commands on multiple containers at once, for example, building images, scaling containers, running containers that were stopped, and more. Please refer to [Reference Documentation](https://docs.docker.com/compose/install/) if you have for more detailed instructions.

![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : Already **included** in the previous package *Docker for Windows*

![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : Already **included** in the previous package *Docker for Mac*

![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) : To install on linux (centOS) you can use the following commands

```bash
# Download deliverable and move to target location
sudo curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Allow execution
sudo chmod +x /usr/local/bin/docker-compose
```

### 1.3 Install Kubernetes command-line tool (Kubectl)

The Kubernetes command-line tool, `kubectl`, allows you to run commands against Kubernetes clusters. You can use kubectl to deploy applications, inspect and manage cluster resources, and view logs. Please refer to [Reference Documentation](https://kubernetes.io/docs/tasks/tools/install-kubectl/) for more detailed instructions.

![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : To install on **windows** please use the following installer [Kubectl for Windows](https://storage.googleapis.com/kubernetes-release/release/v1.18.0/bin/windows/amd64/kubectl.exe)

![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : To install on **MAC OS** please use the following [homebrew](https://docs.brew.sh/Installation) commands:
```bash
brew install kubectl
```

![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) To install on linux (centOS) you can use the following commands. 

*If the binary not working for your linux distribution you have more download urls in the [KUBETCTL DOCUMENTATION](https://kubernetes.io/docs/tasks/tools/install-kubectl/#install-kubectl-on-linux)*.

```bash
# Download binary and move to /bin
curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"

# Make the file executable
chmod +x ./kubectl

# Move the binary in to your PATH.
sudo mv ./kubectl /usr/local/bin/kubectl

#Test to ensure the version you installed is up-to-date:
kubectl version --client
```

### 1.3 Install `watch`

During the workshop some bootstrap can last for a few minutes and the utilty `watch` can come to he rescue to avoid typing the same command multiple times to see the evolutions.

![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : The utility does not exist on **windows** but this is quite easy to create your own `watch.bat` file containing :
```bash
@ECHO OFF
:loop
  cls
  %*
  timeout /t 5 > NUL
goto loop
```

![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : To install on **MAC OS** please use the following [homebrew](https://docs.brew.sh/Installation) commands:
```bash
brew install watch
```

![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) the utility is already installed in **centOS**

```bash
echo "Linux Rocks"
```

### 1.4 Install Kind

kind (`kind`)cis a tool for running local Kubernetes clusters using Docker container “nodes”. kind was primarily designed for testing Kubernetes itself, but may be used for local development or CI. Please refer to [Reference Documentation](https://kind.sigs.k8s.io/docs/user/quick-start/) for more detailed instructions.

![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : To install on **windows** please dowmload the [executable](https://kind.sigs.k8s.io/dl/v0.7.0/kind-windows-amd64) and place it on the PATH. You can also use **[Chocolatey](https://chocolatey.org/packages/kind)** very clever package manager for windows.
```bash
choco install kind
```

![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : To install on **MAC OS** please use the following [homebrew](https://docs.brew.sh/Installation) commands:
```bash
brew install kind
```

![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) To install on linux (centOS) you can use the following commands
```bash
curl -Lo ./kind https://github.com/kubernetes-sigs/kind/releases/download/v0.7.0/kind-$(uname)-amd64
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind
```

Check that the installation is successful. Starting for now all command will be the same on each platform, as a such we will keep providing a single command. We will mark with a blue book the command (📘) and a green book (📗) to show expected result.

📘 **Command to execute**
```bash
# Check existing Kind clusters
kind get clusters
```

📗 **Expected output**
```bash
No kind clusters found.
```

### 1.5 Create a KIND cluster

During this workshop we will work with a cluster with multiple Cassandra nodes. As of now the Cassandra Operator needs a worker per node. We will create a Kubernetes cluster based on the [following configuration](01-kind-config.yaml) **1 master, 5 workers** to be able to spawn some multi nodes clusters. This is also the reason why `kind` has been preferred as `minikube` another solution to run Kubernetes locally but handling only a managin a single worker.

**✅ Step 1.5a. Execute the following command to create a kubernetes cluster**
```bash
kind create cluster --name kind-cassandra --config ./0-setup-your-cluster/01-kind-config.yaml
```

📗 **Expected output**
```
Creating cluster "kind-cassandra" ...
 ✓ Ensuring node image (kindest/node:v1.17.0) 🖼
 ✓ Preparing nodes 📦 📦 📦 📦  
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
 ✓ Joining worker nodes 🚜 
Set kubectl context to "kind-kind-cassandra"
You can now use your cluster with:
kubectl cluster-info --context kind-kind-cassandra
Have a question, bug, or feature request? Let us know! https://kind.sigs.k8s.io/#community 🙂
```

**✅ Step 1.5b. Execute the following command to visualize the list of your clusters**
```bash
# Show the new created cluster
kind get clusters
```
📗 **Expected output**
```
kind-cassandra
```

**✅ Step 1.5c. Execute the following command to link `kubectl` with our new cluster**
```bash
kubectl cluster-info --context kind-kind-cassandra
```
📗 **Expected output**
```
Kubernetes master is running at https://127.0.0.1:45451
KubeDNS is running at https://127.0.0.1:45451/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

**✅ Step 1.5d. Execute the following command to list nodes in k8s cluster**
```bash
kubectl get nodes
```
📗 **Expected output**
```
NAME                           STATUS   ROLES    AGE    VERSION
kind-cassandra-control-plane   Ready    master   2m4s   v1.17.0
kind-cassandra-worker          Ready    <none>   86s    v1.17.0
kind-cassandra-worker2         Ready    <none>   88s    v1.17.0
kind-cassandra-worker3         Ready    <none>   88s    v1.17.0
kind-cassandra-worker4         Ready    <none>   88s    v1.17.0
kind-cassandra-worker5         Ready    <none>   88s    v1.17.0
```

## 1.6 Create namespace and storageClass

Cass-operator is built to watch over pods running Casandra or DSE in a Kubernetes namespace. We need to create a namespace for the cluster. For the rest of this guide, we will be using the namespace `cass-operator`. You can pick any name you like but you would have to change the commands accordingly.

**✅ Step 1.6a. Execute the following command to create the namespace**
```bash
kubectl create ns cass-operator
```
📗 **Expected output**
```
namespace/cass-operator created
```

Kubernetes uses the `StorageClass` resource as an abstraction layer between pods needing persistent storage and the storage resources that a specific Kubernetes cluster can provide. We recommend using the fastest type of networked storage available. Let's create one for your environment.

**✅ Step 1.6b. Execute the following command to list existing storageClass**
```bash
kubectl get storageclass
```
📗 **Expected output**
```bash
NAME                 PROVISIONER             RECLAIMPOLICY   VOLUMEBINDINGMODE      ALLOWVOLUMEEXPANSION   AGE
standard (default)   rancher.io/local-path   Delete          WaitForFirstConsumer   false                  11m
```

**✅ Step 1.6c. Execute the following command describe `default` storageClass**
```bash
kubectl describe storageclass standard
```
📗 **Expected output** (may vary)
```
Name:            standard
IsDefaultClass:  Yes
Annotations:     kubectl.kubernetes.io/last-applied-configuration={"apiVersion":"storage.k8s.io/v1","kind":"StorageClass","metadata":{"annotations":{"storageclass.kubernetes.io/is-default-class":"true"},"name":"standard"},"provisioner":"rancher.io/local-path","reclaimPolicy":"Delete","volumeBindingMode":"WaitForFirstConsumer"}
,storageclass.kubernetes.io/is-default-class=true
Provisioner:           rancher.io/local-path
Parameters:            <none>
AllowVolumeExpansion:  <unset>
MountOptions:          <none>
ReclaimPolicy:         Delete
VolumeBindingMode:     WaitForFirstConsumer
Events:                <none>
```

We are are interested in a few fields to intialize the `yaml` to create a storageClass. You can find more information in the [Reference Documentation](https://kubernetes.io/docs/concepts/storage/storage-classes/) and the [dedicated page](https://kubernetes.io/docs/tasks/administer-cluster/change-default-storage-class/#changing-the-default-storageclass) to edit those.

One thing thing to note here is `volumeBindingMode: WaitForFirstConsumer`. The default value is `Immediate` and should not be used. It can prevent Cassandra pods from being scheduled on a worker node. If a pod fails to run and its status reports a message like, had volume node affinity conflict, then check the `volumeBindingMode` of the `StorageClass` being used

This file should be adapted tor reflect your *Kubernetes* environment. We have several samples in the repository for [kind](02-storageclass-kind.yaml), [Minikube](03-storageclass-minikube.yaml) and [GKE](04-storageclass-gke.yaml).
```yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  annotations:
    storageclass.kubernetes.io/is-default-class: "true"
  name: server-storage
provisioner: rancher.io/local-path
reclaimPolicy: Delete
volumeBindingMode: WaitForFirstConsumer
```

**✅ Step 1.6d. Execute the following command to create a new `storageClass` named `server-storage`**
For the rest of this guide, we would assume you have defined a `StorageClass` and named it `server-storage`. 
```bash
kubectl -n cass-operator apply -f ./0-setup-your-cluster/02-storageclass-kind.yaml
```

**✅ Step 1.6e. Execute the following command to list the storageClass**
```
kubectl -n cass-operator get storageClass
```
📗 **Expected output**
```
NAME                       PROVISIONER             RECLAIMPOLICY   VOLUMEBINDINGMODE      ALLOWVOLUMEEXPANSION   AGE
server-storage (default)   rancher.io/local-path   Delete          WaitForFirstConsumer   false                  25m
standard (default)         rancher.io/local-path   Delete          WaitForFirstConsumer   false                  88m
```

## 2. Install Cass Operator

Cass Operator simplifies the process of deploying and managing Cassandra or DSE in a Kubernetes cluster.

Within this guide, we have joined together a few Kubernetes resources into a single YAML file needed to deploy the operator. This file defines the following:

- `ServiceAccount`, `Role`, and `RoleBinding`to describe a user and set of permissions necessary to run the operator. In demo environments that don't have role-based access-control enabled, these extra steps are unnecessary but are harmless.
- `CustomResourceDefinition` for the `CassandraDatacenter` resources used to configure clusters managed by the `cass-operator`.
- Deployment to start the operator in a state where it waits and watches for CassandraDatacenter resources.

Generally, `cluster-admin` privileges are required to register a CustomResourceDefinition (CRD). All privileges needed by the operator are present within the operator-manifests YAML. Note the operator does not require cluster-admin privileges, only the user defining the CRD requires those permissions.

**✅ Step 2a. Apply the manifest**
Apply the manifest above, and wait for the deployment to become ready. You can watch the progress by getting the list of pods for the namespace, as demonstrated below:
```bash
kubectl -n cass-operator apply -f ./1-cassandra/11-install-cass-operator-v1.1.yaml
```
📗 **Expected output**
```bash
namespace/cass-operator configured
serviceaccount/cass-operator created
secret/cass-operator-webhook-config created
customresourcedefinition.apiextensions.k8s.io/cassandradatacenters.cassandra.datastax.com created
clusterrole.rbac.authorization.k8s.io/cass-operator-cluster-role created
clusterrolebinding.rbac.authorization.k8s.io/cass-operator created
role.rbac.authorization.k8s.io/cass-operator created
rolebinding.rbac.authorization.k8s.io/cass-operator created
service/cassandradatacenter-webhook-service created
deployment.apps/cass-operator created
validatingwebhookconfiguration.admissionregistration.k8s.io/cassandradatacenter-webhook-registration created
```

**✅ Step 2b. Execute this command to Wait for the pod to be ready**
*`wath` is here optional but allows  auto-refresh of the command*
```bash
watch kubectl -n cass-operator get pod
```
📗 **Expected output**
```bash
NAME                             READY   STATUS    RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running   0          51s
```

In the output we can see a generated id here `657cb5c695-q9psl` it will be different for you, as such change the command lines accordingly to match your id.

**✅ Step 2c. Execute this command to describe the CRD**
```bash
kubectl -n cass-operator describe pods cass-operator-YOURID
```
📗 **Expected output**
```
Name:         cass-operator-657cb5c695-q9psl
Namespace:    cass-operator
Priority:     0
Node:         kind-cassandra-worker/172.17.0.4
Start Time:   Fri, 24 Apr 2020 13:39:46 +0200
Labels:       name=cass-operator
              pod-template-hash=657cb5c695
Annotations:  <none>
Status:       Running
IP:           10.244.3.2
IPs:
  IP:           10.244.3.2
Controlled By:  ReplicaSet/cass-operator-657cb5c695
Containers:
  cass-operator:
    Container ID:   containerd://46b30d1b4d98df144e5781d0f3507755a4388d04b90c301a3c5b654e77759108
    Image:          datastax/cass-operator:1.1.0
    Image ID:       docker.io/datastax/cass-operator@sha256:26d64588b4e626dcd74c2a6412dff1cfad42c80ad674f67c57f098d5cd20e2dd
    Port:           <none>
    Host Port:      <none>
    State:          Running
      Started:      Fri, 24 Apr 2020 13:39:51 +0200
    Ready:          True
    Restart Count:  0
    Liveness:       exec [pgrep .*operator] delay=5s timeout=5s period=5s #success=1 #failure=3
    Readiness:      exec [stat /tmp/operator-sdk-ready] delay=5s timeout=5s period=5s #success=1 #failure=1
    Environment:
      WATCH_NAMESPACE:          cass-operator (v1:metadata.namespace)
      POD_NAME:                 cass-operator-657cb5c695-q9psl (v1:metadata.name)
      OPERATOR_NAME:            cass-operator
      SKIP_VALIDATING_WEBHOOK:  FALSE
    Mounts:
      /tmp/k8s-webhook-server/serving-certs from cass-operator-certs-volume (rw)
      /var/run/secrets/kubernetes.io/serviceaccount from cass-operator-token-j2wbf (ro)
Conditions:
  Type              Status
  Initialized       True 
  Ready             True 
  ContainersReady   True 
  PodScheduled      True 
Volumes:
  cass-operator-certs-volume:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  cass-operator-webhook-config
    Optional:    false
  cass-operator-token-j2wbf:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  cass-operator-token-j2wbf
    Optional:    false
QoS Class:       BestEffort
Node-Selectors:  <none>
Tolerations:     node.kubernetes.io/not-ready:NoExecute for 300s
                 node.kubernetes.io/unreachable:NoExecute for 300s
Events:
  Type    Reason     Age    From                            Message
  ----    ------     ----   ----                            -------
  Normal  Scheduled  2m12s  default-scheduler               Successfully assigned cass-operator/cass-operator-657cb5c695-q9psl to kind-cassandra-worker
  Normal  Pulling    2m12s  kubelet, kind-cassandra-worker  Pulling image "datastax/cass-operator:1.1.0"
  Normal  Pulled     2m7s   kubelet, kind-cassandra-worker  Successfully pulled image "datastax/cass-operator:1.1.0"
  Normal  Created    2m7s   kubelet, kind-cassandra-worker  Created container cass-operator
  Normal  Started    2m7s   kubelet, kind-cassandra-worker  Started container cass-operator
```

The previous section created a new resource type in your Kubernetes cluster, the `CassandraDatacenter`. By adding `CassandraDatacenter` resources to your namespace, you can define a cluster topology for the operator to create and monitor.

*Optionally* You can execute few commands to see what has also been created under the hood:
```bash
# List created services (2 expected metrics and webhook-service)
kubectl -n cass-operator get svc

# List of created Secrets (3 expected, 2 for operator)
kubectl -n cass-operator get secret

# Expected an empty list but no error the CRD cassandradatacenter now exist
kubectl -n cass-operator get cassandradatacenter

# Expected output 1 line
kubectl -n cass-operator get configMap
```

## 3. Create a single node cluster

Apply this file via `kubectl` and watch the list of pods as the operator deploys them. Completing a deployment may take several minutes per node.

**✅ Step 3a. Create the cluster**
```bash
kubectl -n cass-operator apply -f ./1-cassandra/12-cassandra-cluster-1nodes.yaml
```
**✅ Step 3b. Watch progression**
```bash
watch kubectl -n cass-operator get pod
```

📗 **Expected output**
```
NAME                             READY   STATUS    RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running   0          5m22s
cluster1-dc1-default-sts-0       1/2     Running   0          50s
```

*Optionnally* you can also track the datacenter creation (not only) the pod with the following command in another shell. Note the `-w` flag allowing to partially mimic the `watch` mechanism.
```
kubectl -n cass-operator get -w cassdc -o yaml
```

**✅ Step 3c. Execute the command to describe the datacenter**
```bash
kubectl -n cass-operator describe cassdc dc1
```
*Note than you can always use `cassdc` instead of the longer version `cassandradatacenter` in the command lines. In the following we may use one or the other.*

The last part of the Yaml, the Events is what we are looking for, it tells us a lot about the creation process.

📗 **Expected output**
```
Name:         dc1
Namespace:    cass-operator
Labels:       <none>
Annotations:  kubectl.kubernetes.io/last-applied-configuration:
                {"apiVersion":"cassandra.datastax.com/v1beta1","kind":"CassandraDatacenter","metadata":{"annotations":{},"name":"dc1","namespace":"cass-op...
API Version:  cassandra.datastax.com/v1beta1
Kind:         CassandraDatacenter
Metadata:
  Creation Timestamp:  2020-04-24T11:44:18Z
  Finalizers:
    finalizer.cassandra.datastax.com
  Generation:        2
  Resource Version:  2364
  Self Link:         /apis/cassandra.datastax.com/v1beta1/namespaces/cass-operator/cassandradatacenters/dc1
  UID:               70906f13-6e18-4d32-ba61-a53cb89570c1
Spec:
  Cluster Name:  cluster1
  Config:
    Cassandra - Yaml:
      Authenticator:  org.apache.cassandra.auth.PasswordAuthenticator
      Authorizer:     org.apache.cassandra.auth.CassandraAuthorizer
      role_manager:   org.apache.cassandra.auth.CassandraRoleManager
    Jvm - Options:
      initial_heap_size:  800M
      max_heap_size:      800M
  Management API Auth:
    Insecure:
  Resources:
  Server Type:     cassandra
  Server Version:  3.11.6
  Size:            1
  Storage Config:
    Cassandra Data Volume Claim Spec:
      Access Modes:
        ReadWriteOnce
      Resources:
        Requests:
          Storage:         5Gi
      Storage Class Name:  server-storage
Status:
  Cassandra Operator Progress:  Ready
  Last Rolling Restart:         2020-04-24T11:44:18Z
  Last Server Node Started:     2020-04-24T11:45:06Z
  Node Statuses:
    cluster1-dc1-default-sts-0:
  Super User Upserted:  2020-04-24T11:45:28Z
Events:
  Type     Reason             Age                    From           Message
  ----     ------             ----                   ----           -------
  Normal   CreatedResource    2m27s                  cass-operator  Created service cluster1-dc1-service
  Normal   CreatedResource    2m27s                  cass-operator  Created service cluster1-seed-service
  Normal   CreatedResource    2m27s                  cass-operator  Created service cluster1-dc1-all-pods-service
  Normal   CreatedResource    2m27s                  cass-operator  Created statefulset cluster1-dc1-default-sts
  Normal   ScalingUpRack      2m27s (x2 over 2m27s)  cass-operator  Scaling up rack default
  Normal   LabeledPodAsSeed   105s                   cass-operator  Labeled pod a seed node cluster1-dc1-default-sts-0
  Normal   StartingCassandra  100s                   cass-operator  Starting Cassandra for pod cluster1-dc1-default-sts-0
  Normal   StartedCassandra   77s                    cass-operator  Started Cassandra for pod cluster1-dc1-default-sts-0
  Normal   CreatedResource    77s                    cass-operator  Created PodDisruptionBudget dc1-pdb
  Normal   CreatedSuperuser   77s                    cass-operator  Created superuser
  Warning  ReconcileFailed    75s                    cass-operator  Post http://cluster1-dc1-default-sts-0.cluster1-dc1-service.cass-operator:8080/api/v0/ops/seeds/reload: dial tcp: lookup cluster1-dc1-default-sts-0.cluster1-dc1-service.cass-operator on 10.96.0.10:53: no such host
```

**Cluster and DataCenter:**

A logical `datacenter` is the primary resource managed by the cass-operator. Within a single Kubernetes namespace:

- A single `CassandraDatacenter `resource defines a single-datacenter cluster.
- Two or more CassandraDatacenter resources with different clusterName's define separate and unrelated single-datacenter clusters. Note the operator manages both clusters since they reside within the same Kubernetes namespace.
- Two or more CassandraDatacenter resources that have the same clusterName define a multi-datacenter cluster. The operator will join the instances in each datacenter into a logical topology that acts as a single cluster.

For this guide, we define a single-datacenter cluster. The cluster is named cluster1 with the datacenter named dc1.

**Racks**

Cassandra is rack-aware, and the racks parameter will configure the operator to set up pods in a rack aware way. Note the Kubernetes worker nodes must have labels matching `failure-domain.beta.kubernetes.io/zone`. Racks must have identifiers. In this guide we will use `r1`, `r2`, and `r3`.

**NodeCount**

The size parameter is the number of nodes to run in the datacenter. For optimal performance, it's recommended to run only one server instance per Kubernetes worker node. The operator will enforce that limit, and pods may get stuck in the Pending status if there are insufficient Kubernetes workers available.

*Optionally* To see what resources have been created in k8s you can run the following commands.
```
# List datacenters (dc1)
kubectl -n cass-operator get cassandradatacenter
# List statefulset (cluster1-dc1-sts)
kubectl -n cass-operator get sts
# List Services (3 services per dc all-pod, seed,and global)
kubectl -n cass-operator get svc
# List pods (our new Cassandra node)
kubectl -n cass-operator get pods
# List persistent volumes 1 per POD (server-data)
kubectl -n cass-operator get persistentVolumes
```

## 4. Browse your cluster

By default, a cassandra superuser gets created by the operator. A Kubernetes secret will be created for it, named `<cluserName>-superuser`. It will contain username and password keys.

**✅ Step 4a. Execute the command to list secret  container user and password**
```bash
kubectl -n cass-operator get secret cluster1-superuser -o yaml
```
📗 **Expected output**
```yaml
apiVersion: v1
data:
  password: aDVpTUhtOFJwb09mU1hVWE5pR1QtV1BmSmE3dGJ3VjlwbGo0SVRZd0h5Z2h4SmxRTzJ5U1VR
  username: Y2x1c3RlcjEtc3VwZXJ1c2Vy
kind: Secret
metadata:
  creationTimestamp: "2020-04-24T11:44:18Z"
  name: cluster1-superuser
  namespace: cass-operator
  resourceVersion: "2064"
  selfLink: /api/v1/namespaces/cass-operator/secrets/cluster1-superuser
  uid: 7d74a9a8-33da-44cb-a235-d990c494a6d5
type: Opaque
```

Spot the lines
```bash
  password: aDVpTUhtOFJwb09mU1hVWE5pR1QtV1BmSmE3dGJ3VjlwbGo0SVRZd0h5Z2h4SmxRTzJ5U1VR
  username: Y2x1c3RlcjEtc3VwZXJ1c2Vy
```  

The password will be different for everyone (!). Please adapt with the following command lines to match your values.

**✅ Step 4b. Execute the command to decode username**
```bash
echo Y2x1c3RlcjEtc3VwZXJ1c2Vy | base64 -d && echo ""
```
📗 **Expected output**
```bash
cluster1-superuser
```
Not a surprise right ? Do the same for the password

**✅ Step 4c. Execute the command to decode password**
```bash
echo aDVpTUhtOFJwb09mU1hVWE5pR1QtV1BmSmE3dGJ3VjlwbGo0SVRZd0h5Z2h4SmxRTzJ5U1VR | base64 -d && echo ""
```
📗 **Sample output**
```bash
-WPfJa7tbwV9plj4ITYwHyghxJlQO2ySUQ
```

**✅ Step 4d. Execute the command to login to a pod with SSH**
We are using the pod name `cluster1-dc1-default-sts-0` listed with `get pods` command, make sure the pod name also exist for you.
```bash
kubectl -n cass-operator exec -it cluster1-dc1-default-sts-0  -- /bin/bash
```
📗 **Sample output**
```bash
Defaulting container name to cassandra.
Use 'kubectl describe pod/cluster1-dc1-default-sts-0 -n cass-operator' to see all of the containers in this pod.
root@cluster1-dc1-default-sts-0:/#
```

**✅ Step 4e. Display status of your cluster with `nodetool`**
```bash
root@cluster1-dc1-default-sts-0:/# nodetool status
```
📗 **Sample output**
```
Datacenter: dc1
===============
Status=Up/Down
|/ State=Normal/Leaving/Joining/Moving
--  Address     Load       Owns (effective)  Host ID                               Token                                    Rack
UN  10.244.2.3  65.3 KiB   100.0%            ae2fe508-dde0-43c2-ad14-42973e6c9821  3273667123371405452                      default
```

We notice a working cluster with only 1 node. Let's dig into it. The operator makes a Kubernetes headless service available at `<clusterName>-<datacenterName>-service`. Any CQL client inside the Kubernetes cluster should be able to connect to `cluster1-dc1-service.cass-operator` and use the nodes in a round-robin fashion as contact points.

**✅ Step 4f. Start a `cqlsh` console**
```bash
cqlsh cluster1-dc1-service.cass-operator -u cluster1-superuser -p h5iMHm8RpoOfSXUXNiGT-WPfJa7tbwV9plj4ITYwHyghxJlQO2ySUQ
```

📗 **Sample output**
```bash
Connected to cluster1 at cluster1-dc1-service.cass-operator:9042.
[cqlsh 5.0.1 | Cassandra 3.11.6 | CQL spec 3.4.4 | Native protocol v4]
Use HELP for help.
cluster1-superuser@cqlsh> 
```

**✅ Step 4g. Create a keyspace `killrvideo1` with a CQL command**
```bash
cluster1-superuser@cqlsh> CREATE KEYSPACE IF NOT EXISTS killrvideo1 WITH replication = {'class': 'NetworkTopologyStrategy', 'dc1': '1'} AND durable_writes = true;
```

**✅ Step 4h. Describe keyspaces with a CQL command**
```bash
cluster1-superuser@cqlsh> describe keyspaces;
```
📗 **Sample output**
```bash
system_schema  system              system_traces
system_auth    system_distributed  killrvideo1 
```

**✅ Step 4i. Create tables and insert some data**
```bash
cluster1-superuser@cqlsh> use killrvideo1;
cluster1-superuser@cqlsh:killrvideo1>
```
```sql
CREATE TABLE IF NOT EXISTS users (
    userid     uuid,
    firstname  text,
    lastname   text,
    email      text,
    created_date timestamp,
    PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS videos (
    videoid                uuid,
    userid                 uuid,
    name                   text,
    description            text,
    location               text,
    location_type          int,
    preview_image_location text,
    tags                   set<text>,
    added_date             timestamp,
    PRIMARY KEY (videoid)
);

INSERT INTO killrvideo1.videos (videoid, name, description, added_date)
  VALUES(99999999-1111-1111-1111-111111111111, 'Best of Jeff Carpenter', 'A highlights reel of some of Jeff''s greatest vlogs', toTimestamp(now()));
INSERT INTO killrvideo1.videos (videoid, name, description, added_date)
  VALUES(99999999-2222-2222-2222-222222222222, 'David''s Bloopers', 'A collection of David''s missteps and fleet-a-foot recoveries', toTimestamp(now()));
INSERT INTO killrvideo1.videos (videoid, name, description, added_date)
  VALUES(99999999-3333-3333-3333-333333333333, 'Cedrick''s Out-takes', 'It may sound like he''s cursing, but it''s just that he''s speaking French',  toTimestamp(now()));

INSERT INTO killrvideo1.users (userid, created_date, firstname, lastname, email)
  VALUES(11111111-1111-1111-1111-111111111111, toTimestamp(now()), 'Jeffrey', 'Carpenter', 'jc@datastax.com');
//INSERT INTO killrvideo.user_credentials (userid, email, password)
//  VALUES(11111111-1111-1111-1111-111111111111, 'jc@datastax.com', 'J3ffL0v3$C@ss@ndr@');
  
INSERT INTO killrvideo1.users (userid, created_date, firstname, lastname, email)
  VALUES(22222222-2222-2222-2222-222222222222, toTimestamp(now()), 'Eric', 'Zietlow', 'ez@datastax.com');
//INSERT INTO killrvideo.user_credentials (userid, email, password)
//  VALUES(22222222-2222-2222-2222-222222222222, 'ez@datastax.com', 'C@ss@ndr@R0ck$');

INSERT INTO killrvideo1.users (userid, created_date, firstname, lastname, email)
  VALUES(33333333-3333-3333-3333-333333333333, toTimestamp(now()), 'Cedrick', 'Lunven', 'cl@datastax.com');
//INSERT INTO killrvideo.user_credentials (userid, email, password)
//  VALUES(33333333-3333-3333-3333-333333333333, 'cl@datastax.com', 'Fr@nc3L0v3$C@ss@ndr@');
```

**✅ Step 4j. Show the `users` tables**
```bash
cluster1-superuser@cqlsh:killrvideo1> select * from users;
```
📗 **Sample output**
```bash
 userid                               | created_date                    | email           | firstname | lastname
--------------------------------------+---------------------------------+-----------------+-----------+-----------
 22222222-2222-2222-2222-222222222222 | 2020-04-24 11:57:08.858000+0000 | ez@datastax.com |      Eric |   Zietlow
 11111111-1111-1111-1111-111111111111 | 2020-04-24 11:57:08.840000+0000 | jc@datastax.com |   Jeffrey | Carpenter
 33333333-3333-3333-3333-333333333333 | 2020-04-24 11:57:08.925000+0000 | cl@datastax.com |   Cedrick |    Lunven

(3 rows)
cluster1-superuser@cqlsh:killrvideo1> select * from videos;

 videoid                              | added_date                      | description                                                             | location | location_type | name                   | preview_image_location | tags | userid
--------------------------------------+---------------------------------+-------------------------------------------------------------------------+----------+---------------+------------------------+------------------------+------+--------
 99999999-1111-1111-1111-111111111111 | 2020-04-24 11:57:08.782000+0000 |                      A highlights reel of some of Jeff's greatest vlogs |     null |          null | Best of Jeff Carpenter |                   null | null |   null
 99999999-2222-2222-2222-222222222222 | 2020-04-24 11:57:08.797000+0000 |            A collection of David's missteps and fleet-a-foot recoveries |     null |          null |       David's Bloopers |                   null | null |   null
 99999999-3333-3333-3333-333333333333 | 2020-04-24 11:57:08.828000+0000 | It may sound like he's cursing, but it's just that he's speaking French |     null |          null |    Cedrick's Out-takes |                   null | null |   null

(3 rows)
cluster1-superuser@cqlsh:killrvideo1> 
```

**✅ Step 4k. Exit both `cqlsh` and the pod to go back to kubectl**
```bash
exit
exit
```

## 5 - Scale up our cluster

In the following chapter we will expand our cluster from 1 node to 3 nodes.

**✅ Step 5a. Compare files `12-cassandra-cluster-1nodes` and  `13-cassandra-cluster-3nodes`**
```bash
diff ./1-cassandra/12-cassandra-cluster-1nodes.yaml ./1-cassandra/13-cassandra-cluster-3nodes.yaml
```
📗 **Expected output**
```bash
13c13
<   size: 1
---
>   size: 3
```

**✅ Step 5b. Execute the following command to Apply the new configuration**
```bash
kubectl -n cass-operator apply -f ./1-cassandra/13-cassandra-cluster-3nodes.yaml
```

**✅ Step 5c. Execute the following command to watch evolution of the cluster**
We may need about 10 minutes to have all pods both started and ready.
```bash
watch kubectl -n cass-operator get pod
```

📗 **Sample output**
```
NAME                             READY   STATUS             RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running            0          23m
cluster1-dc1-default-sts-0       2/2     Running            0          18m
cluster1-dc1-default-sts-1       0/2     ImagePullBackOff   0          84s
cluster1-dc1-default-sts-2       0/2     ImagePullBackOff   0          84s
---

NAME                             READY   STATUS             RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running            0          25m
cluster1-dc1-default-sts-0       2/2     Running            0          20m
cluster1-dc1-default-sts-1       1/2     ImagePullBackOff   0          3m20s
cluster1-dc1-default-sts-2       1/2     Running            0          3m20s
---

NAME                             READY   STATUS    RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running   0          29m
cluster1-dc1-default-sts-0       2/2     Running   0          25m
cluster1-dc1-default-sts-1       2/2     Running   0          8m11s
cluster1-dc1-default-sts-2       1/2     Running   0          8m11s
---

NAME                             READY   STATUS    RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running   0          34m
cluster1-dc1-default-sts-0       2/2     Running   0          30m
cluster1-dc1-default-sts-1       2/2     Running   0          13m
cluster1-dc1-default-sts-2       2/2     Running   0          13m
---
```

As before you can also optionally see the evolution of the `cassandradatacenter` with the following describe command `kubectl -n cass-operator get -w cassdc dc1 -o yaml`

It is a best practice to change the `yaml` file and then applying the new file. It allows to keep manifest and cluster in sync. Professional would notice we could also have enter the command `kubectl -n cass-operator patch cassandradatacenter/dc1 --patch '{"spec":{"size":3}}' --type merge`

*Optionally* To see what resources have been created in k8s you can run the following commands.
```bash
# List datacenters (dnochange)
kubectl -n cass-operator get cassandradatacenter
# List statefulset (no change)
kubectl -n cass-operator get sts
# List Services (no changel)
kubectl -n cass-operator get svc
# List pods (our new Cassandra node)
kubectl -n cass-operator get pods
# List persistent volumes (1 per POD (server-data)
kubectl -n cass-operator get persistentVolumes
```
*Optionally* To see what is inside a pod (to show the side car) you can run the following
```bash
kubectl -n cass-operator get pod cluster1-dc1-default-sts-1 -o yaml
```


**✅ Step 5d. Execute the command to login to a pod with SSH**
```bash
kubectl -n cass-operator exec -it cluster1-dc1-default-sts-0  -- /bin/bash
```

**✅ Step 5e. Display status of the cluster**
```bash
root@cluster1-dc1-default-sts-0:/# nodetool status
```
📗 **Sample output**
```
Datacenter: dc1
===============
Status=Up/Down
|/ State=Normal/Leaving/Joining/Moving
--  Address     Load       Tokens       Owns (effective)  Host ID                               Rack
UN  10.244.5.4  90.77 KiB  1            5.6%              5ad18392-14cd-47bb-a2a9-d56af9eb56d1  default
UN  10.244.1.3  91.34 KiB  1            76.9%             ee712d14-401d-4db6-b7c7-126197f5fe78  default
UN  10.244.2.3  114.89 KiB  1            17.4%             ae2fe508-dde0-43c2-ad14-42973e6c9821  default
```

**✅ Step 5f. Execute the command to login ton `cqlsh`**
```bash
cqlsh cluster1-dc1-service.cass-operator -u cluster1-superuser -p h5iMHm8RpoOfSXUXNiGT-WPfJa7tbwV9plj4ITYwHyghxJlQO2ySUQ
```
📗 **Sample output**
```bash
Connected to cluster1 at cluster1-dc1-service.cass-operator:9042.
[cqlsh 5.0.1 | Cassandra 3.11.6 | CQL spec 3.4.4 | Native protocol v4]
Use HELP for help.
cluster1-superuser@cqlsh> 
```

**✅ Step 5g. Describe the keyspace `killvideo1`**
```sql
describe keyspace killrvideo1;
```
📗 **Sample output**
```sql
CREATE KEYSPACE killrvideo1 WITH replication = {'class': 'NetworkTopologyStrategy', 'dc1': '1'}  AND durable_writes = true;

CREATE TABLE killrvideo1.users (
    userid uuid PRIMARY KEY,
    created_date timestamp,
    email text,
    firstname text,
    lastname text
) WITH bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';

CREATE TABLE killrvideo1.videos (
    videoid uuid PRIMARY KEY,
    added_date timestamp,
    description text,
    location text,
    location_type int,
    name text,
    preview_image_location text,
    tags set<text>,
    userid uuid
) WITH bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';
```

**✅ Step 5h. Change the deplication factor of `killvideo1`**
```sql
alter keyspace killrvideo1 with replication={'class':'NetworkTopologyStrategy', 'dc1':3};
```

**✅ Step 5i. Describe the keyspace `killvideo1`**
```sql
describe keyspace killrvideo1
```
📗 **Sample output**
```sql
CREATE KEYSPACE killrvideo1 WITH replication = {'class': 'NetworkTopologyStrategy', 'dc1': '3'}  AND durable_writes = true;
// [...]
```

**✅ Step 5j. Set the consistency level to `QUORUM`**
```bash
cluster1-superuser@cqlsh> CONSISTENCY QUORUM;
```
📗 **Sample output**
```
Consistency level set to QUORUM.
```

**✅ Step 5k. Enable tracing in CQLSH console**
```bash
cluster1-superuser@cqlsh> tracing on;
```
📗 **Sample output**
```
Now Tracing is enabled
```

**✅ Step 5l. Show a record from the table `videos`**
```sql
select * from killrvideo1.videos WHERE videoid=99999999-3333-3333-3333-333333333333;
```
📗 **Sample output**
```bash
 videoid                              | added_date                      | description                                                             | location | location_type | name                | preview_image_location | tags | userid
--------------------------------------+---------------------------------+-------------------------------------------------------------------------+----------+---------------+---------------------+------------------------+------+--------
 99999999-3333-3333-3333-333333333333 | 2020-04-24 11:57:08.828000+0000 | It may sound like he's cursing, but it's just that he's speaking French |     null |          null | Cedrick's Out-takes |                   null | null |   null

(1 rows)

Tracing session: b563e540-862b-11ea-8a55-95adacc89ecc

 activity                                                                                                                                                                                                                                                         | timestamp                  | source     | source_elapsed | client
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+----------------------------+------------+----------------+------------
execute CQL3 query | 2020-04-24 13:01:27.318000 | 10.244.5.4 |              0 | 10.244.2.3
parsing select * from killrvideo1.videos WHERE videoid=99999999-3333-3333-3333-333333333333; [Native-Transport-Requests-1] | 2020-04-24 13:01:27.326000 | 10.244.5.4 |           8157 | 10.244.2.3
reparing statement [Native-Transport-Requests-1] | 2020-04-24 13:01:27.327000 | 10.244.5.4 |           8738 | 10.244.2.3
reading data from /10.244.1.3 [Nat
```

**✅ Step 5m. Exit both `cqlsh` and the pod to go back to kubectl**
```bash
exit
exit
```

## 6 - Change the Cassandra Configuration

**✅ Step 6a. Use diff to show new config**
```bash
diff ./1-cassandra/13-cassandra-cluster-3nodes.yaml ./1-cassandra/14-cassandra-cluster-3nodes-newconfig.yaml 
```
📗 **Sample output**
```
26a27
>       commitlog_sync_period_in_ms: 11000
```

**✅ Step 6b. Apply the configuration**
```bash
kubectl -n cass-operator apply -f ./1-cassandra/14-cassandra-cluster-3nodes-newconfig.yaml 
```

**✅ Step 6c. Watch the rolling update of the operator**
```bash
watch kubectl -n cass-operator get pod
```
📗 **Sample output**
```
NAME                             READY   STATUS        RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running       0          92m
cluster1-dc1-default-sts-0       2/2     Running       0          88m
cluster1-dc1-default-sts-1       2/2     Running       0          71m
cluster1-dc1-default-sts-2       2/2     Terminating   0          71m
---

NAME                             READY   STATUS        RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running       0          94m
cluster1-dc1-default-sts-0       2/2     Running       0          89m
cluster1-dc1-default-sts-1       2/2     Terminating   0          72m
cluster1-dc1-default-sts-2       2/2     Running       0          56s
---

NAME                             READY   STATUS        RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running       0          95m
cluster1-dc1-default-sts-0       2/2     Terminating   0          91m
cluster1-dc1-default-sts-1       2/2     Running       0          42s
cluster1-dc1-default-sts-2       2/2     Running       0          2m2s
---

NAME                             READY   STATUS     RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running    0          96m
cluster1-dc1-default-sts-0       0/2     Init:0/1   0          3s
cluster1-dc1-default-sts-1       2/2     Running    0          71s
cluster1-dc1-default-sts-2       2/2     Running    0          2m31s
---

NAME                             READY   STATUS     RESTARTS   AGE
cass-operator-657cb5c695-q9psl   1/1     Running    0          96m
cluster1-dc1-default-sts-0       0/2     Init:0/1   0          3s
cluster1-dc1-default-sts-1       2/2     Running    0          71s
cluster1-dc1-default-sts-2       2/2     Running    0          2m31s
---
```

**✅ Step 6d. Describe the datacenter resource**
```bash
kubectl -n cass-operator describe pods cluster1-dc1-default-sts-0
```
📗 **Sample output**
```
Name:         cluster1-dc1-default-sts-0
Namespace:    cass-operator
Priority:     0
Node:         kind-cassandra-worker3/172.17.0.3
Start Time:   Fri, 24 Apr 2020 15:15:44 +0200
Labels:       app.kubernetes.io/managed-by=cassandra-operator
              cassandra.datastax.com/cluster=cluster1
              cassandra.datastax.com/datacenter=dc1
              cassandra.datastax.com/node-state=Started
              cassandra.datastax.com/rack=default
              cassandra.datastax.com/seed-node=true
              controller-revision-hash=cluster1-dc1-default-sts-64ff4f6b8
              statefulset.kubernetes.io/pod-name=cluster1-dc1-default-sts-0
Annotations:  <none>
Status:       Running
IP:           10.244.2.4
IPs:
  IP:           10.244.2.4
Controlled By:  StatefulSet/cluster1-dc1-default-sts
Init Containers:
  server-config-init:
    Container ID:   containerd://80c51d096b3adb02fdb96592ba25b42a8d34edc5e1eaa369dab6f2e02a8ef0dd
    Image:          datastax/cass-config-builder:1.0.0
    Image ID:       docker.io/datastax/cass-config-builder@sha256:ea01e19959fefb4bc36c6c500dc37cb003bc6713065be93ab2c3e4180b298cf0
    Port:           <none>
    Host Port:      <none>
    State:          Terminated
      Reason:       Completed
      Exit Code:    0
      Started:      Fri, 24 Apr 2020 15:15:45 +0200
      Finished:     Fri, 24 Apr 2020 15:15:47 +0200
    Ready:          True
    Restart Count:  0
    Environment:
      CONFIG_FILE_DATA:  {"cassandra-yaml":{"authenticator":"org.apache.cassandra.auth.PasswordAuthenticator","authorizer":"org.apache.cassandra.auth.CassandraAuthorizer","commitlog_sync_period_in_ms":11000,"role_manager":"org.apache.cassandra.auth.CassandraRoleManager"},"cluster-info":{"name":"cluster1","seeds":"cluster1-seed-service"},"datacenter-info":{"name":"dc1"},"jvm-options":{"initial_heap_size":"800M","max_heap_size":"800M"}}
      POD_IP:             (v1:status.podIP)
      RACK_NAME:          (v1:metadata.labels['cassandra.datastax.com/rack'])
      PRODUCT_VERSION:   3.11.6
      PRODUCT_NAME:      cassandra
      DSE_VERSION:       3.11.6
    Mounts:
      /config from server-config (rw)
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-cg9cj (ro)
Containers:
  cassandra:
    Container ID:   containerd://de0fcd1a3b35985ddee4471e2e5032361067cd84838beb32bd83b2618c04bd6d
    Image:          datastax/cassandra-mgmtapi-3_11_6:v0.1.0
    Image ID:       docker.io/datastax/cassandra-mgmtapi-3_11_6@sha256:25611452cec30bc9bf8ac734f6258525e905612f2f5874c62023c4983f256dc1
    Ports:          9042/TCP, 8609/TCP, 7000/TCP, 7001/TCP, 8080/TCP
    Host Ports:     0/TCP, 0/TCP, 0/TCP, 0/TCP, 0/TCP
    State:          Running
      Started:      Fri, 24 Apr 2020 15:15:49 +0200
    Ready:          True
    Restart Count:  0
    Liveness:       http-get http://:8080/api/v0/probes/liveness delay=15s timeout=1s period=15s #success=1 #failure=3
    Readiness:      http-get http://:8080/api/v0/probes/readiness delay=20s timeout=1s period=10s #success=1 #failure=3
    Environment:
      DS_LICENSE:               accept
      DSE_AUTO_CONF_OFF:        all
      USE_MGMT_API:             true
      MGMT_API_EXPLICIT_START:  true
      DSE_MGMT_EXPLICIT_START:  true
    Mounts:
      /config from server-config (rw)
      /var/lib/cassandra from server-data (rw)
      /var/log/cassandra from server-logs (rw)
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-cg9cj (ro)
  server-system-logger:
    Container ID:  containerd://1b59895f64b67d4d565491782be9f54df90477a236cb8eb3324d1eb2aff201ed
    Image:         busybox
    Image ID:      docker.io/library/busybox@sha256:a8cf7ff6367c2afa2a90acd081b484cbded349a7076e7bdf37a05279f276bc12
    Port:          <none>
    Host Port:     <none>
    Args:
      /bin/sh
      -c
      tail -n+1 -F /var/log/cassandra/system.log
    State:          Running
      Started:      Fri, 24 Apr 2020 15:15:50 +0200
    Ready:          True
    Restart Count:  0
    Environment:    <none>
    Mounts:
      /var/log/cassandra from server-logs (rw)
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-cg9cj (ro)
Conditions:
  Type              Status
  Initialized       True 
  Ready             True 
  ContainersReady   True 
  PodScheduled      True 
Volumes:
  server-data:
    Type:       PersistentVolumeClaim (a reference to a PersistentVolumeClaim in the same namespace)
    ClaimName:  server-data-cluster1-dc1-default-sts-0
    ReadOnly:   false
  server-config:
    Type:       EmptyDir (a temporary directory that shares a pod's lifetime)
    Medium:     
    SizeLimit:  <unset>
  server-logs:
    Type:       EmptyDir (a temporary directory that shares a pod's lifetime)
    Medium:     
    SizeLimit:  <unset>
  default-token-cg9cj:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  default-token-cg9cj
    Optional:    false
QoS Class:       BestEffort
Node-Selectors:  <none>
Tolerations:     node.kubernetes.io/not-ready:NoExecute for 300s
                 node.kubernetes.io/unreachable:NoExecute for 300s
Events:
  Type     Reason     Age   From                             Message
  ----     ------     ----  ----                             -------
  Normal   Scheduled  70s   default-scheduler                Successfully assigned cass-operator/cluster1-dc1-default-sts-0 to kind-cassandra-worker3
  Normal   Pulled     69s   kubelet, kind-cassandra-worker3  Container image "datastax/cass-config-builder:1.0.0" already present on machine
  Normal   Created    69s   kubelet, kind-cassandra-worker3  Created container server-config-init
  Normal   Started    69s   kubelet, kind-cassandra-worker3  Started container server-config-init
  Normal   Pulled     66s   kubelet, kind-cassandra-worker3  Container image "datastax/cassandra-mgmtapi-3_11_6:v0.1.0" already present on machine
  Normal   Created    66s   kubelet, kind-cassandra-worker3  Created container cassandra
  Normal   Started    65s   kubelet, kind-cassandra-worker3  Started container cassandra
  Normal   Pulling    65s   kubelet, kind-cassandra-worker3  Pulling image "busybox"
  Normal   Pulled     64s   kubelet, kind-cassandra-worker3  Successfully pulled image "busybox"
  Normal   Created    64s   kubelet, kind-cassandra-worker3  Created container server-system-logger
  Normal   Started    64s   kubelet, kind-cassandra-worker3  Started container server-system-logger
  Warning  Unhealthy  37s   kubelet, kind-cassandra-worker3  Readiness probe failed: HTTP probe failed with statuscode: 500
```

📗 **Specially check the `cassandra-yaml` value**
```json
{
 "cassandra-yaml": {
    "authenticator":"org.apache.cassandra.auth.PasswordAuthenticator",
    "authorizer":"org.apache.cassandra.auth.CassandraAuthorizer",
    "commitlog_sync_period_in_ms":11000,
    "role_manager":"org.apache.cassandra.auth.CassandraRoleManager"
  },
 "cluster-info": {
    "name":"cluster1",
    "seeds":"cluster1-seed-service"
 },
 "datacenter-info":{
    "name":"dc1"
 },
 "jvm-options":{
   "initial_heap_size":"800M",
   "max_heap_size":"800M"
 }
}
```

[🏠 Back to Table of Content](#table-of-content)

[🏠Back to HOME workshop](https://github.com/DataStax-Academy/cassandra-workshop-series)

THE END.