
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# Application Development with JAVA EXERCISES

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-java.png)

[🏠 GO BACK TO MAIN](./README.MD)


## 3. Connectivity to Cassandra 

**✅ Step 3a. Fix unit test `Ex02_Connect_to_Cassandra.java`**

- Unit Tests are located in `src/test/java` [HERE](./src/test/java/com/datastax/workshop). Open the file `DBConnection` and fill the properties with your Astra informations.

*Do not copy-paste - this is a sample for me.*
```java
// This is the Zip file you downloaded
String SECURE_CONNECT_BUNDLE = "/workspace/cassandra-workshop-series/week3-AppDev-crud/secure-connect-killrvideoclusters.zip";
// This is the username, recommended value was KVUser
String USERNAME = "KVUser";
// This is the password, recommended value was KVPassword
String PASSWORD = "KVPassword";
// This is the keyspace name, recommended value was killrvideo
String KEYSPACE = "killrvideo";
```

```
⚠️ By default Autosaved is not enabled in Gitpod. Don't forget to save your modifications with CTRL+S
```

Compile
```bash
cd crud-java/
mvn clean compile
```

```
INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ crud-java ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 4 source files to /workspace/cassandra-workshop-series/week3-AppDev-crud/crud-java/target/classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.014 s
[INFO] Finished at: 2020-07-14T12:09:29Z
[INFO] ------------------------------------------------------------------------
```

- You can now run the UNIT TEST with your IDE or Maven in a terminal (easier with gitpod 

```bash
mvn test -Dtest=com.datastax.workshop.Ex02_Connect_to_Cassandra
```

**👁️ Expected output**

```bash
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex02_Connect_to_Cassandra
13:58:52.224 INFO  Exercise2  : ========================================
13:58:52.227 INFO  Exercise2  : Start exercise
13:58:52.230 INFO  Exercise2  : File /workspace/cassandra-workshop-series/week3-AppDev-crud/secure-connect-devworkshopdb.zip located
13:58:55.526 INFO  Exercise2  : Connected with Keyspace todoapp
13:58:57.567 INFO  Exercise2  : SUCCESS
13:58:57.567 INFO  Exercise2  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.074 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  10.955 s
[INFO] Finished at: 2020-07-14T13:58:57Z
[INFO] ------------------------------------------------------------------------
```




## 4. CREATE READ UPDATE

### Create a journey

```
mvn test -Dtest=com.datastax.workshop.Ex03_Query5a_Insert_Journey
```

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex03_Query5a_Insert_Journey
14:00:05.184 INFO  Exercise3  : ========================================
14:00:05.186 INFO  Exercise3  : Start exercise
14:00:08.434 INFO  Exercise3  : Journey created : 53983d90-c5da-11ea-bcd8-c78bf1f1af98
14:00:08.435 INFO  Exercise3  : SUCCESS
14:00:08.435 INFO  Exercise3  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.075 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.527 s
[INFO] Finished at: 2020-07-14T14:00:08Z
[INFO] ------------------------------------------------------------------------
```

Took the journeyId 

open Ex04_Query5b_TakeOff locate constant `JOURNEY_ID` and change with propert value here for me it is `53983d90-c5da-11ea-bcd8-c78bf1f1af98`

```
mvn test -Dtest=com.datastax.workshop.Ex04_Query5b_TakeOff
```

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex04_Query5b_TakeOff
14:04:52.018 INFO  Exercise3  : ========================================
14:04:52.021 INFO  Exercise3  : Start exercise
14:04:55.309 INFO  Exercise3  : 9..8..7..6..5..4..3..2..1 Ignition
14:04:55.316 INFO  Exercise3  : Journey 53983d90-c5da-11ea-bcd8-c78bf1f1af98 has now taken off
14:04:55.318 INFO  Exercise3  : SUCCESS
14:04:55.318 INFO  Exercise3  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.153 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.050 s
[INFO] Finished at: 2020-07-14T14:04:57Z
[INFO] ------------------------------------------------------------------------
```

```
mvn test -Dtest=com.datastax.workshop.Ex05_Query5c_Travel
```

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex05_Query5c_Travel
14:05:34.801 INFO  Exercise3  : ========================================
14:05:34.804 INFO  Exercise3  : Start exercise
14:05:38.333 INFO  Exercise3  : 0/50 - travelling..
14:05:38.542 INFO  Exercise3  : 1/50 - travelling..
14:05:38.749 INFO  Exercise3  : 2/50 - travelling..
...
14:05:48.641 INFO  Exercise3  : 49/50 - travelling..
14:05:48.641 INFO  Exercise3  : Reading saved
14:05:48.641 INFO  Exercise3  : SUCCESS
14:05:48.641 INFO  Exercise3  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 16.598 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  18.441 s
[INFO] Finished at: 2020-07-14T14:05:50Z
[INFO] ------------------------------------------------------------------------
```

```
mvn test -Dtest=com.datastax.workshop.Ex06_Query5d_Landing
```

```
------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex06_Query5d_Landing
14:07:42.405 INFO  Exercise3  : ========================================
14:07:42.414 INFO  Exercise3  : Start exercise
14:07:45.707 INFO  Exercise3  : Journey 53983d90-c5da-11ea-bcd8-c78bf1f1af98 has now landed
14:07:45.709 INFO  Exercise3  : SUCCESS
14:07:45.709 INFO  Exercise3  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.12 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.091 s
[INFO] Finished at: 2020-07-14T14:07:47Z
[INFO] ------------------------------------------------------------------------
```

```
mvn test -Dtest=com.datastax.workshop.Ex07_Query4a_ListJourneys
```

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex07_Query4a_ListJourneys
14:08:46.286 INFO  Exercise4  : ========================================
14:08:46.288 INFO  Exercise4  : Start exercise
14:08:49.872 INFO  Exercise4  : - Journey: 53983d90-c5da-11ea-bcd8-c78bf1f1af98 Summary: Bring Astronauts to ISS
14:08:49.874 INFO  Exercise4  : - Journey: 90163870-c5d6-11ea-b11f-c30e2b038000 Summary: Bring Astronauts to ISS
14:08:49.876 INFO  Exercise4  : SUCCESS
14:08:49.876 INFO  Exercise4  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.619 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.361 s
[INFO] Finished at: 2020-07-14T14:08:51Z
[INFO] ------------------------------------------------------------------------
```

```
mvn test -Dtest=com.datastax.workshop.Ex08_Query4b_Read_Journey_Details
```

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex08_Query4b_Read_Journey_Details
14:10:47.851 INFO  Exercise4  : ========================================
14:10:47.853 INFO  Exercise4  : Start exercise
14:10:50.963 INFO  Exercise4  : Journey has been found
14:10:50.963 INFO  Exercise4  : - Uid:           53983d90-c5da-11ea-bcd8-c78bf1f1af98
14:10:50.965 INFO  Exercise4  : - Spacecraft:    Crew Dragon Endeavour,SpaceX
14:10:50.965 INFO  Exercise4  : - Summary:       Bring Astronauts to ISS
14:10:50.965 INFO  Exercise4  : - Takeoff:       2020-07-14T14:04:55.311Z
14:10:50.967 INFO  Exercise4  : - Landing:       2020-07-14T14:07:45.702Z
14:10:50.967 INFO  Exercise4  : SUCCESS
14:10:50.967 INFO  Exercise4  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.828 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.608 s
[INFO] Finished at: 2020-07-14T14:10:53Z
[INFO] ------------------------------------------------------------------------
```

```
mvn test -Dtest=com.datastax.workshop.Ex09_Query4c_ReadMetrics
```

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex09_Query4c_ReadMetrics
14:11:51.130 INFO  Exercise4  : ========================================
14:11:51.132 INFO  Exercise4  : Start exercise
14:11:54.190 INFO  Exercise4  : idx:1, time=2020-07-14T14:05:48.436Z, value=349.8946944141032
14:11:54.196 INFO  Exercise4  : idx:2, time=2020-07-14T14:05:48.231Z, value=356.84344091063286
14:11:54.197 INFO  Exercise4  : idx:3, time=2020-07-14T14:05:48.025Z, value=347.11910951750343
14:11:54.197 INFO  Exercise4  : idx:4, time=2020-07-14T14:05:47.821Z, value=353.79479760820146
...
14:11:54.206 INFO  Exercise4  : idx:48, time=2020-07-14T14:05:38.543Z, value=304.333270325714
14:11:54.206 INFO  Exercise4  : idx:49, time=2020-07-14T14:05:38.335Z, value=304.42852584020767
14:11:54.206 INFO  Exercise4  : idx:50, time=2020-07-14T14:05:38.066Z, value=307.3590786240418
14:11:54.206 INFO  Exercise4  : SUCCESS
14:11:54.206 INFO  Exercise4  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.818 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.611 s
[INFO] Finished at: 2020-07-14T14:11:56Z
[INFO] ------------------------------------------------------------------------
```

```
mvn test -Dtest=com.datastax.workshop.Ex10_Query4c_ReadMetrics_Paging
```

```
------------------------------------------------------
 T E S T S
-------------------------------------------------------
Picked up JAVA_TOOL_OPTIONS: -Xmx2254m
Running com.datastax.workshop.Ex10_Query4c_ReadMetrics_Paging
14:13:20.382 INFO  Exercise4  : ========================================
14:13:20.385 INFO  Exercise4  : Start exercise
14:13:24.080 INFO  Exercise4  : Page1: 10
14:13:24.086 INFO  Exercise4  : - time=2020-07-14T14:05:48.436Z, value=349.8946944141032
14:13:24.088 INFO  Exercise4  : - time=2020-07-14T14:05:48.231Z, value=356.84344091063286
14:13:24.089 INFO  Exercise4  : - time=2020-07-14T14:05:48.025Z, value=347.11910951750343
14:13:24.089 INFO  Exercise4  : - time=2020-07-14T14:05:47.821Z, value=353.79479760820146
14:13:24.089 INFO  Exercise4  : - time=2020-07-14T14:05:47.615Z, value=345.85152561418556
14:13:24.089 INFO  Exercise4  : - time=2020-07-14T14:05:47.410Z, value=346.68705214383374
14:13:24.089 INFO  Exercise4  : - time=2020-07-14T14:05:47.204Z, value=351.9627849961532
14:13:24.090 INFO  Exercise4  : - time=2020-07-14T14:05:46.999Z, value=343.71082151200267
14:13:24.090 INFO  Exercise4  : - time=2020-07-14T14:05:46.794Z, value=343.7901330585385
14:13:24.090 INFO  Exercise4  : - time=2020-07-14T14:05:46.588Z, value=346.1033175527708
14:13:24.097 INFO  Exercise4  : Page2: 10
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:46.383Z, value=346.5964547379327
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:46.178Z, value=346.79413100267135
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:45.972Z, value=345.02578761204694
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:45.767Z, value=338.60645370492
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:45.562Z, value=341.0456211449011
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:45.357Z, value=337.9532897908187
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:45.151Z, value=335.7301839936337
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:44.946Z, value=334.98991697137654
14:13:24.098 INFO  Exercise4  : - time=2020-07-14T14:05:44.741Z, value=333.82733283481525
14:13:24.099 INFO  Exercise4  : - time=2020-07-14T14:05:44.535Z, value=336.42345676874857
14:13:24.099 INFO  Exercise4  : SUCCESS
14:13:24.099 INFO  Exercise4  : ========================================
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.896 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.336 s
[INFO] Finished at: 2020-07-14T14:13:26Z
[INFO] ------------------------------------------------------------------------
```


### Launch the space craft

### Record telemetry

### Landing


## 5. READ AND PARSE RESULTS



There are 2 ways to do the exercises, **locally** on your computer and with a **cloud-based IDE named Gitpod.** We recommend you to use your laptop in order to save code modifications and come back later. In both cases you should start by cloning the repository and download everything (including slides).

- ### Option A - Work with a 100% Cloud-based Environment ☁️

**✅ Open gitpod** : [Gitpod](https://www.gitpod.io/) is an IDE 100% online based on Eclipse Theia. To initialize your environment simply click on the button below.

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/DataStax-Academy//cassandra-workshop-series)

A new tab opens in your browser at  `https://<your_uid>.<your_region>.gitpod.io/#/workspace//cassandra-workshop-series`. Those URL are dynamic and we cannot provide clickable links in advance. You should copy-paste `<your_uid>.<your_region>` as we will insert them in each URL  during the exercises.

**👁️ Expected output**
![TODO](#)

**That's it.** Gitpod provides all tools we will need for today including `Maven` and exporting port `8080`. At initialization of the workspace we schedule a `mvn clean install` to download dependencies. You can still download the repository and bookmark it as all materials including slides are part of it.

- ### Option B - Work with Local Environment 💻

This part is dedicated to people will to run the exercises on their laptop. You can skip it and directly go to [2. Run the Todo Application](#2-run-the-todo-application) if you want to work with gitpod.

**✅ Clone the repository :** : With `git` or through the web UI clone the repo
```bash
# Clone the current repository
git clone https://github.com/DataStax-Academy/cassandra-workshop-series.git

# Access the proper folder
cd microservices-java-workshop-online
```

**✅ Install Java (JDK8+)** Pick the proper infos based on your operating system
- ![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) Please use the following tutorial [Installation of the JDK on Microsoft Windows Platforms](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-microsoft-windows-platforms.html#GUID-A7E27B90-A28D-4237-9383-A58B416071CA)
- ![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) Please use the following tutorial [Installation of the JDK on Linux Platform Platforms](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-linux-platforms.html#GUID-737A84E4-2EFF-4D38-8E60-3E29D1B884B8)
- ![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) Please use the following tutorial [Installation of the JDK on MACOS Platforms](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-macos.html#GUID-2FE451B0-9572-4E38-A1A5-568B77B146DE) or with [homebrew](https://docs.brew.sh/Installation):
```bash
brew cask install java
```

**✅ Install Maven**
- ![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) Please use the following tutorial [How to install Maven on Windows](#https://mkyong.com/maven/how-to-install-maven-in-windows/)
- ![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true). For CentOS you can use: `sudo yum install maven`
- ![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) For MacOS you can use `brew install maven`

Validate installation
```bash
mvn -v
```

**👁️ Expected output**
```bash
Apache Maven 3.6.0 (97c98ec64a1fdfee7767ce5ffb20918da4f719f3; 2018-10-24T20:41:47+02:00)
Maven home: /usr/local/Cellar/maven/3.6.0/libexec
Java version: 12, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/openjdk-12.jdk/Contents/Home
Default locale: en_FR, platform encoding: UTF-8
OS name: "mac os x", version: "10.15.3", arch: "x86_64", family: "mac"
```

**✅ Install an IDE**

Pick and install your favourite Java IDE. Here are some propositions without preference order.

| Tools  | Download link
|---|---|
| **Eclipse STS** | 📥[Download Eclipse Spring Tools Suite](https://spring.io/tools#main)
| **Jetbrains IntelliJ** | 📥[Download IntelliJ](https://www.jetbrains.com/idea/download/index.html)
| **Core Eclipse** | 📥[Download Eclipse](https://www.eclipse.org/downloads/)
| **Visual Studio Code** | 📥[Downlowd Visual Code](https://code.visualstudio.com/Download)

**✅ Install Docker** (=not for today, to run Cassandra Locally)

Docker is an open-source project that automates the deployment of software applications inside containers by providing an additional layer of abstraction and automation of OS-level virtualization on Linux.

- ![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : To install on **windows** please use the following installer [Docker Dekstop for Windows Installer](https://download.docker.com/win/stable/Docker%20Desktop%20Installer.exe)

- ![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : To install on **MAC OS**  use [Docker Dekstop for MAC Installer](https://download.docker.com/mac/stable/Docker.dmg) or [homebrew](https://docs.brew.sh/Installation) with following commands:
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

- ![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) : To install on linux (centOS) you can use the following commands
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

**✅ Install Docker Compose** (=not for today, to run Cassandra Locally)

Docker Compose is a tool for defining and running multi-container Docker applications. It uses YAML files to configure the application's services and performs the creation and start-up process of all the containers with a single command. The `docker-compose` CLI utility allows users to run commands on multiple containers at once, for example, building images, scaling containers, running containers that were stopped, and more. Please refer to [Reference Documentation](https://docs.docker.com/compose/install/) if you have for more detailed instructions.

- ![Windows](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/windows32.png?raw=true) : Already **included** in the previous package *Docker for Windows*

- ![osx](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/mac32.png?raw=true) : Already **included** in the previous package *Docker for Mac*

- ![linux](https://github.com/DataStax-Academy/kubernetes-workshop-online/blob/master/4-materials/images/linux32.png?raw=true) : To install on linux (centOS) you can use the following commands

```bash
# Download deliverable and move to target location
sudo curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Allow execution
sudo chmod +x /usr/local/bin/docker-compose
```

[🏠back to table of content](#table-of-content)

## 2. Run the Todo Application

**TodoMVC** is a fabulous community contribution that helps developers compare frameworks on the basis of actual project code, not just claims and anecdotes. Michael Mahemoff. TodoMVC is an immensely valuable attempt at a difficult problem - providing a structured way of comparing JS libraries and frameworks.

![TodoMVC](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todomvc.png?raw=true)

**✅ Test todoMVC application** : Access the [http://todomvc.com/](http://todomvc.com/) website and test one application with the framework of your choice.

**Todo-Backend** is a shared example to showcase backend tech stacks. The Todo-Backend project defines a simple web API spec - for managing a todo list. Contributors implement that spec using various tech stacks. Those implementations are cataloged below. A spec runner verifies that each contribution implements the exact same API, by running an automated test suite which defines the API.

![TodoBackend](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todobackend.png?raw=true)

There are multiple free implementations available already. Some implementations provide endpoints to be tested in `heroku`. [Heroku provide free hosting for a Java application but this is on-demand](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku). With `Heroku` The application will be sleeping until you access the endpoint. So it will take 30s for the first person to have the application running. Among the available free endpoints we can list:
- `Endpoint#1` : [https://todo-back-springboot220-java12.herokuapp.com/todos](https://todo-back-springboot220-java12.herokuapp.com/todos)
- `Endpoint#2` : [https://todo-quarkus.herokuapp.com/todos](https://todo-quarkus.herokuapp.com/todos)
- `Endpoint#3` : [https://todo-backend-micronaut.herokuapp.com/todos](https://todo-backend-micronaut.herokuapp.com/todos)

**✅ Test TodoBackEnd Spec Runner** : Locate the [spec runner](https://www.todobackend.com/specs/index.html) and use one of the `endpoints` listed below to evaluate if API matchs the requirements. To do so copy-paste one URL and click `Run Tests`.

![TodoBackendTest](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todobackend-runtest.png?raw=true)

**👁️ Expected output**

![TodoBackendOuput](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todobackend-output-host.png?raw=true)

**Note:** *During the live stream you will be hundreds testing the same endpoints so - maybe - we could hit some issues = race conditions.*

**✅ Test TodoBackEnd Web Client** : todoBackend.com provides a [client](https://www.todobackend.com/client/index.html) to work with the API. As before pick one of the `endpoints` listed before and try the client.

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todobackend-runclient.png?raw=true)

**👁️ Expected output**

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todobackend-output-client.png?raw=true)

Our mission withing the next hour is to implement the *backend API* and store  data into Apache Cassandra™. We have you covered by providing the skeleton of the application.

**✅ Start Backend API** : By using a maven command or run application in your IDE

```bash
# Build the project without tests (you did not implemented them yet TDD baby !)
mvn clean install -Dmaven.test.skip=true

# Navigate to the proper folder
cd wkshop-microservice-2-spring-boot/

# Start the backend API
mvn spring-boot:run
```

**👁️ Expected output**
```bash
[...]
INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for Spring microservices with Apache Cassandra 1.0-SNAPSHOT:
[INFO] 
[INFO] Spring microservices with Apache Cassandra ......... SUCCESS [  4.115 s]
[INFO] + microservice-1-common ............................ SUCCESS [  8.893 s]
[INFO] + microservice-2-spring-boot ....................... SUCCESS [  3.504 s]
[INFO] + microservice-3-spring-data ....................... SUCCESS [  1.369 s]
[INFO] + microservice-4-spring-webflux .................... SUCCESS [  0.311 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  18.701 s
[INFO] Finished at: 2020-06-15T13:08:57Z
[INFO] ------------------------------------------------------------------------
```

```bash
 ________                __      __ __           .__                   
 \______ \   _______  __/  \    /  \  | __  _____|  |__   ____ ______  
  |    |  \_/ __ \  \/ /\   \/\/   /  |/ / /  ___/  |  \ /  _ \\____ \ 
  |    `   \  ___/\   /  \        /|    <  \___ \|   Y  (  <_> )  |_> >
 /_______  /\___  >\_/    \__/\  / |__|_ \/____  >___|  /\____/|   __/ 
         \/     \/             \/       \/     \/     \/       |__|    

 Backend API implementation of todobackend.com
 We are using SpringBoot and Apache Cassandra
 The application is started at http://localhost:8080  

14:54:54.773 INFO  com.datastax.sample.TodoBackendApplication         : Starting TodoBackendApplication on clunhost with PID 75757 (/Users/cedricklunven/dev/WORKSPACES/DATASTAX/microservices-java-workshop-online/wkshop-microservice-2-spring-boot/target/classes started by cedricklunven in /Users/cedricklunven/dev/WORKSPACES/DATASTAX/microservices-java-workshop-online/wkshop-microservice-2-spring-boot)
14:54:54.775 INFO  com.datastax.sample.TodoBackendApplication         : No active profile set, falling back to default profiles: default
14:55:03.604 INFO  com.datastax.sample.TodoBackendApplication         : Started TodoBackendApplication in 1.075 seconds (JVM running for 1.492)
```

**✅ Show Api Documentation** : through your browser

- 💻 If you are on your **laptop** : open a browser and navigate to [http://localhost:8080](http://localhost:8080)

- ☁️ If you are on **gitpod** : a popup may show up and redirect you to `https://8080-<your_id>.<>your_region.gitpod.io` as you can see this an alias for port `8080`.

**👁️ Expected output**

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/backend-doc.png?raw=true)

**✅ Test Backend API** :using the OpenAPI generated documentation. You can open the `GET` bloc labeled `/api/v1/todos` and pick `Try It Out`. Then locate `Execute` to test the API  and get a few results.

**👁️ Expected output**

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/todobackend-swagger-test.png?raw=true)

**✅ Test our Backend API against the spec runner** : This REST API store data **In memory** but is 100% valid. We can validate by testing the same spec runner as before.

- 💻 If you are on your **laptop** open a browser and navigate to [https://www.todobackend.com/specs/index.html?http://localhost:8080/api/v1/todos](https://www.todobackend.com/specs/index.html?http://localhost:8080/api/v1/todos)

-  ☁️ If you are on **gitpod** : a popup may show up and redirect you to `https://8080-<your_id>.<>your_region.gitpod.io/api/v1/todos`.

**👁️ Expected output**

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/realbackend-test.png?raw=true)

**✅ Test our Web Client UI against our Backend API** : This is neat, the client and spec runner both work even with URL like `localhost` because this is some `javascript` code executed at client side.

Back the client UI change the URL to match you backend URL :
- 💻 If your work locally : [https://www.todobackend.com/client/index.html?http://localhost:8080/api/v1/todos](https://www.todobackend.com/client/index.html?http://localhost:8080/api/v1/todos)
 

- ☁️ If you are using gitpod `https://www.todobackend.com/client/index.html?https://8080-<your_id>.<your_region>-eu01.gitpod.io/api/v1/todos`

**👁️ Expected output**

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/backend-ok.png?raw=true)

*Ok I changed a little bit the values but now this is working....* 

[🏠 Back to Table of Content](#table-of-content)

## 3. Create your Astra instance

Access to `ASTRA` service on url [https://astra.datastax.com](https://astra.datastax.com/)

**✅  Register (if needed) and Sign In to Astra**

- Access [Registration Page](https://astra.datastax.com/register)
![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/backend-ok.png?raw=true)

- Access [Authentication Page](https://astra.datastax.com/)
![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-login.png?raw=true)


### Fill the Create New Database Form

As you don't have have any instances the login will route through the instance creation form. You will find below which values to enter for each field.

<img src="https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-online/master/images/03.png" height="400" />


- **Set the Compute Size**: For the work we are doing please use `Free tier`. You instance will be there forever, free of charge

- **Select the region**: This is the region where your database will reside physically (choose one close to you or your users). For people in EMEA please use `europe-west-1` idea here is to reduce latency.

- **Fill in the database name** - `killrvideocluster.` While Astra allows you to fill in these fields with values of your own choosing, please follow our reccomendations to make the rest of the exercises easier to follow. If you don't, you are on your own! :)

- **Fill in the keyspace name** - `killrvideo`. It's really important that you use the name killrvideo (with no 'e' in "killr") here in order for all the exercises to work well. We realize you want to be creative, but please just roll with this one today.

- **Fill in the Database User name** - `KVUser`. Note the user name is case-sensitive. Please use the case we suggest here.

- **Fill in the password** - `KVPassword`. Fill in both the password and the confirmation fields. Note that the password is also case-sensitive. Please use the case we suggest here.

- **Launch the database**. Review all the fields to make sure they are as shown, and click the Launch Database button.


<img src="https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-online/master/images/04.png" height="300" />


### View your Database and connect

View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point. But, go ahead and continue with the rest of the exercise now.

<img src="https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-online/master/images/05.png" height="300" />


Once Database is ready you should see the following home page:

<img src="https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-online/master/images/06.png" height="100" />


Let’s review the database you have configured. In the box on the top-left side of the window, you can see the database and keyspace name metadata. The box on the top-right describes the size and location of your database. The lower-left box shows your estimated cost. Once Astra initializes the database completely, the lower-right box will have connection details.

<img src="https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-online/master/images/07.png" height="100" />


### Open DataStax Studio

You can click on `Launch Developer Studio` blue button to start the tool. Please enter the credentials you used for Astra instance creation.

<img src="https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-online/master/images/08.png" height="300" />


- **Fill in the Database User name** - `KVUser`. Note the user name is case-sensitive. Please use the case we suggest here.

- **Fill in the password** - `KVPassword`. Fill in both the password and the confirmation fields. Note that the password is also case-sensitive. Please use the case we suggest here.

TADA !

[🏠 Back to Table of Content](#table-of-content)


## 4. Connectivity to Astra


[🏠back to table of content](#table-of-content)

## 5. CRUD Repository

[🏠back to table of content](#table-of-content)

## 6. CRUD Repository with Object Mapper
 
[🏠back to table of content](#table-of-content)

## 7. CRUD Repository with Spring Data

[🏠back to table of content](#table-of-content)

## 8. Going Reactive

```
XXX
```

[🏠back to table of content](#table-of-content)

## Exercise 3 -  Connectivity to Cassandra

Do X Y Z

```
XXX
```

[🏠back to table of content](#table-of-content)

## Exercise 4 -  Implement CRUD Repository

Do X Y Z

```
XXX
```

[🏠back to table of content](#table-of-content)

## Exercise 5 - Run and Test the API

Do X Y Z

```
XXX
```

[🏠back to table of content](#table-of-content)

## Exercise 6 - Spring DATA

Do X Y Z

```
XXX
```

[🏠back to table of content](#table-of-content)

## Exercise 7 - Spring Webflux

Do X Y Z

```
XXX
```

[🏠back to table of content](#table-of-content)


THE END.





