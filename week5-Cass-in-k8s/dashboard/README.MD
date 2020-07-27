![OK](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/00-screenplay.png?raw=true)

# Browse Cass Operator in Dashboard UI

At this point we expect you to have successfully complete section #0 and section #1.

**WARNING**: `DUE TO LIMITATIONS IN THE KUBERNETES DASHBOARD UI THIS PART CANNOT BE EXECUTED IN THE CLOUD-BASES ENVIRONMENTS, THE TOKEN GENERATION SEEMS KO.`

## 1. Install Dahsboard UI

Dashboard is a web-based Kubernetes user interface. You can use Dashboard to deploy containerized applications to a Kubernetes cluster, troubleshoot your containerized application, and manage the cluster resources. You can use Dashboard to get an overview of applications running on your cluster, as well as for creating or modifying individual Kubernetes resources (such as Deployments, Jobs, DaemonSets, etc). For example, you can scale a Deployment, initiate a rolling update, restart a pod or deploy new applications using a deploy wizard. More information on the dashboard can be found in the [reference documentation](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/)

📘 **1a. Apply the manisfest to install the UI**
```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml
```
📗 **Expected output**
```
namespace/kubernetes-dashboard created
serviceaccount/kubernetes-dashboard created
service/kubernetes-dashboard created
secret/kubernetes-dashboard-certs created
secret/kubernetes-dashboard-csrf created
secret/kubernetes-dashboard-key-holder created
configmap/kubernetes-dashboard-settings created
role.rbac.authorization.k8s.io/kubernetes-dashboard created
clusterrole.rbac.authorization.k8s.io/kubernetes-dashboard created
rolebinding.rbac.authorization.k8s.io/kubernetes-dashboard created
clusterrolebinding.rbac.authorization.k8s.io/kubernetes-dashboard created
deployment.apps/kubernetes-dashboard created
service/dashboard-metrics-scraper created
deployment.apps/dashboard-metrics-scraper created
```

📘 **1b.Execute the commands to create both account and roles**

```bash
kubectl apply -f ./2-dashboard/22-create-dashboard-account.yaml
```

```bash
kubectl apply -f ./2-dashboard/23-create-dashboard-role.yaml
```

📘 **1c.Generate a token**

To login to the dashboard credentials are required. This could be either a login/password or a token. We will here use onlu token.

```bash
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
```

📘 **1d.Start the UI through the usage of proxy**
```bash
kubectl proxy
```
You can now open a browser and navigate to `127.0.0.1:8001` Note we may have rewrite the URL in cloud based environments to cope with port opening policies
```
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login
```

Copy/Paste the generated token to login you can now browse the UI

## 2. Browse Dahsboard UI


📘 **2a.Welcome page (namespace `default`)**

![OK](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/21-ui-default.png?raw=true)

📘 **2b. Our namespace `cass-operator` (so cool)**

![OK](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/22-ui-home.png?raw=true)

📘 **2c. Navigate in the pods**

![OK](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/23-ui-pods.png?raw=true)

📘 **2d. Navigate in the replicat set**

![OK](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/24-ui-replicatsets.png?raw=true)

You can now kill the `kubectl proxy` and delete the resources associated to gain some resources for later..... we might need it, time to get serious.

📘 **2e. Navigate in the replicat set**
```bash
kubectl delete -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml
```

## Congratulations, your are done for this exercise.

![OK](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/welldone.jpg?raw=true)

