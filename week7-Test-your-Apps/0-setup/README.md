
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨Performance Benchmark your Data Model✨

[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

# Initial Setup Using Katacoda (**recommended**)
If you wish to use our Katacoda scenarios then go here -> https://katacoda.com/datastax/courses/nosqlbench-intro

The Katacoda scenarios will bring you through a step-by-step experience in environments you can easily restart and play around with. It does require a sign-up, but is completely **FREE** to use.

![Please do NOT run Katakoda scenarios during the workshop.](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/workshop-week7-do-not-run-katakoda.png)

# Initial Setup On Your Local Machine
For those of you who would like to have a basic setup working on your own machine follow the steps below. **If you are using Katacoda you DO NOT NEED THESE STEPS**. All scenarios will play out within Katacoda.

## Install Docker and Download NOSQLBench
**NOTE:** Although Docker is **NOT** a pre-requisite for NoSQLBench, it is used in the following instructions to quickly spin up an Apache Cassandra™ cluster for use with NoSQLBench in our scenarios and later to use Grafana and Prometheus for metrics.


### 1. Install Docker

Docker is an open-source project that automates the deployment of software applications inside containers by providing an additional layer of abstraction and automation of OS-level virtualization on Linux.

![It is **STRONGLY RECOMMENDED** you provide at least 4GB of RAM and 4 CPUs to Docker to handle this workload. If you do not have those resources available then I suggest using the Katacoda scenarios.](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/workshop-week6-docker-requirements.png)

![Windows](https://github.com/DataStax-Academy/nosqlbench-workshop-online/blob/master/materials/images/windows32.png?raw=true) : To install on **windows** please use the following installer [Docker Dekstop for Windows Installer](https://download.docker.com/win/stable/Docker%20Desktop%20Installer.exe)

![osx](https://github.com/DataStax-Academy/nosqlbench-workshop-online/blob/master/materials/images/mac32.png?raw=true) : To install on **MAC OS**  install Docker Desktop for Mac, follow the instructions here [Install Docker Desktop on Mac](https://docs.docker.com/docker-for-mac/install/)

![linux](https://github.com/DataStax-Academy/nosqlbench-workshop-online/blob/master/materials/images/linux32.png?raw=true) : To install on linux (centOS) you can use the following commands
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

# Logout....Login
exit
# Create user
sudo usermod -aG docker $USER
newgrp docker

# Validation
docker images
docker run hello-world
docker -v
```

**NOTE**: To install Docker on Ubuntu, follow the **Install Docker Engine** section in [https://community.datastax.com/questions/5369/](https://community.datastax.com/questions/5369/).

### 2. Start Cassandra
Obviously NoSQLBench needs something to benchmark against and in this workshop we will use an Apache Cassandra™ database. 
Let's start one up using Docker.

📘 **Command to execute**
```
docker run --name my-cassandra -p 9042:9042 -d cassandra:latest
```
That's it. This will spin up the latest version of Cassandra on port 9042. Should just take a moment.

### If you already happen to have a database up and running you can skip to step 3. It is assumed you are using default port 9042 for Cassandra.

### 3. Download NoSQLBench

You can run NoSQLBench via a Linux binary or jar file. It is recommended to use the binary as it includes its own JVM and does not need to manage Java downloads, but for those NOT using Linux systems the jar is fine. If you are using the jar Java 14 is recommended. *MacOS* users may want to check out [https://github.com/AdoptOpenJDK/homebrew-openjdk](https://github.com/AdoptOpenJDK/homebrew-openjdk) to get Java 14 using openJDK on the Mac.

It is **STRONGLY RECOMMENDED** that you create a directory to use with NoSQLBench and download it there as we will generate many files over the course of our time in the workshop.

![Windows](https://github.com/DataStax-Academy/nosqlbench-workshop-online/blob/master/materials/images/windows32.png?raw=true)  ![osx](https://github.com/DataStax-Academy/nosqlbench-workshop-online/blob/master/materials/images/mac32.png?raw=true): To download and install on Windows or OSX simply download the latest release jar file [HERE](https://github.com/nosqlbench/nosqlbench/releases/latest/download/nb.jar)

Once the jar download completes you are complete until the next exercsise.

![linux](https://github.com/DataStax-Academy/nosqlbench-workshop-online/blob/master/materials/images/linux32.png?raw=true) : To install on linux use the following commands.

📘 **Commands to execute**
```bash
# Download NoSQLBench binary
wget https://github.com/nosqlbench/nosqlbench/releases/latest/download/nb

# Let's ensure ensure nb is executable
chmod +x nb
```
## Congratulations, your initial NoSQLBench setup is complete. We'll start running some commands in the next section. Click [HERE](../1-executing-commands/README.md) to go to the next scenario.
