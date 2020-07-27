# Install Linux Box

This document will give you steps use to setup the linuxBox available for this workshop

## Requirements

The following configuration is adviced

- CentOS RED HAT images with : 4 CPU, 16GB RAM (t2.xlarge) 
- Disk 30GB is enough

Security group or nginx as reverse proxy
- 8001 (k8s console), 22 (SSH), 8080 (ported needed webssh)
- 9090 (prometheus), 3000 (grafana)

## Initialization

Create the keypair.pem file, store locally, and setup limit access `chmod 700`

- **Connect**

```
ssh -i <my_keypari_file>.pem ec2-user@<dns>.us-east-2.compute.amazonaws.com
```

- **Update**

```
sudo yum update
```

## Installation

### Docker

- **Installation**

Steps from https://linoxide.com/linux-how-to/how-to-install-docker-on-centos/

```bash
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine

sudo yum install -y yum-utils

sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo

sudo dnf -y  install docker-ce --nobest

sudo systemctl enable --now docker

systemctl status  docker
```

- **Validation:**

```bash
sudo usermod -aG docker $USER
newgrp docker
docker images
docker run hello-world

docker -v
```

### Docker Compose

- **Installation:**

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose
```

- **Validation:**

```bash
docker-compose -v
```

### Kubectl

- **Installation:**

```bash
curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl

chmod +x kubectl

sudo mv ./kubectl /usr/local/bin/kubectl

kubectl version --client
```

- **Validation:**

```bash
kubectl version --client
```


### Kind

- **Installation:**

```bash
curl -Lo ./kind https://github.com/kubernetes-sigs/kind/releases/download/v0.7.0/kind-$(uname)-amd64
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind
```

- **Validation:**

```bash
kind get clusters
```

### Git

- **Installation:**

```bash
sudo yum install git
```

- **Validation and working document:**

```bash
git clone https://github.com/DataStax-Academy/kubernetes-workshop-online.git
cd kubernetes-workshop-online
```

### BASH

- **Override the prompt:**

```bash
export PS1="\[\033[31m\]\u\[\033[00m\]@\[\033[32m\]\h\[\033[00m\]:\[\033[34m\]\w\[\033[00m\]> "
```

### Web SSH

- **Installation:**

```
TODO
```



