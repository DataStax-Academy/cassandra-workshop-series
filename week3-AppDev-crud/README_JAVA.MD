
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# Application Development with JAVA EXERCISES

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-java.png)

[🏠 GO BACK TO MAIN](./README.MD)

| Exercises |
|---|
| [Connectivity to Cassandra](#3-connectivity-to-cassandra) |
| [Create Schema](#4-create-read-update) |
| [Connectivity](#5-read-and-parse-results) |


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

**👁️ Expected output**

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

**✅ Step 4a. Run unit test `Ex03_Query5a_Insert_Journey`** to insert a journey in the catalog

```bash
mvn test -Dtest=com.datastax.workshop.Ex03_Query5a_Insert_Journey
```

**👁️ Expected output**

```bash
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

**✅ Step 4b. Run unit test `Ex04_Query5b_TakeOff`** to update the journey and marked started.

In the previous exercises we displayed a journeyID (for me it is `53983d90-c5da-11ea-bcd8-c78bf1f1af98`). Open `Ex04_Query5b_TakeOff` locate constant `JOURNEY_ID` and change value with the proper journeyID. 

Run the takeoff query by running this test

```bash
mvn test -Dtest=com.datastax.workshop.Ex04_Query5b_TakeOff
```

**👁️ Expected output**

```bash
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

**✅ Step 4c. Run unit test `Ex05_Query5c_Travel`** to simulate a travel in space


```bash
mvn test -Dtest=com.datastax.workshop.Ex05_Query5c_Travel
```

```bash
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

**✅ Step 4d. Run unit test `Ex06_Query5d_Landing`** update the journey to make it inactive and provide end date. To run the test please execute:

```bash
mvn test -Dtest=com.datastax.workshop.Ex06_Query5d_Landing
```

**👁️ Expected output**

```bash
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

## 5. READ AND PARSE RESULTS

**✅ Step 5a. Run unit test `Ex07_Query4a_ListJourneys`** to list all journeys for a 
spacecraft.

```bash
mvn test -Dtest=com.datastax.workshop.Ex07_Query4a_ListJourneys
```

**👁️ Expected output**

```bash
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

**✅ Step 5b. Run unit test `Ex08_Query4b_Read_Journey_Details`** to get the details of one journey

```bash
mvn test -Dtest=com.datastax.workshop.Ex08_Query4b_Read_Journey_Details
```

**👁️ Expected output**

```bash
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

**✅ Step 5c. Run unit test `Ex09_Query4c_ReadMetrics`** to get ALL readings (speed) from one of the journey

```bash
mvn test -Dtest=com.datastax.workshop.Ex09_Query4c_ReadMetrics
```

**👁️ Expected output**

```bash
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

**✅ Step 5d. Run unit test `Ex10_Query4c_ReadMetrics_Paging`** to get readings (speed) from one of the journey but page by page.

```bash
mvn test -Dtest=com.datastax.workshop.Ex10_Query4c_ReadMetrics_Paging
```

**👁️ Expected output**

```bash
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

You are all done congratulations !






