
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨Performance Benchmark your Data Model✨

[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)


## Table of Contents
| Steps |
|---|
| [Intro : Custom Workloads](#custom-workloads) |
| [Step 1: Listing Workloads and Named Scenarios](#step-1-listing-workloads-and-named-scenarios) |
| [Step 2: Copying Workloads](#step-2-copying-workloads) |
| [Step 3: Building Your First Workload](#step-3-building-your-first-workload) |
| [Step 4: Putting it all Together](#step-4-putting-it-all-together) |

# Custom Workloads
This section starts getting into a slightly more advanced topic, but once you understand the pattern you'll be on your way to creating workloads and scenarios for your own data models. This won't be an exhaustive walkthrough, but should give you enough to get going.

# Step 1: Listing Workloads and Named Scenarios
The benchmarking workloads we have run in previous scenarios we all pre-packaged. Now, we'll take a look at how these are built so we can start creating our own.

| Steps |
|---|
| [1a. Using --list-workloads](#1a-Using---list-workloads) |
| [1b. Using --list-scenarios](#1b-Using---list-scenarios) |

### 1a. Using --list-workloads
It's usually easiest to modify an existing workload. We can list the pre-packaged workloads with this command.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```
java -jar nb.jar --list-workloads
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb --list-workloads
```

📗 **Expected output**
```
activities/baselines/cql-iot-dse.yaml
# description:
An IOT workload which more optimal DSE settings

activities/baselines/cql-iot.yaml
# description:
This workload emulates a time-series data model and access patterns.
...
```

### 1b. Using --list-scenarios
Named scenarios are found within workloads and allow you to create multiple variations of scenarios. For example, maybe for the **default** scenario I perform queries as I would expect, but I also might want an **allow-filtering** scenario to test out what might happen if I use **ALLOW FILTERING** in my read queries. I can add both to a workload to make it easy to switch between them, reuse the same bindings, or even run them as part of a single benchmark for comparison.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```
java -jar nb.jar --list-scenarios
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb --list-scenarios
```

📗 **Expected output**
```
# workload in activities/baselines/cql-iot.yaml
# description:
This workload emulates a time-series data model and access patterns.
    # scenarios:
    nb activities/baselines/cql-iot default
        # defaults
        compression = LZ4Compressor
        expiry_minutes = 60
        instrument = false
        instrument-reads = false
        instrument-writes = false
        keyspace = baselines
        limit = 10
...
```
Notice how the above example displays the **default** scenario within the **cql-iot** workload. Not only that, but this output will also display all of the default parameters being used.

# Step 2: Copying Workloads
Ok, so we listed some workloads and scenarios, but all of the pre-packaged ones are stored in the nb binary or jar. Instead of trying to create one the first time from scratch it's best to simply copy an existing one and go from there. Luckily, there's a very simple command to do just that.

| Steps |
|---|
| [2a. Using --copy](#2a-Using---copy) |
| [2b. Reading the yaml](#2b-Reading-the-yaml) |

### 2a. Using --copy
Since we've been using the cql-iot workload throughout this workshop let's work with that. Just use the **--copy** option and pass the name of the workload. That's it.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```bash
java -jar nb.jar --copy cql-iot
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb --copy cql-iot
```

There is no log output, but **cql-iot.yaml should be sitting in the directory** where you ran the above command.

### 2b. Reading the yaml
Briefly, inspect the extracted file with your favorite editor. Like an unfamiliar yaml file, these can be a bit overwhelming at first glance, but we'll walk you through it. By the end of this scenario, you'll be comfortable with the basics!

📘 **Command to execute**
```
Using your favorite text/yaml editor open cql-iot.yaml and inspect the contents
```

*Don't go too crazy trying to understand everything in the file at this point. We'll build up to that. For now a quick once-over is fine.*

# Step 3: Building Your First Workload
Since this is our first introduction to workload configuration, we created some stripped-down files to emphsize the important parts. We'll use these stripped-down files instead of the one we extracted, but when we are done, you should be able to make your way through the file you extracted.

| Steps |
|---|
| [3a. Create a test schema](#3a-Create-a-test-schema) |
| [3b. Insert initial seed data](#3b-Insert-initial-seed-data) |
| [3c. Execute your main benchmark](#3c-Execute-your-main-benchmark) |

### 3a. Create a test schema
The first thing a workload wants to do is create the necessary keyspace and tables for the benchmark. Here's an example workload to create the IOT keyspace and tables.

```yaml
# nb run driver=cql workload=cql-iot-basic-schema.yaml threads=auto cycles=3
description: |
  This workload emulates a time-series data model schema creation.
blocks:
  - tags:
      phase: schema
    params:
      prepared: false
    statements:
     - create-keyspace: |
        create keyspace if not exists baselines
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}
        AND durable_writes = true;
     - create-table : |
        create table if not exists baselines.iot (
        machine_id UUID,     // source machine
        sensor_name text,    // sensor name
        time timestamp,      // timestamp of collection
        sensor_value double, //
        station_id UUID,     // source location
        data text,
        PRIMARY KEY ((machine_id, sensor_name), time)
        ) WITH CLUSTERING ORDER BY (time DESC)
         AND compression = { 'sstable_compression' : 'LZ4Compressor' }
         AND compaction = {
         'class': 'TimeWindowCompactionStrategy',
         'compaction_window_size': 60,
         'compaction_window_unit': 'MINUTES'
        };
     - truncate-table: |
         truncate table baselines.iot;
```

We'll put a comment at the top of the file that shows how to run the workload as well as a description of the workload.
```yaml
# nb run driver=cql workload=cql-iot-basic-schema.yaml threads=auto cycles=3
description: |
  This workload emulates a time-series data model schema creation.
```

The remainder of this file is a list named blocks containing a single block. Each block has tags. We use these tags to indicate the phase of the workload. In this example, the phase is **schema**, which is short for schema creation. Remember back in the [**Executing Commands**](../1-executing-commands#executing-commands) section when we executed the command ["run driver=cql workload=cql-keyvalue tags=phase:schema"](../1-executing-commands#2a-create-the-schema)? This is the *Schema* phase we were referring to in the **cql-keyvalue** workload. When called, it will process everything in the **schema** phase.
```yaml
blocks:
  - tags:
      phase: schema
```

Ok, so let's take a look at the rest of the file. 
- **prepared: false** is telling nb none of the following statements needs to be prepared. Since these are used only once for schema creation they do not need to be prepared
- **statements:** sets the statements (3 in our example) that will be executed within this phase
- **- create-keyspace:** sets the CQL DDL statement to use for creating the keyspace
- **- create-table :** sets the CQL DDL statement(s) to use for creating tables
- **- truncate-table:** is truncating the table in case it already exists to ensure data is clean

Notice within each **statement** type are the CQL statements to execute. These are exactly the same as the statements you might execute within cqlsh or from within your application.
```yaml
params:
      prepared: false
    statements:
     - create-keyspace: |
        create keyspace if not exists baselines
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}
        AND durable_writes = true;
     - create-table : |
        create table if not exists baselines.iot (
        machine_id UUID,     // source machine
        sensor_name text,    // sensor name
        time timestamp,      // timestamp of collection
        sensor_value double, //
        station_id UUID,     // source location
        data text,
        PRIMARY KEY ((machine_id, sensor_name), time)
        ) WITH CLUSTERING ORDER BY (time DESC)
         AND compression = { 'sstable_compression' : 'LZ4Compressor' }
         AND compaction = {
         'class': 'TimeWindowCompactionStrategy',
         'compaction_window_size': 60,
         'compaction_window_unit': 'MINUTES'
        };
     - truncate-table: |
         truncate table baselines.iot;
```

*BTW, all of the above sections are optional. Obviously, you would need to create a keyspace if you want any tables, but you technically don't have to include it. I just included this set of examples to give a "full" picture of what you might need for a schema.*

### 3b. Insert initial seed data
When benchmarking you're attempting to emulate what your system performance looks like under realistic conditions. This includes access patterns, data density, and factoring in headroom that any highly available system requires. The **rampup** phase is meant to bring a system under test to a realistic density with a realistic data set.

*Response, throughput, and data density are always connected. Every database performs differently at higher density for read operations, thus it is imperative that you qualify your results with all three parameters: throughput, latency, and density.*

```yaml
# nb run driver=cql workload=cql-iot-basic-rampup.yaml threads=auto cycles=100000
description: |
  This workload emulates a time-series data model and ramps up data after schema creation.
blocks:
  - tags:
      phase: rampup
    bindings:
      machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
      sensor_name: HashedLineToString('data/variable_words.txt')
      time: Mul(100); Div(10000L); ToDate()
      cell_timestamp: Mul(100L); Div(10000L); Mul(1000L)
      sensor_value: Normal(0.0,5.0); Add(100.0) -> double
      station_id: Div(10000);Mod(100); ToHashedUUID() -> java.util.UUID
      data: HashedFileExtractToString('data/lorem_ipsum_full.txt',800,1200)
    statements:
     - insert-rampup: |
        insert into baselines.iot
        (machine_id, sensor_name, time, sensor_value, station_id, data)
        values ({machine_id}, {sensor_name}, {time}, {sensor_value}, {station_id}, {data})
        using timestamp {cell_timestamp}
       idempotent: true
       prepared: true
       cl: LOCAL_QUORUM
```

Notice the name of our **phase** tag. Again, remember back to the [**Executing Commands**](../1-executing-commands#executing-commands) section when we executed the command ["start driver=stdout workload=cql-keyvalue **tags=phase:rampup** cycles=10"](../1-executing-commands#3a-test-activity-with-stdout)? This was executing the **rampup** phase which inserts the initial dataset into our data model and primes us for our benchmark.
```yaml
blocks:
  - tags:
      phase: rampup
```

I think at this point you will recognize the **insert** statement within the **statements** section to be the insert statement responsible for our data load. Again, this is the same DML statement would you execute in cqlsh or from your application. 
Notice the following paramters, again, all optional.
- **idempotent: true** sets idempotency to true
- **prepared: true** sets prepared to true since this query will run many times
- **cl: LOCAL_QUORUM** sets our consistency level to LOCAL_QUORUM

The last part I want to call out is the **bindings** section. This is the section where our "generated" data comes from. I put generated in quotes because data is not randomly generated or anything like that. It comes from initial seed data and functions used to calculate values. The key here is that this data is deterministic which means subsequent runs will generate the same data. If you want a deeper dive take a look [here](http://docs.nosqlbench.io/#/docs/bindings).

For now, I want you to see the relationship between bindings and how they are applied to the insert statement.
Notice the following 3 bindings.
```yaml
machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
sensor_name: HashedLineToString('data/variable_words.txt')
time: Mul(100); Div(10000L); ToDate()
```

Now notice our insert statement. See the curly braces {} around **machine_id**, **sensor_name**, and **time**? These are mapping the respective bindings to the insert statement. That's it. Create your bindings, then apply to your queries.
```yaml
 insert into baselines.iot
        (machine_id, sensor_name, time, sensor_value, station_id, data)
        values ({machine_id}, {sensor_name}, {time}, {sensor_value}, {station_id}, {data})
```

### 3c. Execute your main benchmark
Now that we have a schema created and our initial rampup data in place, it's time to run our main benchmark. Once more, remember back to the [**Executing Commands**](../1-executing-commands#executing-commands) section when we executed the command ["start driver=cql workload=cql-keyvalue tags=**phase:main** cycles=100k cyclerate=5000 threads=50 --progress console:2s"](../1-executing-commands#step-4-run-the-benchmark)? This was executing the **main** phase which starts our performance benchmark.

```yaml
# nb run driver=cql workload=cql-iot-basic-main.yaml threads=auto cycles=100000
description: |
  This workload emulates a time-series access pattern.
blocks:
  - tags:
      phase: main
    bindings:
      machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
      sensor_name: HashedLineToString('data/variable_words.txt')
    statements:
     - select-read: |
         select * from baselines.iot
         where machine_id={machine_id} and sensor_name={sensor_name}
         limit 10
       cl: LOCAL_QUORUM
       prepared: true
```

One more time, let's look at the **phase** tag.
```yaml
blocks:
  - tags:
      phase: main
```

You can see we have a single **read** query with 2 bindings for **machine_id** and **sensor_name** set to **LOCAL_QUORUM** using prepared statements.

# Step 4: Putting it all Together
Ok, at this point you could actually stop with the **schema**, **rampup**, and **main** phase files and simply work with those. If you wanted you could execute each of these like the following examples and be perfectly fine.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```bash
java -jar nb.jar run driver=cql workload=cql-iot-basic-schema.yaml threads=auto cycles=3
java -jar nb.jar run driver=cql workload=cql-iot-basic-rampup.yaml threads=auto cycles=100000 --progress console:1s
java -jar nb.jar run driver=cql workload=cql-iot-basic-main.yaml threads=auto cycles=100000 --progress console:1s
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb run driver=cql workload=cql-iot-basic-schema.yaml threads=auto cycles=3
./nb run driver=cql workload=cql-iot-basic-rampup.yaml threads=auto cycles=100000 --progress console:1s
./nb run driver=cql workload=cql-iot-basic-main.yaml threads=auto cycles=100000 --progress console:1s
```

However, we can go another step further and really bring some awesomness to the party. In the following example I have combined all of the above examples into a single workload file. Now, I have a single file to use for my whole benchmarking setup. I can call any phase I want using **tags=phase:*some-phase-here*** from the command line.

```yaml
# nb run driver=cql workload=cql-iot-basic-all.yaml tags=phase:schema threads=auto cycles=3
# nb run driver=cql workload=cql-iot-basic-all.yaml tags=phase:rampup threads=auto cycles=100000
# nb run driver=cql workload=cql-iot-basic-all.yaml tags=phase:main threads=auto cycles=100000
description: |
  This workload emulates a time-series data model and access patterns.
blocks:
  - tags:
      phase: schema
    params:
      prepared: false
    statements:
     - create-keyspace: |
        create keyspace if not exists baselines
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}
        AND durable_writes = true;
     - create-table : |
        create table if not exists baselines.iot (
        machine_id UUID,     // source machine
        sensor_name text,    // sensor name
        time timestamp,      // timestamp of collection
        sensor_value double, //
        station_id UUID,     // source location
        data text,
        PRIMARY KEY ((machine_id, sensor_name), time)
        ) WITH CLUSTERING ORDER BY (time DESC)
         AND compression = { 'sstable_compression' : 'LZ4Compressor' }
         AND compaction = {
         'class': 'TimeWindowCompactionStrategy',
         'compaction_window_size': 60,
         'compaction_window_unit': 'MINUTES'
        };
     - truncate-table: |
         truncate table baselines.iot;
  - tags:
      phase: rampup
    params:
      cl: LOCAL_QUORUM
    bindings:
      machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
      sensor_name: HashedLineToString('data/variable_words.txt')
      time: Mul(100L); Div(10000L); ToDate()
      cell_timestamp: Mul(100L); Div(10000L); Mul(1000L)
      sensor_value: Normal(0.0,5.0); Add(100.0) -> double
      station_id: Div(10000);Mod(100); ToHashedUUID() -> java.util.UUID
      data: HashedFileExtractToString('data/lorem_ipsum_full.txt',800,1200)
    statements:
     - insert-rampup: |
        insert into  baselines.iot
        (machine_id, sensor_name, time, sensor_value, station_id, data)
        values ({machine_id}, {sensor_name}, {time}, {sensor_value}, {station_id}, {data})
        using timestamp {cell_timestamp}
       idempotent: true
  - tags:
      phase: main
    params:
      cl: LOCAL_QUORUM
      prepared: true
    bindings:
      machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
      sensor_name: HashedLineToString('data/variable_words.txt')
    statements:
     - select-read: |
         select * from baselines.iot
         where machine_id={machine_id} and sensor_name={sensor_name}
         limit 10
```

Let's execute this last set with the single, combined file and notice we are now referencing a single yaml file along with using the **tags=phase:** parameter.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```bash
java -jar nb.jar run driver=cql workload=cql-iot-basic-all.yaml tags=phase:schema threads=auto cycles=3
java -jar nb.jar run driver=cql workload=cql-iot-basic-all.yaml tags=phase:rampup threads=auto cycles=100000 --progress console:1s
java -jar nb.jar run driver=cql workload=cql-iot-basic-all.yaml tags=phase:main threads=auto cycles=100000 --progress console:1s
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb run driver=cql workload=cql-iot-basic-all.yaml tags=phase:schema threads=auto cycles=3
./nb run driver=cql workload=cql-iot-basic-all.yaml tags=phase:rampup threads=auto cycles=100000 --progress console:1s
./nb run driver=cql workload=cql-iot-basic-all.yaml tags=phase:main threads=auto cycles=100000 --progress console:1s
```

Further, we can create scenarios that allow us to run a sequence of blocks. We can specify the name of the scenario by passing the name of the scenario in the command line. If we name the scenario **default**, the scenario will run automatically.

Here's an example. Let's modify the workload file by replacing adding a **scenarios:** section in our yaml.

```yaml
scenarios:
  default:
    - run driver=cql tags==phase:schema threads==1 cycles==UNDEF
    - run driver=cql tags==phase:rampup cycles==100000 threads=auto
    - run driver=cql tags==phase:main cycles==100000 threads=auto
```

The complete yaml look like this.

```yaml
# nb cql-iot-basic-scenario --docker-metrics
description: |
  This workload emulates a time-series data model and access patterns and uses a named scenario to run all phases with default values.
scenarios:
  default:
    - run driver=cql tags==phase:schema threads==1 cycles==UNDEF
    - run driver=cql tags==phase:rampup cycles==100000 threads=auto
    - run driver=cql tags==phase:main cycles==100000 threads=auto
blocks:
  - tags:
      phase: schema
    params:
      prepared: false
    statements:
     - create-keyspace: |
        create keyspace if not exists baselines
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}
        AND durable_writes = true;
     - create-table : |
        create table if not exists baselines.iot (
        machine_id UUID,     // source machine
        sensor_name text,    // sensor name
        time timestamp,      // timestamp of collection
        sensor_value double, //
        station_id UUID,     // source location
        data text,
        PRIMARY KEY ((machine_id, sensor_name), time)
        ) WITH CLUSTERING ORDER BY (time DESC)
         AND compression = { 'sstable_compression' : 'LZ4Compressor' }
         AND compaction = {
         'class': 'TimeWindowCompactionStrategy',
         'compaction_window_size': 60,
         'compaction_window_unit': 'MINUTES'
        };
     - truncate-table: |
         truncate table baselines.iot;
  - tags:
      phase: rampup
    params:
      cl: LOCAL_QUORUM
    bindings:
      machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
      sensor_name: HashedLineToString('data/variable_words.txt')
      time: Mul(100L); Div(10000L); ToDate()
      cell_timestamp: Mul(100L); Div(10000L); Mul(1000L)
      sensor_value: Normal(0.0,5.0); Add(100.0) -> double
      station_id: Div(10000);Mod(100); ToHashedUUID() -> java.util.UUID
      data: HashedFileExtractToString('data/lorem_ipsum_full.txt',800,1200)
    statements:
     - insert-rampup: |
        insert into  baselines.iot
        (machine_id, sensor_name, time, sensor_value, station_id, data)
        values ({machine_id}, {sensor_name}, {time}, {sensor_value}, {station_id}, {data})
        using timestamp {cell_timestamp}
       idempotent: true
  - tags:
      phase: main
    params:
      cl: LOCAL_QUORUM
      prepared: true
    bindings:
      machine_id: Mod(10000); ToHashedUUID() -> java.util.UUID
      sensor_name: HashedLineToString('data/variable_words.txt')
    statements:
     - select-read: |
         select * from baselines.iot
         where machine_id={machine_id} and sensor_name={sensor_name}
         limit 10
```

And finally we can run our whole scenario with the following.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```bash
java -jar nb.jar ./cql-iot-basic-scenario.yaml default --progress console:1s
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb ./cql-iot-basic-scenario.yaml default --progress console:1s
```

## Ok, I think it's time to grab a cup of coffee or tea and take a mental break. I threw a good amount at you there. There is a TON more you can do with NoSQLBench, but this should get you started and on your way to benchmarking your data models. Happy benchmarking! 