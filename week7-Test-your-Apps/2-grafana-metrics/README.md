
![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)

# ✨Performance Benchmark your Data Model✨

[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Discord](https://img.shields.io/discord/685554030159593522)](https://discord.com/widget?id=685554030159593522&theme=dark)

## Table of Contents
| Steps |
|---|
| [Step 1: Starting a benchmark with Grafana metrics enabled](#step-1-starting-a-benchmark-with-grafana-metrics-enabled) |
| [Step 2. View Grafana](#step-2-view-grafana) |
| [Step 3. View Various Metrics](#step-3-view-various-metrics) |
| [Step 4. Launch Prometheus and View Metrics](#step-4-launch-prometheus-and-view-metrics) |

# Understanding Metrics

# Step 1: Starting a benchmark with Grafana metrics enabled

### 1a. Using --docker-metrics
Even though there are [multiple ways](http://docs.nosqlbench.io/#/docs/getting_started%2F03_reading_metrics) to get metrics from NoSQLBench, by far the easiest and quickest way is to use the **--docker-metrics** parameter. This option tells NoSQLBench to export statistical data so that we can view it in Grafana. It will launch all of the needed components and hook them all up in Docker for you. Let's try it out.


![Windows](../materials/images/windows32.png?raw=true)  ![osx](../materials/images/mac32.png?raw=true): To run on Windows or OSX use the jar.

**NOTE:** For Windows users, --docker-metrics is experimental at best. It may not work right out of the box.

📘 **Command to execute**
```
java -jar nb.jar cql-iot cyclerate=100 --progress console:15s --docker-metrics
```

![linux](../materials/images/linux32.png?raw=true) : To run on linux use the following command.

📘 **Command to execute**
```bash
./nb cql-iot cyclerate=100 --progress console:15s --docker-metrics
```
On your first run it might take a moment for all of the Grafana images to download into Docker. Once everything is up and running you should see output like the following.

📗 **Expected output**
```
11:11:06.974 [main] WARN  io.nosqlbench.engine.cli.NBCLI - Docker Containers are started, for grafana and prometheus, hit these urls in your browser: http://<host>:3000 and http://<host>:9090
```

This is letting you know that Grafana is now available at a **default** of http://localhost:3000 and Prometheus at a **default** of http://localhost:9090.

# Step 2. View Grafana
At this point you should be able to open http://localhost:3000 in your browser. Let's do that.

| Steps |
|---|
| [2a. Launch Grafana in your browser and login](#2a-Launch-Grafana-in-your-browser-and-login) |
| [2b. Navigate to the *NoSQLBench* dashboard](#2b-Navigate-to-the-NoSQLBench-dashboard) |

### 2a. Launch Grafana in your browser and login
📘 **Command to execute**
<pre>
Ctrl-click on <a href="http://localhost:3000">http://localhost:3000</a> to launch Grafana in a new tab.
</pre>

The credentials are
- username: *admin*
- password: *admin*

The first screen you are presented should look something like:
![OK](../materials/images/SkipChangePW.png?raw=true)

Normally, you should change your password when you login the first time, but for this training scenario, just skip it.

### 2b. Navigate to the *NoSQLBench* dashboard
📘 **Command to execute**
<pre>
Next, click on the dropdown arrow by the Home icon.
</pre>
![OK](../materials/images/HomeButton.png?raw=true)

📘 **Command to execute**
```
Then, click on NoSQLBench.
```
![OK](../materials/images/AnalysisLink.png?raw=true)

The benchmark will need to run for a minute or so to allow statistics to accumulate. Then you will start to see results in the various metrics' graphs. You may want to change your refresh rate in Grafana to use **Last 5 mintes Refresh every 5s** per the image below in order to see data more quickly.

![OK](../materials/images/RefreshRate.png?raw=true)

# Step 3. View Various Metrics
Let's look at what the various metrics represent.

| Steps |
|---|
| [3a. Ops and Successful Ops](#3a-Ops-and-Successful-Ops) |
| [3b. Error Counts](#3b-Error-Counts) |
| [3c. Service Time Distribution](#3c-Service-Time-Distribution) |
| [3d. Ops Tries Distribution](#3d-Ops-Tries-Distribution) |

### 3a. Ops and Successful Ops
Notice the *Ops* and *Successful Ops* graph in the top-left of Grafana's display. This graph indicates the operation rate (reads/writes per second) of the benchmark. Use this metric to see the load the benchmark places on the database.

![OK](../materials/images/OpsRate.png?raw=true)

*Both **ops** and **success** metrics should match. If you see a difference between these it is something to take a look at. You should NOT have more operations than successful operations.*

### 3b. Error Counts
The next graph shows how many database errors have occurred. You probably won't see any errors yet, but in the next scenario we'll force some errors to occur.

![OK](../materials/images/ErrorCount.png?raw=true)

*Generally, no errors are a good thing, this is where you want to be. If you do see errors on this chart you should see a correlation in the **Ops and Successful Ops** chart as well. You will also see the error type on the chart, but will most likely need to take a deeper look at results in your logs and/or Prometheus to get the full stack trace.*

### 3c. Service Time Distribution
The third graph indicates the mean (over a one minute period) service times for operations. Use this metric to understand how quickly the database responds to requests.

![OK](../materials/images/ServiceTime.png?raw=true)

*Service time is the END to END response time from your client to the server and back to the client. It is recommened you run NoSQLBench on the same infrastructure and instances as your application instances to ensure it is working through the same "path" as your application to give you the most realistic result.*

### 3d. Ops Tries Distribution
The fourth graph on the top row shows how many attempts (tries and retries) the benchmark performs per operation. Normally, each operation will only use one try to complete the operation. However, as databases become swamped, timeouts will cause the try count to increase.

![OK](../materials/images/RetryCount.png?raw=true)

*If your database is getting overwhelemed or stressed you might see an increase in the number of retries. In a perfect scenario this should always be 1 or very close to it. If this consistently deviates above 1 it garners investigation. If you are seeing values of around 10 you should stop what you are doing and investigate immediately.*

# Step 4. Launch Prometheus and View Metrics
Grafana actually pulls its data from Prometheus, which gets the data indirectly from NoSQLBench. You can launch Prometheus by opening http://localhost:9090 in your browser.

<pre>
Ctrl-click on <a href="http://localhost:9090">http://localhost:9090</a> to launch Prometheus in a new tab.
</pre>

Prometheus is useful if you are a die-hard metrics junkie who wants the raw footage. For example, if we want to investigate the raw one-minute mean service time data, we would follow these steps.

📘 **Command to execute**
<pre>
First, select insert metric at cursor and select the metric
</pre>

![OK](../materials/images/InsertMetric.png?raw=true)


📘 **Command to execute**
<pre>
Click the Execute button.
</pre>

![OK](../materials/images/ExecuteMetric.png?raw=true)

📘 **Command to execute**
<pre>
Select the Graph tab.
</pre>

![OK](../materials/images/SelectGraph.png?raw=true)

Finally, you should see something like this.

![OK](../materials/images/ViewGraph.png?raw=true)

### Now you are free to dig into as much raw data as you want!

## Woot! You've now got a taste of how to quickly visualize metrics with NoSQLBench. Now, let's move on to creating workloads to fit YOUR data model. Click [HERE](../3-custom-workloads/README.md) to go to the next scenario.
