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
| Prerequisites - Create your Astra instance | [Create your Astra instance](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud#1-create-your-astra-instance-reminders) |
| Prerequisites : Create the Data model | [Create the Data model](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud#2-exercise--the-spacecraft-nodebook) |
| Exercise : Start backend and API | [Start JAVA Backend](#21-start-the-java-backend) |
| Exercise : Start backend and API | [Start PYTHON Backend](#22-start-the-python-backend) |
| Exercise : Start backend and API | [Start CSHARP Backend](#23-start-the-csharp-backend) |
| Exercise : Start UI and demo | [Start User Interface](#3-exercise--start-the-ui) |

## 1. Prerequisites 

- **Create your Astra instance (reminders)**

[done during week 3](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud#1-create-your-astra-instance-reminders)

- **Create the Data Model (reminders)**

[done during week 3](https://github.com/DataStax-Academy/cassandra-workshop-series/tree/master/week3-AppDev-crud#2-exercise--the-spacecraft-nodebook)

## 2. Exercise : Start the backend

### 2.1 Start the JAVA BACKEND
🔥 If you intend to use the python or C# backend please jump to that section using [Start PYTHON Backend](#22-start-the-python-backend) OR [Start CSHARP Backend](#23-start-the-csharp-backend). **You only need ONE backend at a time**. 🔥

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-java.png)

This provides an example REST backend built in Java using `Spring Boot`. This application serves as the connection between the UI website and an underlying Astra database. It has Swagger installed.

Creation of `CqlSession` objects within an application is an expensive process as they take awhile to initialize and become aware of the clusters topology.  Due to this it is a best practice to create a `CqlSession` object once per application and reuse it throughout the entire lifetime of that application.

`SessionManager` implements the Singleton pattern to handle a single instance of the `CqlSession` object. Note that this is the only bean not initialized by Spring at startup. This is only when you have provided the secure credential bundle that we can initiate a connection.

#### 2.1.1 Run the Java Backend in gitpod
```diff 
@@ ONLINE in GITPOD @@ 
```

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

**👁️ Expected output**

```
   ___        _                
  / _ \      | |               
 / /_\ \ ___ | |_  _ __   __ _ 
 |  _  |/ __|| __|| '__| / _` |
 | | | |\__ \| |_ | |   | (_| |
 \_| |_/|___/ \__||_|    \__,_|                         
 
 Getting Started with Astra


08:53:58.334 INFO  com.datastax.astra.GettingStartedWithAstra    : Starting GettingStartedWithAstra on ws-5cea4c69-c849-40fe-9b27-0b6673a93e90 with PID 3322 (/workspace/cassandra-workshop-series/week4-AppDev-api/getting-started-with-astra-java/target/classes started by gitpod in /workspace/cassandra-workshop-series/week4-AppDev-api/getting-started-with-astra-java)
08:53:58.337 INFO  com.datastax.astra.GettingStartedWithAstra    : No active profile set, falling back to default profiles: default
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.codehaus.groovy.vmplugin.v7.Java7$1 (file:/workspace/m2-repository/org/codehaus/groovy/groovy/2.5.7/groovy-2.5.7-indy.jar) to constructor java.lang.invoke.MethodHandles$Lookup(java.lang.Class,int)
WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.vmplugin.v7.Java7$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
08:54:02.282 INFO  com.datastax.astra.GettingStartedWithAstra    : Started GettingStartedWithAstra in 4.58 seconds (JVM running for 14.049)
```

![Splash](images/popup-open.png?raw=true)

Click the "Open Browser" button and GitPod will generate a URL that you should be able to access to the UI with. Note that the `8080` port has been redirected to port `80` but changing the URL with a `8080` prefix and enforcing HTTPS. With the UI (swagger) you can start executing some requests against the REST API. Just paste "/swagger-ui.html" to the end of YOUR generated URL.

```
https://8080-<your_uid>.<your_region>.gitpod.io/swagger-ui.html
```

![rest-api](images/rest-api.png?raw=true)

```diff 
@@ ONLINE in GITPOD END @@ 
```

#### 2.1.2 Run the java Backend locally
```diff
! LOCAL on YOUR MACHINE
```

**✅ Prerequisites**

* Java 11+
* An Astra compatible Java driver, instructions may be found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudConnectJavaDriver.html) to install this locally.
* An Astra database with the CQL schema located in [schema.cql](getting-started-with-astra-java/src/main/resources/schema.cql) already added.
* The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudObtainingCredentials.html).

**✅ Start SpringBoot**

This application is a Spring Boot web application. This sample can be run from the root directory using:
```
cd week4-AppDev-api/getting-started-with-astra-java
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

```diff
! LOCAL on YOUR MACHINE END
```

### 2.2 Start the PYTHON BACKEND
🔥 If you intend to use the Java or C# backend please jump to that section using [Start JAVA Backend](#21-start-the-java-backend) OR [Start CSHARP Backend](#23-start-the-csharp-backend). **You only need ONE backend at a time**. 🔥

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

### 2.2.1 Run the Python Backend locally
```diff
! LOCAL on YOUR MACHINE
```

**✅ Prerequisites**

* Python 3.6 or newer
* An Astra compactible python driver, instructions may be found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudConnectPythonDriver.html)
* An Astra database with the CQL schema located in [schema.cql](getting-started-with-astra-python/schema.cql) already added.
* The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudObtainingCredentials.html).

This appliation uses the Flask framework. This sample can be run from the root directory.

**✅ Installation procedure**

- Setup environment

We advise that you set up a virtual python environment with Python 3 first (e.g with pyenv or venv).
To setup a virtual environment with [venv](https://docs.python.org/3/library/venv.html)

```
python3 -m venv astra-venv
cd astra-venv
source bin/activate
```
The activation will prepend your command prompt with `(astra-venv)`, indicating that you are operating in your virtual environment.  All dependencies will be installed into this environment only.

- Install dependencies
```
pip install Flask flask-cors cassandra-driver
```

- Clone this repository
```
git clone https://github.com/DataStax-Academy/cassandra-workshop-series
```

**✅ Starting procedure**

Go to the directory that you just cloned this repo into
```
cd cassandra-workshop-series/week4-AppDev-api/getting-started-with-astra-python
python getting_started_with_astra.py
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

- Access the API documentation from a browser

[http://127.0.0.1:5000](http://127.0.0.1:5000)

It's open, but there is nothing to see:

```
Not Found
The requested URL was not found on the server. If you entered the URL manually please check your spelling and try again.
```

This is **OK** and is **expected**. What's important is that our API is ready to go for the front end UI in the next section.

*Note: If you want to change the listening port of the application, change it in the app.run() call in `getting-started-with-astra.py`, eg*

```
app.run(port=5001)
```
```diff
! LOCAL on YOUR MACHINE END
```

#### 2.2.2 Run in gitpod
```diff
@@ ONLINE in GITPOD @@
```

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

**✅ Installation procedure**

```
pip install Flask flask-cors cassandra-driver
```

**✅ Starting the Flask application procedure**

Move to folder ` week4-AppDev-api` to get the exercises of the week and `getting-started-with-astra-python` to work with PYTHON.

```
cd week4-AppDev-api/getting-started-with-astra-python
python getting_started_with_astra.py
```

![Splash](images/popup-open.png?raw=true)

Click the "Open Browser" button and GitPod will generate a URL that you should be able to access to the UI with.

```
https://5000-<your_uid>.<your_region>.gitpod.io
```

It's open, but there is nothing to see:

```
Nothing to see here... 🦗
Port 5000 didn't respond
Please make sure this port is exposed and your app is running.
```

This is **OK** and is **expected**. What's important is that our API is ready to go for the front end UI in the next section.

You will see an `Open Ports` tab in the gitpod terminal window, where you can check on the status of the ports and access previews and browsers. 


*Note: If you want to change the listening port of the application, change it in the app.run() call in `getting-started-with-astra.py`, eg*

```
app.run(port=5001)
```

```diff 
@@ ONLINE in GITPOD END @@ 
```

### 2.3 Start the CSHARP BACKEND
🔥 If you intend to use the Java or python backend please jump to that section using [Start JAVA Backend](#21-start-the-java-backend) OR [Start PYTHON Backend](#22-start-the-python-backend). **You only need ONE backend at a time**. 🔥

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-csharp.png)

**Objectives**
- How to connect to Astra via the Secure Connect Bundle
- How to manage a Cassandra Session within a .NET web application

**Project Layout**

This sample also contains several interesting files that worth noting specifically:

* [Services/AstraService.cs](getting-started-with-astra-csharp/Services/AstraService.cs) - This file contains all the logic for connecting to the Astra database using the secure connect bundle.
* [Startup.cs](getting-started-with-astra-csharp/Startup.cs) - This file contains the logic for adding a singleton for the Session object for reuse across our application.

See [here](https://docs.datastax.com/en/devapp/doc/devapp/driversBestPractices.html#Useasinglesessionobjectperapplication) for additional details on how the session object in Cassandra works and why it is best practice to only have a single Session object per application

* [schema.cql](getting-started-with-astra-csharp/schema.cql) - The database schema required for the Astra keyspace
  
* [Controllers/InstrumentController.cs](getting-started-with-astra-csharp/Controllers/InstrumentsController.cs) - If you would like to see how to implement paging in C# then this would be the place to look.  Paging in Cassandra is different than what you are likely used to so it is beneficial to read [this](https://docs.datastax.com/en/devapp/doc/devapp/driversResultPaging.html) article describing how paging works with Cassandra. 

**How this Sample Works**

This is am example .NET Core Web API backend for use with the Astra Getting Started UI which is found [here](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

This application serves as the connection between the UI website and an underlying Astra database.  

It has Swagger installed so once it is running you can look at the Swagger UI here:

```http://localhost:5000/swagger/index.html#/```

**Connecting to Astra with a Secure Connect Bundle**

To see how to connect to Astra using the Secure Connect Bundle you can look at the `ConnectToAstra()` method in [Services/AstraService.cs](Services/AstraService.cs).  In this method you will find the code which:

1. Creates a `Cluster` instance using the builder.
   
    ```var session = Cluster.Builder()```

2. Specifies the local file path to the Secure Connect Bundle ZIP file that has been downloaded from your Astra Database.  
   
   ```.WithCloudSecureConnectionBundle(secureConnectBundlePath)```
3. Set the username and password for your Astra Database

    ```.WithCredentials(username, password)```
4. Set the default Consistency Level to `LOCAL_QUORUM`.  `LOCAL_QUORUM` is the only supported consistency level for Astra queries.
   
   ```.WithQueryOptions(new QueryOptions().SetConsistencyLevel(ConsistencyLevel.LocalQuorum))```
5.  Build the `Cluster` object then connect to your Astra database specifying the keyspace to use.

    ```.Build().Connect(keyspace);```

Once you have completed all these steps you will now have a fully configured, connected, and ready to run CQL queries. 

**Managing Cassandra Session Within a .NET Web Application**

Creation of `Session` objects within an application is an expensive process as they take awhile to initialize and become aware of the clusters topology.  Due to this it is a best practice to create a `Session` object once per application and reuse it throughout the entire lifetime of that application.  When building an ASP.NET Core application as shown this is easily supported through the use of singleton and .NET Core's built in Dependency Injection mechanisms.  

For our Web API endpoints we created a singleton instance of our `AstraService` object inside the `ConfigureServices()` method in [Startup.cs](getting-started-with-astra-csharp/Startup.cs) using the following code:

`services.AddSingleton(typeof(Interfaces.IDataStaxService), typeof(Services.AstraService));`

This code specifies that we are adding a singleton instance of `AstraService` for any dependency requiring an object instantiating the `Interfaces.IDataStaxService` interface.

To use this within our controllers, we need to specify a property on the constructor of the controller that requires the `Interfaces.IDataStaxService` interface as shown below.

`        
public CredentialsController(IDataStaxService service)
        {
            Service = service;
        }
`

#### 2.3.1 Run locally
```diff
! LOCAL on YOUR MACHINE
```

With each call to the `CredentialsController` the `AstraService` singleton we created at startup will be passed to the constructor.  This mechanism of dependency injection allows us a simple mechanism to use a single `Session` object throughout the entirety of the application lifecycle.

**✅ Installation procedure**


-  .NET Core 2.1
-  An Astra compatible C# driver, instructions may be found [here](https://helpdocs.datastax.com/aws/dscloud/astra/dscloudConnectCsharpDriver.html) to install this locally.
-  An Astra database with the CQL schema located in [schema.cql](schema.cql) already added.
-  The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://helpdocs.datastax.com/aws/dscloud/astra/dscloudObtainingCredentials.html).


**✅ Starting procedure**

This application is a .NET 2.1 web application configured to serve it's web application via the Kestrel web server.  This sample can be run from the root directory using:

```dotnet run```

This will startup the application running on `http://localhost:5000`

You will know that you are up and working when you get the following in your terminal window:

```
Hosting environment: Development
Content root path: /Users/dave.bechberger/Documents/projects/bechbd/getting-started-with-astra-csharp
Now listening on: http://localhost:5000
Application started. Press Ctrl+C to shut down.
```
```diff
! LOCAL on YOUR MACHINE END
```

#### 2.3.2 Run in gitpod

*N/A as of now, we did not setup gitpod to work with dotnet.*


## 3. Exercise : Start the UI

🔥 This provides the front end for the Astra Getting Started application. This is meant to be used with one of the following backends (java, python, csharp)🔥 

**Objectives**
- Connect a web application to one of four backends to demonstrate an end to end full stack application
- Demonstrate how to properly page results for an Astra Service

![UI](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/week4-AppDev-api/images/ui.png)

### 3.1 Run locally
```diff
! LOCAL on YOUR MACHINE
```

**Prerequisites**

- node v8.1.0

**Running**

A `.env` file is required before you can build the application. This file needs to be added to the root of the project and called .env. This file contains all the environment variables required for the application. Currently there is only one required entry:

This value is where you put the relative address of the api.

- Java

```
BASE_ADDRESS=http://localhost:8080/api
```

- Python

```
BASE_ADDRESS=http://localhost:5000/api
```

- CSharp

```
BASE_ADDRESS=http://localhost:5001/api
```

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
[http://127.0.0.1:3000](http://127.0.0.1:3000)

If you would like to build this for deployment then this can be accomplished using:

```
npm run build
```
```diff
! LOCAL on YOUR MACHINE END
```

## 3.2 Run in gitpod
```diff
@@ ONLINE in GITPOD @@
```

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

- <your_port>: Will be `8080` is you pick the Java Backend, `5000` if you pick the python backend and `5001` if you pick the CSharp backend.

```
npm run start
```

Your should be able to access
```
https://3000-<your_uid>.<>your_region.gitpod.io/
```

```diff 
@@ ONLINE in GITPOD END@@ 
```

[🏠 Back to Table of Content](#table-of-content)

[🏠Back to HOME workshop](https://github.com/DataStax-Academy/cassandra-workshop-series)

THE END.


