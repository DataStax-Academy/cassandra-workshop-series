![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨ Application Development  - API  REST✨

[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud) 
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

`Cassandra Workshop Series` are an interactive experience. Datastax Developer Advocates share some knowledge about Apache Cassandra™ NoSQL database and how you build Cloud Native applications. You interact with them through chats *([youtube](https://www.youtube.com/channel/UCAIQY251avaMv7bBv5PCo-A) and [discord](https://discord.com/widget?id=685554030159593522&theme=dark))*, quizzes (menti.com), and exercises. 

In this repository, you'll find everything you need related to `week 3` of the **Cassandra Worskhop Series**. For simplicity all exercise instructions will be listed in a single `README` for each language: Java, Javascript, Python, CSharp. We recommended you to have some previous experience with the language you choose. 

Now if you are NOT familiar with any of those, we recommended to use Python, the is the simplest. Ready ? go !

## Workshop Materials

| Materials  | Description and Links
|---|---|
| Slidedeck | [SLIDEDECK](./materials/presentation-week3.pdf) |
| Chat with us on Discord | [DISCORD](https://bit.ly/cassandra-workshop) |
| Ask Question during week | [COMMUNITY](https://community.datastax.com/spaces/172/index.html) |
| Recording LIVESTREAM NA, LATAM |  📅 [July 22th 12EDT](#) |
| Recording LIVESTREAM APAC, EMEA | 📅 [July 23th 12:30 IST](#) |
| Homework | [here](#) |

## Table of Contents

| Exercises | Description and Links
|---|---|
| 0. Prerequisites - Create your Astra instance | [Create your Astra instance](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud#1-create-your-astra-instance-reminders) |
| 1. Prerequisites : Create the Data model | [Create the Data model](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud#2-exercise--the-spacecraft-nodebook) |
| 2. Exercise : Start backend and API | [Start Backend](#3-start-the-backend) |
| 2. Exercise : Start UI and demo | [Start User Interface](#3-start-the-backend) |

## 1. Create your Astra instance (reminders)

`ASTRA` service is available at url [https://astra.datastax.com](https://astra.datastax.com/)

**✅ Step 1a. Register (if needed) and Sign In to Astra** : You can use your `Github`, `Google` accounts or register with an `email`

- [Registration Page](https://astra.datastax.com/register)

![Registration Image](images/astra-create-register.png?raw=true)

- [Authentication Page](https://astra.datastax.com/)

![Login Image](images/astra-create-login.png?raw=true)


**✅ Step 1b. Fill the Create New Database Form**

As you don't have have any instances the login will route through the instance creation form. You will find below which values to enter for each field.

![Database Form](images/astra-create-2.png?raw=true)

- **Set the Compute Size**: For the work we are doing please use `Free tier`. You instance will be there forever, free of charge

- **Select the region**: This is the region where your database will reside physically (choose one close to you or your users). For people in EMEA please use `europe-west-1` idea here is to reduce latency.

- **Fill in the database name** - `killrvideocluster.` While Astra allows you to fill in these fields with values of your own choosing, please follow our reccomendations to make the rest of the exercises easier to follow. If you don't, you are on your own! :)

- **Fill in the keyspace name** - `killrvideo`. It's really important that you use the name killrvideo (with no 'e' in "killr") here in order for all the exercises to work well. We realize you want to be creative, but please just roll with this one today.

- **Fill in the Database User name** - `KVUser`. Note the user name is case-sensitive. Please use the case we suggest here.

- **Fill in the password** - `KVPassword`. Fill in both the password and the confirmation fields. Note that the password is also case-sensitive. Please use the case we suggest here.

- **Launch the database**. Review all the fields to make sure they are as shown, and click the Launch Database button.


![Launch Database](images/astra-create-3.png?raw=true)

**✅ Step 1c. View your Database and connect**

View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point. But, go ahead and continue with the rest of the exercise now.

![View Database](images/astra-create-4.png?raw=true)

Once Database is ready you should see the following home page:

![Home Page](images/astra-create-5.png?raw=true)

Let’s review the database you have configured. In the box on the left side of the window, you can see the database and keyspace name metadata. The box on the right describes the size and location of your database along with your estimated cost. Once Astra initializes the database completely, the left box will have connection details.

## 2. Exercise : The spacecraft nodebook (reminders)

**✅ Step 2a. Open DataStax Studio**

You can click the **Launch Now** link at the bottom of "Connection Details" box on the left to start the tool. Please enter the credentials you used for Astra instance creation.

![Launch Database](images/astra-create-6.png?raw=true)

- **Fill in the Database User name** - `KVUser`. Note the user name is case-sensitive. Please use the case we suggest here.

- **Fill in the password** - `KVPassword`. Fill in both the password and the confirmation fields. Note that the password is also case-sensitive. Please use the case we suggest here.

- **Click Test**. Wait for a "connected successfully" message before saving.

- **Click Save**. Once you have a positive test then click the **Save** button to launch DataStax Studio.

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/launch-studio-home.png?raw=true)

- Download the file `spacecraft.tar` located in folder `notebooks` [download link](https://github.com/DataStax-Academy/cassandra-workshop-series/raw/master/week3-AppDev-crud/notebooks/spacecraft.tar)

- Import the file in studio with a drag and drop in the Datastax studio window

*Importing*

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/launch-studio-import.png?raw=true)

*File is now opened*

![notebook](images/notebook-open.png?raw=true)

[🏠 Back to Table of Content](#table-of-content)

## 3. Exercise : Start the backend

### 3.1 Start the JAVA BACKEND

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-java.png)

This provides an example REST backend built in Java using `Spring Boot`. This application serves as the connection between the UI website and an underlying Astra database. It has Swagger installed.

Creation of `CqlSession` objects within an application is an expensive process as they take awhile to initialize and become aware of the clusters topology.  Due to this it is a best practice to create a `CqlSession` object once per application and reuse it throughout the entire lifetime of that application.

`SessionManager` implements the Singleton pattern to handle a single instance of the `CqlSession` object. Note that this is the only bean not initialized by Spring at startup. This is only when you have provided the secure credential bundle that we can initiate a connection.

#### 3.1.1 Run the Java Backend in gitpod

**✅  Open gitpod** : [Gitpod](https://www.gitpod.io/) is an IDE 100% online based on Eclipse Theia. To initialize your environment simply click on the button below *(CTRL + Click to open in new tab)*

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/DataStax-Academy/cassandra-workshop-series)

Target url looks like  `https://<your_uid>.<your_region>.gitpod.io/#/workspace/cassandra-workshop-series`. These URL's are dynamic and we cannot provide clickable links in advance. You will need to copy-paste `<your_uid>.<your_region>` as we will insert them in each URL during the exercises.

```
⚠️⚠️⚠️ 
By default Autosave is not enabled in Gitpod. Don't forget to save your modifications with CTRL+S
⚠️⚠️⚠️
```

**👁️ Expected output**

![Splash](images/gitpod-splash.png?raw=true)

**That's it.** Gitpod provides all tools we will need today including `maven`,`pip`,`npm`. At initialization of the workspace we schedule a command to download dependencies.

**✅ Start SpringBoot**

Move to folder ` week4-AppDev-api` to get the exercises of the week and `getting-started-with-astra-java` to work with JAVA.

```
cd week4-AppDev-api/getting-started-with-astra-java
mvn spring-boot:run
```

You should be able to access to the UI with. Note that the `8080` port has been redirected to port `80` but changing the URL with a `8080` prefix and enforcing HTTPS. With the UI (swagger) you can start executing some requests against the REST API

```
https://8080-<your_uid>.<your_region>.gitpod.io/swagger-ui.html
```


#### 3.1.2 Run the java Backend locally

**✅ Prerequisites**

* Java 11+
* An Astra compatible Java driver, instructions may be found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudConnectJavaDriver.html) to install this locally.
* An Astra database with the CQL schema located in [schema.cql](src/main/resources/schema.cql) already added.
* The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudObtainingCredentials.html).

**✅ Start SpringBoot**

This application is a Spring Boot web application. This sample can be run from the root directory using:
```
cd getting-started-with-astra-java
mvn spring-boot:run
```
This will startup the application running on `http://localhost:8080`
You will know that you are up and working when you get the following in your terminal window:

```
16:23:01.569 INFO  com.datastax.astra.GettingStartedWithAstra  : Started GettingStartedWithAstra in 1.851 seconds (JVM running for 2.39)
```

**✅ Access the API from a browser**

[http://localhost:8080](http://localhost:8080)

*Note: If you want to change the listening port of the application, locate the file `src/main/resources/application.yml` and change key `server.port`*

### 3.3 Start the PYTHON BACKEND

#### 3.3.1 Run locally

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-python.png)

This sample Python backend provides a REST API service that is used with the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui) to show a
simple example of how to connect to and query DataStax Astra databases. This application serves as the connection between the UI website and an underlying Astra database. 

**Objectives**
- How to connect to Astra using the Secure Connect Bundle
- How to manage a Cassandra Session within a Python web application

**Project Layout**
- [getting_started_with_astra.py](getting_started_with_astra.py) - entrypoint for the backend, registers controller blueprints with Flask app
- [schema.cql](schema.cql) - database schema used by the application
- [service](service/) - acts as the middle-man to take requests from the controllers and calls the corresponding dao methods.
Note how this and [session_manager.py](dao/session_manager.py) are used to share a single DataStax Driver Session across API requests, this is a best practice.
- [model](model/) - defines the Python objects that correspond to the database tables
- [dao](dao/) - methods for accessing the database, contains the DataStax Driver API calls
- [controller](controller/) - defines the API endpoints using Flask decorators

**How this works**
This project is built in Python and uses Flask to expose a REST API backend for use with the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

This application is the middle man that receives requests from the UI web page and serves data from the underlying DataStax Astra database.

### 3.2.1 Run the Python Backend locally

*Note: You cannot run this application on GitPod. This application needs to be run locally.

**✅ Prerequisites**

* Python 3.6 or newer
* An Astra compactible python driver, instructions may be found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudConnectPythonDriver.html)
* An Astra database with the CQL schema located in [schema.cql](schema.cql) already added.
* The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudObtainingCredentials.html).

This appliation uses the Flask framework. This sample can be run from the root directory.

**✅ Installation procedure**

**a) Setup environment**

We advise that you set up a virtual python environment with Python 3 first (e.g with pyenv or venv).
To setup a virtual environment with [venv](https://docs.python.org/3/library/venv.html)

```
python3 -m venv astra-venv
cd astra-venv
source bin/activate
```
The activation will prepend your command prompt with `(astra-venv)`, indicating that you are operating in your virtual environment.  All dependencies will be installed into this environment only.

**b) install dependencies**
```
pip install Flask flask-cors cassandra-driver
```

#### c) Clone this repository
```
git clone https://github.com/DataStax-Academy/cassandra-workshop-series
```

**✅ Starting procedure**

Go to the directory that you just cloned this repo into
```
cd getting-started-with-astra-python
python getting-started-with-astra.py
```

This will start the application running on `http://127.0.0.1:5000`

You should see the following output:
```
 $ python getting_started_with_astra.py 
 * Serving Flask app "getting_started_with_astra" (lazy loading)
 * Environment: production
   WARNING: This is a development server. Do not use it in a production deployment.
   Use a production WSGI server instead.
 * Debug mode: off
 * Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
```

**c) Access the API documentation from a browser**
[http://127.0.0.1:5000](http://127.0.0.1:5000)

*Note: If you want to change the listening port of the application, change it in the app.run() call in `getting-started-with-astra.py`, eg 
```
app.run(port=5001)
```

#### 3.3.2 Run in gitpod

**✅  Open gitpod** : [Gitpod](https://www.gitpod.io/) is an IDE 100% online based on Eclipse Theia. To initialize your environment simply click on the button below *(CTRL + Click to open in new tab)*

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/DataStax-Academy/cassandra-workshop-series)

Target url looks like  `https://<your_uid>.<your_region>.gitpod.io/#/workspace/cassandra-workshop-series`. These URL's are dynamic and we cannot provide clickable links in advance. You will need to copy-paste `<your_uid>.<your_region>` as we will insert them in each URL during the exercises.

```
⚠️⚠️⚠️ 
By default Autosave is not enabled in Gitpod. Don't forget to save your modifications with CTRL+S
⚠️⚠️⚠️
```

**👁️ Expected output**

![Splash](images/gitpod-splash.png?raw=true)


```
⚠️ By default Autosave is not enabled in Gitpod. Don't forget to save your modifications with CTRL+S
```


#### d) Start the front end in gitpod
​
```
cd week4-AppDev-api/getting-started-with-astra-ui 
npm install
```
​
You will need to create a file named `.env` with an environment variable that points to your backend api
​
```
echo 'BASE_ADDRESS=https://https://5000-<your_uid>.<your_region>.gitpod.io/api' > .env
cat .env
```
​
```
npm run start
```
​
You should be able to access:
​
```
https://3000-<your_uid>.<>your_region.gitpod.io/
```



**✅ Installation procedure**

```
pip install Flask flask-cors cassandra-driver
```

**✅ Starting the Flask application procedure**

Move to folder ` week4-AppDev-api` to get the exercises of the week and `getting-started-with-astra-python` to work with PYTHON.

```
cd week4-AppDev-api/getting-started-with-astra-python
python getting-started-with-astra.py
```

You should be able to access to the UI with. Note that the `8080` port has been redirected to port `80` but changing the URL with a `8080` prefix and enforcing HTTPS. With the UI (swagger) you can start executing some requests against the REST API.

```
https://5000-<your_uid>.<your_region>.gitpod.io/swagger-ui.html
```

It's open, but there is nothing to see:

```
Not Found
The requested URL was not found on the server. If you entered the URL manually please check your spelling and try again.
```

You will see an `Open Ports` tab in the gitpod terminal window, where you can check on the status of the ports and access previews and browsers. 


*Note: If you want to change the listening port of the application, change it in the app.run() call in `getting-started-with-astra.py`, eg 
```
app.run(port=5001)
```

### 3.4 Start the CSHARP BACKEND

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-csharp.png)

#### 3.4.1 Run locally

#### 3.4.2 Run in gitpod

*N/A as of now, we did not setup gitpod to work with dotnet.*

## 2. Exercise : Start the UI

This provides the front end for the Astra Getting Started application. This is meant to be used with one of the following backends (java, python, csharp)

**Objectives**
- Connect a web application to one of four backends to demonstrate an end to end full stack application
- Demonstrate how to properly page results for an Astra Service

![UI](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/week4-AppDev-api/images/ui.png)

#### Run locally

Prerequisites

- node v8.1.0

Running

A `.env` file is required before you can build the application. This file needs to be added to the root of the project and called .env. This file contains all the environment variables required for the application. Currently there is only one required entry:

```
BASE_ADDRESS=http://localhost:5000/api
```
This value is where you put the relative address of the api.

In order to start this application you need to run:

```
npm install
```
and then

```
npm run start
```
You will know that this is correctly running when you see this message in your terminal:

```
ℹ ｢wds｣: Project is running at http://localhost:3000/
ℹ ｢wds｣: webpack output is served from /
ℹ ｢wds｣: Content not from webpack is served from /Users/dave.bechberger/Documents/projects/bechbd/getting-started-with-astra-ui
ℹ ｢wds｣: 404s will fallback to /index.html
```

If you would like to build this for deployment then this can be accomplished using:

```
npm run build
```

#### Run in gitpod

Locate the folder and install the dependencies:
```
cd week4-AppDev-api/getting-started-with-astra-ui 
npm install
```

A `.env` file is required before you can build the application. This file needs to be added to the root of the project and called .env. This file contains all the environment variables required for the application. Currently there is only one required entry:

```
echo 'BASE_ADDRESS=https://<your_port>-<your_id>.<your_region>.gitpod.io/api' > .env
cat .env
```

- <your_port>: Would be `8080` is you pick the Jaba Backend, `5000` if you pick the python backend and `5001` if you pick the CSharp backend.

```
npm run start
```

Your should be able to access
```
https://3000-<your_uid>.<>your_region.gitpod.io/
```

[🏠 Back to Table of Content](#table-of-content)

[🏠Back to HOME workshop](https://github.com/DataStax-Academy/cassandra-workshop-series)

THE END.


