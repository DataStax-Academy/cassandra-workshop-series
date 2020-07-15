
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# Application Development with JAVASCRIPT EXERCISES

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-javascript.png)

[🏠 GO BACK TO MAIN](./README.MD)

| Exercises |
|---|
| [Connectivity to Cassandra](#3-connectivity-to-cassandra) |
| [Create Schema](#4-create-read-update) |
| [Connectivity](#5-read-and-parse-results) |


## 3. Connectivity to Cassandra 

**✅ Step 3a. Fix unit test `Ex02_Connect_to_Cassandra.js`**

- Locate the file `db_connection.js` and fill the properties with your Astra informations.

*Do not copy-paste - this is a sample for me.*

```javascript
// This is the Zip file you downloaded
const SECURE_CONNECT_BUNDLE = '/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip'
// This is the username, recommended value was KVUser
const USERNAME = "KVUser";
// This is the password, recommended value was KVPassword
const PASSWORD = "KVPassword";
// This is the keyspace name, recommended value was killrvideo
const KEYSPACE = "killrvideo"; 
```

```
⚠️ By default Autosaved is not enabled in Gitpod. Don't forget to save your modifications with CTRL+S
```
```bash
node Ex02_Connect_to_Cassandra.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
Your are now connected to cluster 'caas-cluster'
SUCCESS
========================================
```

## 4. CREATE READ UPDATE

**✅ Step 4a. Run unit test `Ex03_Query5a_Insert_Journey.js`** to insert a journey in the catalog


```bash
node Ex03_Query5a_Insert_Journey.js 
```

```bash
========================================
Start exercise
Journey created : 76dceda0-c5ee-11ea-a96c-cbbdcb5d7db1
SUCCESS
========================================
```

**✅ Step 4b. Run unit test `Ex04_Query5b_TakeOff.js`** to update the journey and marked started.

In the previous exercises we displayed a journeyID (for me it is `76dceda0-c5ee-11ea-a96c-cbbdcb5d7db1`). Open `Ex04_Query5b_TakeOff.js` locate constant `JOURNEY_ID` and change value with the proper journeyID. 

Run the takeoff query by running this test

```bash
node Ex04_Query5b_TakeOff.js 
```

**👁️ Expected output**

```bash
TODO
```

**✅ Step 4c. Run unit test `Ex05_Query5c_Travel.js`** to simulate a travel in space


```bash
node Ex05_Query5c_Travel.js 
```

**👁️ Expected output**

```bash
TODO
```

**✅ Step 4d. Run unit test `Ex06_Query5d_Landing.js`** update the journey to make it inactive and provide end date. To run the test please execute:

```bash
node Ex06_Query5d_Landing.js 
```

**👁️ Expected output**

```bash
TODO
```

## 5. READ AND PARSE RESULTS

**✅ Step 5a. Run unit test `Ex07_Query4a_ListJourneys.js`** to list all journeys for a 
spacecraft.

```bash
node Ex07_Query4a_ListJourneys.js 
```

**👁️ Expected output**

```bash
TODO
```

**✅ Step 5b. Run unit test `Ex08_Query4b_Read_Journey_Details.js`** to get the details of one journey

```bash
node Ex08_Query4b_Read_Journey_Details.js 
```

**👁️ Expected output**

```bash
TODO
```

**✅ Step 5c. Run unit test `Ex09_Query4c_ReadMetrics.js`** to get ALL readings (speed) from one of the journey

```bash
node Ex09_Query4c_ReadMetrics.js 
```

**👁️ Expected output**

```bash
TODO
```

**✅ Step 5d. Run unit test `Ex10_Query4c_ReadMetrics_Paging.js`** to get readings (speed) from one of the journey but page by page.

```bash
node Ex10_Query4c_ReadMetrics_Paging.js 
```

**👁️ Expected output**

```bash
TODO
```

You are all done congratulations !






