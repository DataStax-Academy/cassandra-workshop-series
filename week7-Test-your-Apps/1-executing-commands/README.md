
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨Performance Benchmark your Data Model✨

[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

## Table of Contents
| Steps |
|---|
| [Step 1: Initial Run](#step-1-initial-run) |
| [Step 2: Create a Test Schema](#step-2-create-a-test-schema) |
| [Step 3: Write Some Test Data](#step-3-write-some-test-data) |
| [Step 4: Run the Benchmark](#step-4-run-the-benchmark) |
| [Step 5: Understand the Results](#step-5-understand-the-results) |

# Executing Commands

# Step 1: Initial Run

| Steps |
|---|
| [1a. Running NoSQLBench commands](#1a-running-nosqlbench-commands) |
| [1b. Some initial results](#1b-some-initial-results) |
| [1c. Clean Up](#1c-clean-up) |

### 1a. Running NoSQLBench commands
At this point you should have downloaded either the binary or jar and are ready to start executing commands with NoSQLBench. We will run a quick scenario just to ensure things are working. Execute the following commands to get going.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): Notice that when using the jar you will to prepend your commands with "java -jar nb.jar", but other than that ALL commands will work exactly the same as the binary. 

📘 **Command to execute**
```
java -jar nb.jar cql-iot
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
# Let's run a simple scenario (just ctrl-C to kill it once it starts up)
./nb cql-iot
```

### 1b. Some initial results
Wether you're using the binary or jar you should see output similar the following log statement and initial reporting. Feel free to kill the scenario with ctrl-C at this point.

In the previous "nb cql-iot" command we just ran a pre-packaged scenario. Don't worry about the details of that just yet, we'll get there. What's key is you see something similar to the output below which means everything is hooked up and running.

📗 **Expected output**
```bash
Logging to logs/scenario_20200530_110840_609.log
cql-iot_default_001: 3.45%/Running (details: min=0 cycle=345214 max=10000000)
cql-iot_default_000: 100.00%/Finished (details: min=0 cycle=3 max=3) (last report)
```

### 1c. Clean Up
The bechmark we just ran created a keyspace. Let's delete it just to keep things clean. Again, the following instruction assumes you are using the Docker setup. If you are using your own Cassandra then you'll need to use your own cqlsh as well. :)

📘 **Command to execute**
```
docker exec -it my-cassandra cqlsh -e "DROP KEYSPACE baselines;"
```

## Step 2: Create a Test Schema

| Steps |
|---|
| [2a. Create the schema](#2a-Create-the-schema) |
| [2b. View the results](#2b-View-the-results) |

### 2a. Create the schema
In the previous step, we ran a pre-packaged benchmark. In the remainder of this scenario, we'll show you how to un-package and customize tests.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```
java -jar nb.jar run driver=cql workload=cql-keyvalue tags=phase:schema
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb run driver=cql workload=cql-keyvalue tags=phase:schema
```

Here's what the command parameters mean:

- **run** tells **nb** to execute the sequence of operations synchronously
- **driver=cql** tells **nb** to talk to a Cassandra database using cql
- **workload=cql-keyvalue** identifies the workload description file, which in this case is **cql-keyvalue**
- **tags=phase:schema** identifies the section of the workload file containing the operations to create the schema

## 2b. View the results
In the last command we created a schema within our database for the cql-keyvalue workload. Let's take a look at what we just created.

📘 **Command to execute**
```
docker exec -it my-cassandra cqlsh -e "DESCRIBE KEYSPACE baselines;"
```

Look at the query output. You should see we created a keyspace named **baselines** and a table named **keyvalue**.

## Step 3: Write Some Test Data
Now that we have our newly created table, let's load some data, but before we do this we might want to review the activity **nb** will generate. We can do this by setting **driver=stdout**. Let's do it.

| Steps |
|---|
| [3a. Test activity with stdout](#3a-Test-activity-with-stdout) |
| [3b. This time let's insert the data into our table](#3b-This-time-lets-insert-the-data-into-our-table) |
| [3c. View the results](#3c-View-the-results) |

### 3a. Test activity with stdout

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```
java -jar nb.jar start driver=stdout workload=cql-keyvalue tags=phase:rampup cycles=10
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb start driver=stdout workload=cql-keyvalue tags=phase:rampup cycles=10
```

Let's review the parameters for this command:

- **start** is like **run**, but is asynchronous, where as **run** is synchronous
- **driver=stdout** only prints CQL statements that NoSQLBench generates, but applies no statements to the database
- **workload=cql-keyvalue** sets the name of the workload configuration file to cql-keyvalue
- **tags=phase:rampup** is the target within the workload file
- **cycles=10** tells **nb** how many times to run the target within the workload

**NOTE:** *NoSQLBench generates data values deterministically so that the values will be the same from run to run. So, the output you see with **driver=stdout** will be the same as the values actually inserted with **driver=cql**.*

📗 **Expected output**
```
 Logging to logs/scenario_20200530_134442_399.log
insert into baselines.keyvalue
(key, value)
values (0,382062539);
insert into baselines.keyvalue
(key, value)
values (1,774912474);
insert into baselines.keyvalue
(key, value)
values (2,949364593);
insert into baselines.keyvalue
(key, value)
values (3,352527683);
insert into baselines.keyvalue
(key, value)
values (4,351686621);
...
```

### 3b. This time let's insert the data into our table
The command in this case will be almost exactly the same with one caveat. Notice the value for the **driver** parameter.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```
java -jar nb.jar start driver=cql workload=cql-keyvalue tags=phase:rampup cycles=10
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb start driver=cql workload=cql-keyvalue tags=phase:rampup cycles=10
```

### 3c. View the results
Our last command used **driver=cql** instead of **driver=stdout** which means it used the CQL driver to talk to Cassandra and insert the rampup data as compared to printing it to the terminal. Let's verify.

📘 **Command to execute**
```
docker exec -it my-cassandra cqlsh -e "SELECT * FROM baselines.keyvalue;"
```

If you compare the data from our earlier command using **driver=stdout** the data in the table should match our previous output.

📗 **Expected output**
```
 key | value
-----+-----------
   6 | 439790106
   7 | 564330072
   9 |  97405552
   4 | 351686621
   3 | 352527683
   5 | 114304900
   0 | 382062539
   8 | 296173906
   2 | 949364593
   1 | 774912474
```

## Step 4: Run the Benchmark
We created our schema, tested our output data, inserted our initial rampup data into the database, and now it's time to run the actual performance benchmark. Here we go.

![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

📘 **Command to execute**
```
java -jar nb.jar start driver=cql workload=cql-keyvalue tags=phase:main cycles=100k cyclerate=5000 threads=50 --progress console:2s
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb start driver=cql workload=cql-keyvalue tags=phase:main cycles=100k cyclerate=5000 threads=50 --progress console:2s
```

You're probably familiar with many of the parameters by now, but we'll review them all anyway:

- **start** executes the workload statements asynchronously
- **workload=cql-keyvalue** identifies the workload file
- **tags=phase:main** identifies the section to run within the workload file
- **cycles=100k** tells nb to run the section 100,000 times
- **cyclerate=5000** limits the test to 5,000 operations per second
- **threads=50** the default is to use one thread. Setting this parameter allows NoSQLBench the necessary concurrency for a significant workload
- **--progress console:2s** sets the progress reporting interval to two seconds

You can follow the progress by watching the output statements on the console that indicate the percent complete.

📗 **Expected output**
```
Logging to logs/scenario_20200530_140934_635.log
cql-keyvalue: 0.50%/Running (details: min=0 cycle=500 max=100000)
cql-keyvalue: 2.15%/Running (details: min=0 cycle=2150 max=100000)
cql-keyvalue: 7.56%/Running (details: min=0 cycle=7560 max=100000)
cql-keyvalue: 13.69%/Running (details: min=0 cycle=13690 max=100000)
cql-keyvalue: 20.53%/Running (details: min=0 cycle=20530 max=100000)
cql-keyvalue: 27.79%/Running (details: min=0 cycle=27790 max=100000)
cql-keyvalue: 34.42%/Running (details: min=0 cycle=34420 max=100000)
cql-keyvalue: 41.54%/Running (details: min=0 cycle=41540 max=100000)
cql-keyvalue: 49.56%/Running (details: min=0 cycle=49560 max=100000)
cql-keyvalue: 56.57%/Running (details: min=0 cycle=56570 max=100000)
cql-keyvalue: 64.20%/Running (details: min=0 cycle=64200 max=100000)
cql-keyvalue: 71.46%/Running (details: min=0 cycle=71460 max=100000)
cql-keyvalue: 79.08%/Running (details: min=0 cycle=79080 max=100000)
cql-keyvalue: 86.42%/Running (details: min=0 cycle=86420 max=100000)
cql-keyvalue: 94.16%/Running (details: min=0 cycle=94160 max=100000)
cql-keyvalue: 100.00%/Finished (details: min=0 cycle=100000 max=100000) (last report)
```

## Step 5: Understand the Results
Ok, almost there. We ran our benchmark and now we need to take a look at the results. You may have noticed that when we run a test, nb tells us about logging. For example, we may see a line like this:
```
Logging to logs/scenario_20200530_140934_635.log
```

At first glance the log files might feel a little overwhelming because they contain a lot of trace information, but we are interested in performance data so we can filter for "type=TIMER".

![Windows](../materials/images/windows32.png?raw=true): For Windows users use your favorite document editor and search for "type=TIMER".

![linux](../materials/images/linux32.png?raw=true) ![osx](../materials/images/mac32.png?raw=true) : On linux or OSX run the following command from the directory you are running **nb**. 

**NOTE:** Replace the scenario log file **scenario_20200530_140934_635.log** with the file from your local run.

📘 **Command to execute**
```bash
cat logs/scenario_20200530_140934_635.log | grep 'type=TIMER'
```

📗 **Expected output**
```
2020-05-30 14:10:06,468 INFO [main] i.n.e.c.ScenarioResult [Slf4jReporter.java:374] type=TIMER, name=cql-keyvalue.bind, count=100000, min=0.586, max=5174.271, mean=5.56551375, stddev=47.092247802299596, median=3.001, p75=4.21, p95=15.365, p98=26.326, p99=39.521, p999=103.563, mean_rate=3454.2882529085723, m1=2897.9790369246602, m5=2676.076153258973, m15=2633.157700814513, rate_unit=events/second, duration_unit=microseconds
```
Now, this might seem like a lot to understand. Let's break it down.
- **20-04-29 16:57:11** indicates when the sample measurement occurred
- **414 INFO [main] i.n.e.c.ScenarioResult [Slf4jReporter.java:374] type=TIMER** tells where, in the nb code, the measurement came from
- **name=cql-keyvalue.bind** indicates the name of the wokload file
- **count=100000** is the number of operations in this sample
- **min=0.392** is the time measurement of the shortest operation (see the duration_unit at the bottom of the list for the units)
- **max=16811.007** is the time measurement of the longest operation
- **mean=6.76477675** is the mean (average) operation time
- **stddev=173.637642943727** is the standard deviation from the mean
- **median=1.479** is the median (middle) operation time
- **p75=2.314** is the 75th percentile measurement (75% of operations took this amount of time or less)
- **p95=8.652** is the 95th percentile measurement
- **p98=17.374** is the 98th percentile measurement
- **p99=35.603** is the 99th percentile measurement
- **p999=482.063** is the 99.9th percentile measurement
- **mean_rate=4323.519012492167** is the number of operations per time period (see the rate_unit near the bottom of the list for the units)
- **m1=3726.7032056089** is the mean rate measured during 1 minute
- **m5=3527.812026532018** is the mean rate measured during 5 minutes
- **m15=3490.6484917755765** is the mean rate measured during 15 minutes
- **rate_unit=events/second** indicates the time unit for rate measurements
- **duration_unit=microseconds** indicates the time unit for timing measurements

Ok, so there's a solid amount of data here and you may not know exactly what each of these mean. That's ok, We're going to get into the highlights in the next section and give you tools to visualize metrics data.

## Congratulations, you've executed a set of NoSQLBench commands. Hopefully you are starting to get a feel for how it works. Click [HERE](../2-grafana-metrics/README.md) to go to the next scenario.
