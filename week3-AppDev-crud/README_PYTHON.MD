
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# Application Development with PYTHON EXERCISES

![JAVA](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/logo-python.png)

[🏠 GO BACK TO MAIN](./README.MD)

| Exercises |
|---|
| [Connectivity to Cassandra](#3-connectivity-to-cassandra) |
| [Create Schema](#4-create-read-update) |
| [Connectivity](#5-read-and-parse-results) |


## 3. Connectivity to Cassandra 

**✅ Step 3a. Fix unit test `Ex02_Connect_to_Cassandra.py`**


- Navigate to the _crud-python_ folder within the _week3-AppDev-crud_ folder using the terminal with the command below. Install the dependencies.

```
cd week3-AppDev-crud/crud-python/

pip3 install cassandra-driver
```

- Locate the file `db_connection.py` and fill the properties with your Astra informations

*Do not copy-paste - this is a sample for me.*

```python
# This is the Zip file you downloaded
SECURE_CONNECT_BUNDLE = '/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip'
# This is the username, recommended value was KVUser
USERNAME = "todouser";
# This is the password, recommended value was KVPassword
PASSWORD = "todopassword";
# This is the keyspace name, recommended value was killrvideo
KEYSPACE = "todoapp"; 
```

```
⚠️ By default Autosaved is not enabled in Gitpod. Don't forget to save your modifications with CTRL+S
```

Now you can run the exercice 2 to connect

```bash
python Ex02_Connect_to_Cassandra.py 
```

**👁️ Expected output**

```bash
========================================
Start exercise
Your are now connected to cluster '%s' caas-cluster
Success
Closing connection (up to 10s)
========================================
```

## 4. CREATE READ UPDATE

**✅ Step 4a. Run unit test `Ex03_Query5a_Insert_Journey.py`** to insert a journey in the catalog


```bash
python Ex03_Query5a_Insert_Journey.py 
```

```bash
========================================
Start exercise
Journey created 230995ee-c697-11ea-b7a1-8c85907c08dd
Success
Closing connection (up to 10s)
========================================
```

**✅ Step 4b. Run unit test `Ex04_Query5b_TakeOff.py`** to update the journey and marked started.

In the previous exercises we displayed a journeyID (for me it is `230995ee-c697-11ea-b7a1-8c85907c08dd`). Open `Ex04_Query5b_TakeOff.py` locate constant `JOURNEY_ID` and change value with the proper journeyID. 

Run the takeoff query by running this test

```bash
python Ex04_Query5b_TakeOff.py
```

**👁️ Expected output**

```bash
========================================
Start exercise
9..8..7..6..5..4..3..2..1 Ignition
Journey 230995ee-c697-11ea-b7a1-8c85907c08dd has now taken off
Success
========================================

```

**✅ Step 4c. Run unit test `Ex05_Query5c_Travel.py`** to simulate a travel in space


```bash
python Ex05_Query5c_Travel.py 
```

**👁️ Expected output**

```bash
/50 - Travelling..
1/50 - Travelling..
2/50 - Travelling..
3/50 - Travelling
```

**✅ Step 4d. Run unit test `Ex06_Query5d_Landing.py`** update the journey to make it inactive and provide end date. To run the test please execute:

```bash
python Ex06_Query5d_Landing.py 
```

**👁️ Expected output**

```bash
========================================
Start exercise
Journey df8d5c9c-c696-11ea-a8a1-a6cacf7cb1a0 has now landed
Success
========================================
```

## 5. READ AND PARSE RESULTS

**✅ Step 5a. Run unit test `Ex07_Query4a_ListJourneys.py`** to list all journeys for a 
spacecraft.

```bash
python Ex07_Query4a_ListJourneys.py 
```

**👁️ Expected output**

```bash
========================================
Start exercise
- Journey; 230995ee-c697-11ea-b7a1-8c85907c08dd  Summary: Bring Astronauts to ISS
- Journey; beba02a4-c696-11ea-b646-8c85907c08dd  Summary: Bring Astronauts to ISS
- Journey; bf2e708c-c694-11ea-9892-8c85907c08dd  Summary: Bring Astronauts to ISS
- Journey; a322fbb0-c694-11ea-8001-8c85907c08dd  Summary: Bring Astronauts to ISS
- Journey; 8d23bdae-c694-11ea-81b7-8c85907c08dd  Summary: Bring Astronauts to ISS
- Journey; 620acae0-c671-11ea-97e7-56004218e25e  Summary: Bring Astronauts to ISS
- Journey; 87345f00-c66e-11ea-a252-4ebb2901d808  Summary: Bring Astronauts to ISS
- Journey; 84121060-c66e-11ea-a82e-f931183227ac  Summary: Bring Astronauts to ISS
- Journey; 76dceda0-c5ee-11ea-a96c-cbbdcb5d7db1  Summary: Bring Astronauts to ISS
- Journey; 691bcf60-c5ee-11ea-9bde-d1ddecb1d5c1  Summary: Bring Astronauts to ISS
- Journey; 53983d90-c5da-11ea-bcd8-c78bf1f1af98  Summary: Bring Astronauts to ISS
- Journey; 90163870-c5d6-11ea-b11f-c30e2b038000  Summary: Bring Astronauts to ISS
Success
Closing connection (up to 10s)
========================================
```

**✅ Step 5b. Run unit test `Ex08_Query4b_Read_Journey_Details.py`** to get the details of one journey

```bash
python Ex08_Query4b_Read_Journey_Details.py 
```

**👁️ Expected output**

```bash
========================================
Start exercise
Journey has been found
- Uid:		  230995ee-c697-11ea-b7a1-8c85907c08dd
- Spacecraft:	 Crew Dragon Endeavour,SpaceX
- Summary:	 Bring Astronauts to ISS
- Active:	 True
- Takeoff:	 None
- Landing:	 None
Success
Closing connection (up to 10s)
========================================
```

**✅ Step 5c. Run unit test `Ex09_Query4c_ReadMetrics.py`** to get ALL readings (speed) from one of the journey

```bash
python Ex09_Query4c_ReadMetrics.py 
```

**👁️ Expected output**

```bash
========================================
Start exercise
idx= 0 time= 2020-07-15 08:23:18.989000 value= 358.84601643389595
idx= 1 time= 2020-07-15 08:23:18.988000 value= 330.61229400959064
idx= 2 time= 2020-07-15 08:23:18.987000 value= 307.32379130938597
...
idx= 18 time= 2020-07-15 08:15:52.226000 value= 312.62135385904617
idx= 19 time= 2020-07-15 08:15:52.225000 value= 304.43301464875776
idx= 20 time= 2020-07-15 08:10:45.736000 value= 349.81462163292764
idx= 21 time= 2020-07-15 08:10:45.735000 value= 312.94324025033586
idx= 22 time= 2020-07-15 08:10:45.734000 value= 305.9325979919639
Success
Closing connection (up to 10s)
========================================
```

**✅ Step 5d. Run unit test `Ex10_Query4c_ReadMetrics_Paging.py`** to get readings (speed) from one of the journey but page by page.

```bash
python Ex10_Query4c_ReadMetrics_Paging.py 
```

**👁️ Expected output**

```bash
========================================
Start exercise
Page1 with  5  item(s)
idx= 0 time= 2020-07-15 08:23:18.989000 value= 358.84601643389595
idx= 1 time= 2020-07-15 08:23:18.988000 value= 330.61229400959064
idx= 2 time= 2020-07-15 08:23:18.987000 value= 307.32379130938597
idx= 3 time= 2020-07-15 08:23:18.986000 value= 309.65674426432656
idx= 4 time= 2020-07-15 08:22:44.887000 value= 353.7309085562105
Paging State= b'\x00\n\x00\x08\x00\x00\x01sQ\x91\x9f\x97\xf0\x7f\xff\xff\xfa\xf0\x7f\xff\xff\xfa\x00'
Page2 with  5  item(s)
idx= 0 time= 2020-07-15 08:22:44.886000 value= 342.4362157566772
idx= 1 time= 2020-07-15 08:22:44.885000 value= 309.7928649131023
idx= 2 time= 2020-07-15 08:22:44.883000 value= 305.54587412409853
idx= 3 time= 2020-07-15 08:22:20.094000 value= 309.11758671277005
idx= 4 time= 2020-07-15 08:22:20.093000 value= 303.3260737921256
Success
Closing connection (up to 10s)
========================================
```

You are all done congratulations !






