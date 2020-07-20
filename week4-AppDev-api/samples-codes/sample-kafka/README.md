# Reactive Driver and Kafka

### Projects overview :
- *[kafka-dse-core](kafka-dse-core)* contains domain beans and utility classes
- [kafka-dse-producer](kafka-dse-producer) is a standalone Spring Boot application that generates random Tick Data load from AlphaVantage and then random
- [kafka-dse-webui](kafka-dse-webui) is the standalone web UI.

<img src="./pics/02-archi.png" height="300" />


## Start the project

#### Use docker compose to start all components
 
```bash
docker-compose up -d
```

A `docker ps` command will list you the following containers :
- **datastax/dse-server:6.7.0** is the DataStax enterprise server (listening on 9042)
- **datastax/dse-studio:6.7.0** is the WebUI available 9091 [DataStax Studio ](http://localhost:9091)
- **wurstmeister/zookeeper:3.4.6** is the Zookeeper ensuring consistency for Kafka (listening on 2181)
- **wurstmeister/kafka:1.0.0**  the kafka server (listening on 9092)

### 2. Start the `kafka-dse-producer` component. 

Open the folder kafka-dse-producer :

```
# Start Web Application
mvn spring-boot:run 
```

This will create the `KeySpace` `demo_kafka` if needed, the tables to work with and fill the reference table `stocks_infos` with Data coming fron the CSV

This is a Camel Application with no UI, still you should access the [SpringBoot Administration Console](ttp://localhost:8088/admin#/wallboard)

<img src="./pics/AdminConsole.png"  height="300px" />

### 3. Start the `kafka-dse-webui` component. 

Open the folder kafka-dse-webui :

```
# Start Web Application
mvn spring-boot:run 
```

[Access the web UI](http://localhost:8082)


