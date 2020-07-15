
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

- Navigate to the _crud-nodejs_ folder within the _week3-AppDev-crud_ folder using the terminal with the command below. Install the dependencies.

```
cd week3-AppDev-crud/crud-nodejs/

npm install
```

- Locate the file `db_connection.js` and fill the properties with your Astra informations.

*Do not copy-paste - this is a sample for me.*

```javascript
// This is the Zip file you downloaded
const SECURE_CONNECT_BUNDLE = '/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip'
//const SECURE_CONNECT_BUNDLE = '/workspace/cassandra-workshop-series/week3-AppDev-crud/secure-connect-devworkshopdb.zip'
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

You can now execute the code to connect to ASTRA with. 

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

```javascript
const journey_id = TimeUuid.fromString('<insert_your_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```

Run the takeoff query by running this test

```bash
node Ex04_Query5b_TakeOff.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
9..8..7..6..5..4..3..2..1 Ignition
========================================
Journey 84121060-c66e-11ea-a82e-f931183227ac has now taken off
SUCCESS
```

**✅ Step 4c. Run unit test `Ex05_Query5c_Travel.js`** to simulate a travel in space

Open the file and locate constant `journey_id` and change value with the proper journeyID. 

```javascript
const journey_id = TimeUuid.fromString('<insert_your_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```

Run the sample

```bash
node Ex05_Query5c_Travel.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
{0/50}Travelling..
{1/50}Travelling..
{2/50}Travelling..
{3/50}Travelling..
{4/50}Travelling..
{5/50}Travelling..
{6/50}Travelling..
...
{47/50} Travelling..
{48/50} Travelling..
{49/50} Travelling..
Reading saved for journey 84121060-c66e-11ea-a82e-f931183227ac
========================================
```

**✅ Step 4d. Run unit test `Ex06_Query5d_Landing.js`** update the journey to make it inactive and provide end date. To run the test please execute:

Open the file and locate constant `journey_id` and change value with the proper journeyID. 

```javascript
const journey_id = TimeUuid.fromString('<insert_your_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```
Now run the sample:

```bash
node Ex06_Query5d_Landing.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
========================================
Journey 84121060-c66e-11ea-a82e-f931183227ac has now landed
SUCCESS
```

## 5. READ AND PARSE RESULTS

**✅ Step 5a. Run unit test `Ex07_Query4a_ListJourneys.js`** to list all journeys for a 
spacecraft.

Open the file and locate constant `spacecraft_name` and change value with the proper value you entered. 

```javascript
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```
Now run the sample:

```bash
node Ex07_Query4a_ListJourneys.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
========================================
- Journey: 620acae0-c671-11ea-97e7-56004218e25e Summary: Bring Astronauts to ISS
- Journey: 87345f00-c66e-11ea-a252-4ebb2901d808 Summary: Bring Astronauts to ISS
- Journey: 84121060-c66e-11ea-a82e-f931183227ac Summary: Bring Astronauts to ISS
- Journey: 76dceda0-c5ee-11ea-a96c-cbbdcb5d7db1 Summary: Bring Astronauts to ISS
- Journey: 691bcf60-c5ee-11ea-9bde-d1ddecb1d5c1 Summary: Bring Astronauts to ISS
- Journey: 53983d90-c5da-11ea-bcd8-c78bf1f1af98 Summary: Bring Astronauts to ISS
- Journey: 90163870-c5d6-11ea-b11f-c30e2b038000 Summary: Bring Astronauts to ISS
SUCCESS
```

**✅ Step 5b. Run unit test `Ex08_Query4b_Read_Journey_Details.js`** to get the details of one journey

Open the file and locate constant `journey_id` and change value with the proper journeyID. 

```javascript
const journey_id = TimeUuid.fromString('<insert_your_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```
Now run the file :

```bash
node Ex08_Query4b_Read_Journey_Details.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
========================================
Journey has been found
- Uid:		 TimeUuid {
  buffer: <Buffer 84 12 10 60 c6 6e 11 ea a8 2e f9 31 18 32 27 ac>
}
- Spacecraft:	 Crew Dragon Endeavour,SpaceX
- Summary:	 Bring Astronauts to ISS
- Active:	 false
- Takeoff:	 2020-07-15T08:03:50.810Z
- Landing:	 2020-07-15T09:07:12.619Z
SUCCESS
```

**✅ Step 5c. Run unit test `Ex09_Query4c_ReadMetrics.js`** to get ALL readings (speed) from one of the journey

Open the file and locate constant `journey_id` and change value with the proper journeyID. 

```javascript
const journey_id = TimeUuid.fromString('<insert_your_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```
Now run the file :

```bash
node Ex09_Query4c_ReadMetrics.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
========================================
idx:0, time=2020-07-15T08:23:18.989Z, value=358.84601643389595
idx:1, time=2020-07-15T08:23:18.988Z, value=330.61229400959064
idx:2, time=2020-07-15T08:23:18.987Z, value=307.32379130938597
idx:3, time=2020-07-15T08:23:18.986Z, value=309.65674426432656
idx:4, time=2020-07-15T08:22:44.887Z, value=353.7309085562105
idx:5, time=2020-07-15T08:22:44.886Z, value=342.4362157566772
idx:6, time=2020-07-15T08:22:44.885Z, value=309.7928649131023
idx:7, time=2020-07-15T08:22:44.883Z, value=305.54587412409853
idx:8, time=2020-07-15T08:22:20.094Z, value=309.11758671277005
idx:9, time=2020-07-15T08:22:20.093Z, value=303.3260737921256
idx:10, time=2020-07-15T08:21:34.485Z, value=305.01601666161747
idx:11, time=2020-07-15T08:21:34.484Z, value=307.9133038155668
idx:12, time=2020-07-15T08:20:41.268Z, value=350.7937271530607
idx:13, time=2020-07-15T08:20:41.267Z, value=312.31323735679894
idx:14, time=2020-07-15T08:20:41.266Z, value=308.34006008417475
idx:15, time=2020-07-15T08:19:35.401Z, value=351.14572664848623
idx:16, time=2020-07-15T08:19:35.400Z, value=304.01067930496845
idx:17, time=2020-07-15T08:19:35.399Z, value=309.96427715862086
idx:18, time=2020-07-15T08:15:52.226Z, value=312.62135385904617
idx:19, time=2020-07-15T08:15:52.225Z, value=304.43301464875776
idx:20, time=2020-07-15T08:10:45.736Z, value=349.81462163292764
idx:21, time=2020-07-15T08:10:45.735Z, value=312.94324025033586
idx:22, time=2020-07-15T08:10:45.734Z, value=305.9325979919639
SUCCESS
```

**✅ Step 5d. Run unit test `Ex10_Query4c_ReadMetrics_Paging.js`** to get readings (speed) from one of the journey but page by page.

Open the file and locate constant `journey_id` and change value with the proper journeyID. 

```javascript
const journey_id = TimeUuid.fromString('<insert_your_id>')
const spacecraft_name = 'Crew Dragon Endeavour,SpaceX'
```
Now run the file :

```bash
node Ex10_Query4c_ReadMetrics_Paging.js 
```

**👁️ Expected output**

```bash
========================================
Start exercise
========================================
Page1: 5 items
idx:1, time=2020-07-15T08:23:18.989Z, value=358.84601643389595
idx:2, time=2020-07-15T08:23:18.988Z, value=330.61229400959064
idx:3, time=2020-07-15T08:23:18.987Z, value=307.32379130938597
idx:4, time=2020-07-15T08:23:18.986Z, value=309.65674426432656
idx:5, time=2020-07-15T08:22:44.887Z, value=353.7309085562105
Page2: 5 items
idx:5, time=2020-07-15T08:22:44.886Z, value=342.4362157566772
idx:5, time=2020-07-15T08:22:44.885Z, value=309.7928649131023
idx:5, time=2020-07-15T08:22:44.883Z, value=305.54587412409853
idx:5, time=2020-07-15T08:22:20.094Z, value=309.11758671277005
idx:5, time=2020-07-15T08:22:20.093Z, value=303.3260737921256
```

You are all done congratulations !






