# Getting Started with Apache Cassandra™ and Java using DataStax Astra

```
  ___        _
 / _ \      | |
/ /_\ \ ___ | |_  _ __   __ _
|  _  |/ __|| __|| '__| / _` |
| | | |\__ \| |_ | |   | (_| |
\_| |_/|___/ \__||_|    \__,_|
 
```

This provides an example REST backend built in Java using `Spring Boot` for use with the [Getting Started with Astra UI](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

Contributors: 
- [bechbd](https://github.com/bechbd)
- [clun](https://github.com/clun)
- [csplinter](https://github.com/csplinter)
- [msmygit](https://github.com/msmygit)

## Objectives
* How to connect to Astra via the Secure Connect Bundle
* How to manage a Cassandra Session within a JAVA web application

## How this Sample Works

This is an example of a Spring Boot Microservice for use with the Astra Getting Started UI which is found [here](https://github.com/DataStax-Examples/getting-started-with-astra-ui).

This application serves as the connection between the UI website and an underlying Astra database. It has Swagger installed so once it is running you can look at the Swagger UI here:

```http://localhost:8080/```

### Connecting to Astra with a Secure Connect Bundle

To see how to connect to Astra using the Secure Connect Bundle you can look at the `connectToAstra()` method in [SessionManager](src/main/java/com/datastax/astra/dao/SessionManager.java).  In this method you will find the code which:

1. Creates a `Cluster` instance using the builder.
   
   ```CqlSession cqlSession = CqlSession.builder()```

2. Specifies the local file path to the Secure Connect Bundle ZIP file that has been downloaded from your Astra Database.
   
   ```.withCloudSecureConnectBundle(Paths.get(getSecureConnectionBundlePath()))```
3. Set the username and password for your Astra Database

   ```.withAuthCredentials(getUserName(),getPassword())```
  
4.  Build the `Cluster` object then connect to your Astra database specifying the keyspace to use.

    ```.withKeyspace(getKeySpace()).build()```

Once you have completed all these steps you will now have a fully configured, connected, and ready to run CQL queries. 

### Managing Cassandra Session Within a Java Web Application

Creation of `CqlSession` objects within an application is an expensive process as they take awhile to initialize and become aware of the clusters topology.  Due to this it is a best practice to create a `CqlSession` object once per application and reuse it throughout the entire lifetime of that application.

`SessionManager` implements the Singleton pattern to handle a single instance of the `CqlSession` object. Note that this is the only bean not initialized by Spring at startup. This is only when you have provided the secure credential bundle that we can initiate a connection.


## Setup and Running

### Prerequisites

* Java 11+
* An Astra compatible Java driver, instructions may be found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudConnectJavaDriver.html) to install this locally.
* An Astra database with the CQL schema located in [schema.cql](src/main/resources/schema.cql) already added.
* The username, password, keyspace name, and secure connect bundle downloaded from your Astra Database.  For information on how to obtain these credentials please read the documentation found [here](https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudObtainingCredentials.html).

### Running

This application is a Spring Boot web application. This sample can be run from the root directory using:

#### a) Clone this repository

```
git clone https://github.com/DataStax-Examples/getting-started-with-astra-java.git
```

#### b) compile and start the backend *(maven 3.6+ and java11+ required)*

```
cd getting-started-with-astra-java

mvn spring-boot:run
```

This will startup the application running on `http://localhost:8080`

You will know that you are up and working when you get the following in your terminal window:

```
16:23:01.569 INFO  com.datastax.astra.GettingStartedWithAstra  : Started GettingStartedWithAstra in 1.851 seconds (JVM running for 2.39)
```

#### c) Access the API documentation from a browser

[http://localhost:8080](http://localhost:8080)

*Note: If you want to change the listening port of the application, locate the file `src/main/resources/application.yml` and change key `server.port`*

#### d) Setup the user interface to use this backend

To setup the UI to connect to Java backend define a `.env` file in the `getting-started-with-astra-ui` project main directory. Inside the file it should have one entry pointing to this project's API endpoint:

```
BASE_ADDRESS=http://localhost:8080/api
```
Once you start that project with a `npm run build` it will point the UI to the backend API which will then be using Astra as a database. When you first connect to the UI, a dialog box will open asking for Astra connection information. 

# II - Building the docker image

First, you need to build the project and specially the `jar` deliverable with the following:
```
mvn clean install
```

Then build the images using the following command in same folder as `pom.xml` file:
```
mvn dockerfile:build 
```

To run the project in docker and expose correct port use the following:
```
docker run clunven/getting-started-with-astra-java:latest -p 8080:8080
```
