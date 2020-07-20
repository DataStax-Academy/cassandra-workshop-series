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
| 2. Exercise : Start backend and API | [Connectivity](#3-start-the-backend) |
| 2. Exercise : Start UI and demo | [Connectivity](#3-start-the-backend) |

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

[🏠 Back to Table of Content](#table-of-content)

### 3.1 Start the JAVA BACKEND

This provides an example REST backend built in Java using `Spring Boot` for use with the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

This is an example of a Spring Boot Microservice for use with the Astra Getting Started UI which is found [here](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

This application serves as the connection between the UI website and an underlying Astra database. It has Swagger installed.

Creation of `CqlSession` objects within an application is an expensive process as they take awhile to initialize and become aware of the clusters topology.  Due to this it is a best practice to create a `CqlSession` object once per application and reuse it throughout the entire lifetime of that application.

`SessionManager` implements the Singleton pattern to handle a single instance of the `CqlSession` object. Note that this is the only bean not initialized by Spring at startup. This is only when you have provided the secure credential bundle that we can initiate a connection.

### 3.1.1 Run locally

#### Prerequisites
* Java 11+
* An Astra compatible Java driver, instructions may be found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudConnectJavaDriver.html) to install this locally.
* An Astra database with the CQL schema located in [schema.cql](src/main/resources/schema.cql) already added.
* The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudObtainingCredentials.html).

**✅ Start locally**

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

**✅ Access the API documentation from a browser**

[http://localhost:8080](http://localhost:8080)

*Note: If you want to change the listening port of the application, locate the file `src/main/resources/application.yml` and change key `server.port`*

### 3.1.2 Run in gitpod

### 3.2 Start the NODE BACKEND
### 3.2.1 Run locally
### 3.2.2 Run in gitpod

### 3.3 Start the PYTHON BACKEND
#### 3.3.1 Run locally
#### 3.3.2 Run in gitpxod

### 3.4 Start the CSHARP BACKEND
#### 3.4.1 Run locally
#### 3.4.2 Run in gitpod



## 2. Exercise : Start the UI

[🏠 Back to Table of Content](#table-of-content)

[🏠Back to HOME workshop](https://github.com/DataStax-Academy/cassandra-workshop-series)

THE END.


