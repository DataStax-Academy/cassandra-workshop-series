## Demonstration

1. Demarrage de KSQL en ligne de commande :

```
docker-compose exec ksql-cli bash -c 'echo -e "\n\n⏳ Waiting for KSQL to be available before launching CLI\n"; while [ $(curl -s -o /dev/null -w %{http_code} http://ksql-server:8088/) -eq 000 ] ; do echo -e $(date) "KSQL Server HTTP state: " $(curl -s -o /dev/null -w %{http_code} http://ksql-server:8088/) " (waiting for 200)" ; sleep 5 ; done; ksql http://ksql-server:8088'
```

2. Affichage des TOPICS

```
PRINT 'atm_txns_gess' FROM BEGINNING;
```

3. Creation du premier stream

```sql
CREATE STREAM ATM_TXNS_GESS (account_id VARCHAR, \
                            atm VARCHAR, \
                            location STRUCT<lon DOUBLE, \
                                            lat DOUBLE>, \
                            amount INT, \
                            timestamp VARCHAR, \
                            transaction_id VARCHAR) \
            WITH (KAFKA_TOPIC='atm_txns_gess', \
            VALUE_FORMAT='JSON', \
            TIMESTAMP='timestamp', \
            TIMESTAMP_FORMAT='yyyy-MM-dd HH:mm:ss X');
```
4. Requete sur le stream

```
SELECT TIMESTAMPTOSTRING(ROWTIME, 'HH:mm:ss'), ACCOUNT_ID, ATM, AMOUNT\
        FROM ATM_TXNS_GESS \
        LIMIT 5;
```

6. Creation d'un clone

```sql
CREATE STREAM ATM_TXNS_GESS_02 WITH (PARTITIONS=1) AS \
        SELECT * FROM ATM_TXNS_GESS;
```

7. Join et premier calcul

We display here transactions of same Account, different transactions within 10 minutes.
 
```sql
SELECT S1.ACCOUNT_ID, \
        TIMESTAMPTOSTRING(S1.ROWTIME, 'HH:mm:ss'), \
        TIMESTAMPTOSTRING(S2.ROWTIME, 'HH:mm:ss'), \
        (S2.ROWTIME - S1.ROWTIME)/1000, \
        S1.TRANSACTION_ID ,S2.TRANSACTION_ID \
FROM   ATM_TXNS_GESS S1 \
       INNER JOIN ATM_TXNS_GESS_02 S2 \
        WITHIN 10 MINUTES \
        ON S1.ACCOUNT_ID = S2.ACCOUNT_ID \
LIMIT 40;
```

8. Filtre pour changer les locations

```sql
SELECT S1.ACCOUNT_ID, \
        TIMESTAMPTOSTRING(S1.ROWTIME, 'HH:mm:ss'), \
        TIMESTAMPTOSTRING(S2.ROWTIME, 'HH:mm:ss'), \
        (S2.ROWTIME - S1.ROWTIME)/1000 , \
        S1.ATM, S2.ATM, \
        S1.TRANSACTION_ID ,S2.TRANSACTION_ID \
FROM   ATM_TXNS_GESS S1 \
       INNER JOIN ATM_TXNS_GESS_02 S2 \
        WITHIN (0 MINUTES, 10 MINUTES) \
        ON S1.ACCOUNT_ID = S2.ACCOUNT_ID \
WHERE   S1.TRANSACTION_ID != S2.TRANSACTION_ID \
  AND   (S1.location->lat != S2.location->lat OR \
         S1.location->lon != S2.location->lon) \
  AND   S2.ROWTIME != S1.ROWTIME \
LIMIT 20;
```

9. Ajouter distance et vitess

```sql
SELECT S1.ACCOUNT_ID, \
        TIMESTAMPTOSTRING(S1.ROWTIME, 'yyyy-MM-dd HH:mm:ss'), TIMESTAMPTOSTRING(S2.ROWTIME, 'HH:mm:ss'), \
        (CAST(S2.ROWTIME AS DOUBLE) - CAST(S1.ROWTIME AS DOUBLE)) / 1000 / 60 AS MINUTES_DIFFERENCE,  \
        CAST(GEO_DISTANCE(S1.location->lat, S1.location->lon, S2.location->lat, S2.location->lon, 'KM') AS INT) AS DISTANCE_BETWEEN_TXN_KM, \
        GEO_DISTANCE(S1.location->lat, S1.location->lon, S2.location->lat, S2.location->lon, 'KM') / ((CAST(S2.ROWTIME AS DOUBLE) - CAST(S1.ROWTIME AS DOUBLE)) / 1000 / 60 / 60) AS KMH_REQUIRED, \
        S1.ATM, S2.ATM \
FROM   ATM_TXNS_GESS S1 \
       INNER JOIN ATM_TXNS_GESS_02 S2 \
        WITHIN (0 MINUTES, 10 MINUTES) \
        ON S1.ACCOUNT_ID = S2.ACCOUNT_ID \
WHERE   S1.TRANSACTION_ID != S2.TRANSACTION_ID \
  AND   (S1.location->lat != S2.location->lat OR \
         S1.location->lon != S2.location->lon) \
  AND   S2.ROWTIME != S1.ROWTIME \
LIMIT 20;
```

10. Creation du nouveau stream

```sql
CREATE STREAM ATM_POSSIBLE_FRAUD  \
    WITH (PARTITIONS=1) AS \
SELECT S1.ROWTIME AS TX1_TIMESTAMP, S2.ROWTIME AS TX2_TIMESTAMP, \
        GEO_DISTANCE(S1.location->lat, S1.location->lon, S2.location->lat, S2.location->lon, 'KM') AS DISTANCE_BETWEEN_TXN_KM, \
        (S2.ROWTIME - S1.ROWTIME) AS MILLISECONDS_DIFFERENCE,  \
        (CAST(S2.ROWTIME AS DOUBLE) - CAST(S1.ROWTIME AS DOUBLE)) / 1000 / 60 AS MINUTES_DIFFERENCE,  \
        GEO_DISTANCE(S1.location->lat, S1.location->lon, S2.location->lat, S2.location->lon, 'KM') / ((CAST(S2.ROWTIME AS DOUBLE) - CAST(S1.ROWTIME AS DOUBLE)) / 1000 / 60 / 60) AS KMH_REQUIRED, \
        S1.ACCOUNT_ID AS ACCOUNT_ID, \
        S1.TRANSACTION_ID AS TX1_TRANSACTION_ID, S2.TRANSACTION_ID AS TX2_TRANSACTION_ID, \
        S1.AMOUNT AS TX1_AMOUNT, S2.AMOUNT AS TX2_AMOUNT, \
        S1.ATM AS TX1_ATM, S2.ATM AS TX2_ATM, \
        CAST(S1.location->lat AS STRING) + ',' + CAST(S1.location->lon AS STRING) AS TX1_LOCATION, \
        CAST(S2.location->lat AS STRING) + ',' + CAST(S2.location->lon AS STRING) AS TX2_LOCATION \
FROM   ATM_TXNS_GESS S1 \
       INNER JOIN ATM_TXNS_GESS_02 S2 \
        WITHIN (0 MINUTES, 10 MINUTES) \
        ON S1.ACCOUNT_ID = S2.ACCOUNT_ID \
WHERE   S1.TRANSACTION_ID != S2.TRANSACTION_ID \
  AND   (S1.location->lat != S2.location->lat OR \
         S1.location->lon != S2.location->lon) \
  AND   S2.ROWTIME != S1.ROWTIME;
```

11. Run queries on the new stream

```sql
SELECT ACCOUNT_ID, \
        TIMESTAMPTOSTRING(TX1_TIMESTAMP, 'yyyy-MM-dd HH:mm:ss'), TIMESTAMPTOSTRING(TX2_TIMESTAMP, 'HH:mm:ss'), \
        TX1_ATM, TX2_ATM, \
        DISTANCE_BETWEEN_TXN_KM, MINUTES_DIFFERENCE \
FROM ATM_POSSIBLE_FRAUD;  
```

REGISTER SINK :

```
curl -s \
     -X "POST" "http://localhost:18083/connectors/" \
     -H "Content-Type: application/json" \
     -d '{
      "name": "dse_tx_flat",
      "config": {
        "connector.class": "com.datastax.kafkaconnector.DseSinkConnector",
                "tasks.max": "1",
                "topics": "ATM_TXNS_DSE",
                "contactPoints": "dse",
                "loadBalancing.localDc": "DC1",
                "port": 9042,
                "maxConcurrentRequests": 10,
                "maxNumberOfRecordsInBatch": 20,
                "queryExecutionTimeout": 1000,
                "connectionPoolLocalSize": 1,
                "jmx": false,
                "compression": "None",
                "auth.provider": "None",
                "topic.ATM_TXNS_DSE.bank.transactions.mapping": "account_id=value.ACCOUNT_ID, transaction_id=value.TRANSACTION_ID, timestamp=value.TIMESTAMP, atm=value.ATM, amount=value.AMOUNT, longitude=value.LON, latitude=value.LAT",
                "topic.ATM_TXNS_DSE.bank.transactions.consistencyLevel": "ONE",
                "topic.ATM_TXNS_DSE.bank.transactions.ttl": -1,
                "topic.ATM_TXNS_DSE.bank.transactions.nullToUnset": "true",
                "topic.ATM_TXNS_DSE.bank.transactions.deletesEnabled": "true",
                "topic.ATM_TXNS_DSE.codec.locale": "en_US",
                "topic.ATM_TXNS_DSE.codec.timeZone": "UTC",
                "topic.ATM_TXNS_DSE.codec.timestamp": "yyyy-MM-dd HH:mm:ss",
                "topic.ATM_TXNS_DSE.codec.date": "ISO_LOCAL_DATE",
                "topic.ATM_TXNS_DSE.codec.time": "ISO_LOCAL_TIME",
                "topic.ATM_TXNS_DSE.codec.unit": "MILLISECONDS"
            }
        }' 
```
