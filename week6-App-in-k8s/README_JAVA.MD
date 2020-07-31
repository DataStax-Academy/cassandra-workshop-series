## Java

The java backend application `getting-started-with-astra` has been updated to be able to connect eith to `ASTRA` or any Cassandra installation providing the `contacts points`.

- If you don't provide any environment variables or simple put `USE_ASTRA` to `true` we will remain with the default behaviour : we expect the UI to POST all credentials informations including the secure connect bundle.
- If you want to connect to local instance the following properties are reqired

```ini
USE_ASTRA : true or false (default is true)
CONNECTION_POINTS: list of points `127.0.0.1:9042,127.0.0.2:9042` (default is localhost:9042)
USERNAME: username (default is KVUser)
PASSWORD: password (default is KVPassword)
KEYSPACE: keyspace name (default is killrvideo)
LOCAL_DATACENTER: data center name (default is datacenter1)
```

## Using Backend with ASTRA

If not information is provided we will use the default value = ASTRA
```bash
mvn spring-boot:run
```

📗 **Expected output**
```
[...]
14:02:10.847 INFO  com.datastax.astra.GettingStartedWithAstra    : Environment variable 'USE_ASTRA' not found defaulting to 'true'
```

The application expects some environment variables like:
```bash
export USE_ASTRA=true
mvn spring-boot:run
```

📗 **Expected output**
```
[...]
14:01:03.576 INFO  com.datastax.astra.GettingStartedWithAstra    : Environment variable 'USE_ASTRA' has been read as 'true'
```

Now if you try to connect you need to provide secure connect bundle as in week 4
```
curl -X GET "http://localhost:8080/api/credentials" -H "accept: */*"
```

📗 **Expected output**
```json
{ 
  "timestamp":"2020-07-31T12:06:25.539+0000",
  "status":404,"error":"Not Found",
  "message":"java.lang.IllegalStateException: Please initialize the connection parameters first with saveCredentials(...)",
  "path":"/api/credentials"
```

## Using Backend with Local Instance

📘 **Start a Cassandra instance in docker**

- Go to the folder `src/main/resources` and start a Cassandra container with
```
docker-compose up -d
```

- Check launch with and wait until the container is ready
```
docker ps
```

- After a minute you can now enter the container and execute a `cqlsh`
```
docker exec -it `docker ps | grep cassandra:3.11.7 | cut -b 1-12` cqlsh
```

- Enter the following `CQL` statement to create a basic `killrvideo` keyspace
```
CREATE KEYSPACE killrvideo WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
```

📘 **Start the application to connect to Docker**

- Stop the backend application if running and enter the following
```bash
export USE_ASTRA=false
export KEYSPACE=killrvideo
export LOCAL_DATACENTER=datacenter1
export CONNECTION_POINTS=localhost:9042
mvn spring-boot:run
```

This time in the output you get
```
14:15:15.607 INFO  com.datastax.astra.GettingStartedWithAstra    : Environment variable 'USE_ASTRA' has been read as 'false'
```

Now if you try to connect 
```
curl -X GET "http://localhost:8080/api/credentials" -H "accept: */*"
```

You got 
```
Connection Successful
```

and if you look at the log of your application you see that env variables has been used.
```bash
13:40:19.292 INFO : Allowing CORS
13:40:20.218 INFO : Started GettingStartedWithAstra in 3.591 seconds (JVM running for 9.375)
13:40:20.220 INFO : Environment variable 'USE_ASTRA' has been read as 'false'
13:50:43.241 INFO : Initializing connection using Environment Variables (local)
13:50:43.243 INFO : + Username: environment variable 'USERNAME' not found defaulting to 'KVUser'
13:50:43.243 INFO : + Password: environment variable 'PASSWORD' not found defaulting to 'KVPassword'
13:50:43.243 INFO : + Keyspace: environment variable 'KEYSPACE' has been read as killrvideo
13:50:43.243 INFO : + [local] localdataCenter: environment variable 'LOCAL_DATACENTER' has been read datacenter1
13:50:43.243 INFO : + [local] contactPoints: environment variable 'CONNECTION_POINTS' has been read as 'localhost:9042'
13:50:45.493 INFO : + Connected to CASSANDRA
13:50:45.527 INFO : Session is successfully initialized and connected
```

## Package your application as a docker image

Check the `DockerFile` file close at root of project this how you package a Spring-Boot application
```bash
FROM openjdk:11
MAINTAINER Cedrick Lunven <cedrick.lunven@datastax.com>
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8080
```

In the `pom.xml` we have define docker plugin to help us build the image
```xml
<plugin>
  <groupId>com.spotify</groupId>
  <artifactId>dockerfile-maven-plugin</artifactId>
  <version>${version.maven.plugin.docker}</version>
  <configuration>
  <repository>java-backend</repository>
  <buildArgs>
   <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
  </buildArgs>
 </configuration>
</plugin>
```

To build the image
```
mvn clean package dockerfile:build
```

Output
```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------< com.datastax.astra:getting-started-with-astra-java >---------
[INFO] Building getting-started-with-astra-java 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[WARNING] The artifact org.apache.commons:commons-io:jar:1.3.2 has been relocated to commons-io:commons-io:jar:1.3.2
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ getting-started-with-astra-java ---
[INFO] Deleting /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java/target
[INFO] 
[INFO] --- maven-resources-plugin:3.1.0:resources (default-resources) @ getting-started-with-astra-java ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO] Copying 5 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ getting-started-with-astra-java ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 23 source files to /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java/target/classes
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
[INFO] 
[INFO] --- maven-resources-plugin:3.1.0:testResources (default-testResources) @ getting-started-with-astra-java ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ getting-started-with-astra-java ---
[INFO] No sources to compile
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ getting-started-with-astra-java ---
[INFO] No tests to run.
[INFO] 
[INFO] --- maven-jar-plugin:3.1.2:jar (default-jar) @ getting-started-with-astra-java ---
[INFO] Building jar: /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java/target/getting-started-with-astra-java-1.0.0-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.1.9.RELEASE:repackage (repackage) @ getting-started-with-astra-java ---
[INFO] Replacing main artifact with repackaged archive
[INFO] 
[INFO] --- dockerfile-maven-plugin:1.4.12:build (default-cli) @ getting-started-with-astra-java ---
[INFO] dockerfile: null
[INFO] contextDirectory: /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java
[INFO] Building Docker context /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java
[INFO] Path(dockerfile): null
[INFO] Path(contextDirectory): /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java
[INFO] 
[INFO] Image will be built as java-backend:latest
[INFO] 
[INFO] Step 1/7 : FROM openjdk:11
[INFO] 
[INFO] Pulling from library/openjdk
[INFO] Digest: sha256:1ebc09a5971cc8b875592c4b4d39f319c2828ae0489ae80aae800503e63ac72a
[INFO] Status: Image is up to date for openjdk:11
[INFO]  ---> 3a7aa4036e82
[INFO] Step 2/7 : MAINTAINER Cedrick Lunven <cedrick.lunven@datastax.com>
[INFO] 
[INFO]  ---> Using cache
[INFO]  ---> 587e8c276951
[INFO] Step 3/7 : VOLUME /tmp
[INFO] 
[INFO]  ---> Using cache
[INFO]  ---> 242d7fadb310
[INFO] Step 4/7 : ARG JAR_FILE
[INFO] 
[INFO]  ---> Using cache
[INFO]  ---> e75895dd13c8
[INFO] Step 5/7 : ADD ${JAR_FILE} app.jar
[INFO] 
[INFO]  ---> c23b210cecf5
[INFO] Step 6/7 : ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
[INFO] 
[INFO]  ---> Running in 18eee6948e8f
[INFO] Removing intermediate container 18eee6948e8f
[INFO]  ---> 8c67ac50c895
[INFO] Step 7/7 : EXPOSE 8080
[INFO] 
[INFO]  ---> Running in 0dad20ac8bd1
[INFO] Removing intermediate container 0dad20ac8bd1
[INFO]  ---> 3175d57a181d
[INFO] Successfully built 3175d57a181d
[INFO] Successfully tagged java-backend:latest
[INFO] 
[INFO] Detected build of image with id 3175d57a181d
[INFO] Building jar: /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java/target/getting-started-with-astra-java-1.0.0-SNAPSHOT-docker-info.jar
[INFO] Successfully built java-backend:latest
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  18.101 s
[INFO] Finished at: 2020-07-31T14:42:54+02:00
[INFO] ------------------------------------------------------------------------
```

The image is ready you can list it
```
docker images | grep java-backend
```

output
```
java-backend  latest 323a40f42835 5 minutes ago 680MB
```

Let's run it
```
docker run java-backend
```

output
```
cedricklunven@clunhost:~/dev/WORKSPACES/cassandra-workshop-series/week6-App-in-k8s/getting-started-with-astra-java> docker run java-backend
   ___        _                
  / _ \      | |               
 / /_\ \ ___ | |_  _ __   __ _ 
 |  _  |/ __|| __|| '__| / _` |
 | | | |\__ \| |_ | |   | (_| |
 \_| |_/|___/ \__||_|    \__,_|                         
 
 Getting Started with Astra


12:45:08.224 INFO  com.datastax.astra.GettingStartedWithAstra    : Starting GettingStartedWithAstra v1.0.0-SNAPSHOT on 6b33b55017f2 with PID 1 (/app.jar started by root in /)
12:45:08.228 INFO  com.datastax.astra.GettingStartedWithAstra    : No active profile set, falling back to default profiles: default
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.codehaus.groovy.vmplugin.v7.Java7$1 (jar:file:/app.jar!/BOOT-INF/lib/groovy-2.5.7-indy.jar!/) to constructor java.lang.invoke.MethodHandles$Lookup(java.lang.Class,int)
WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.vmplugin.v7.Java7$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
12:45:11.039 INFO  com.datastax.astra.controller.CorsController  : Allowing CORS
12:45:12.344 INFO  com.datastax.astra.GettingStartedWithAstra    : Started GettingStartedWithAstra in 4.926 seconds (JVM running for 5.644)
12:45:12.348 INFO  com.datastax.astra.GettingStartedWithAstra    : Environment variable 'USE_ASTRA' not found defaulting to 'true'
```

Ok we are set what about some environment variables ?
```
docker run java-backend -e USE_ASTRA='false' \
    -e KEYSPACE='killrvideo' \
    -e LOCAL_DATACENTER='datacenter1' \
    -e CONNECTION_POINTS='locahost:9042'
```

ERROR








