package com.datastax.workshop;

import static com.datastax.oss.driver.api.core.type.DataTypes.BOOLEAN;
import static com.datastax.oss.driver.api.core.type.DataTypes.TEXT;
import static com.datastax.oss.driver.api.core.type.DataTypes.TIMEUUID;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;

/*
 *




CREATE TYPE IF NOT EXISTS location_udt (
    x_coordinate double,
    y_coordinate double,
    z_coordinate double);

CREATE TABLE IF NOT EXISTS spacecraft_location_over_time (
    spacecraft_name text,
    journey_id timeuuid,
    location frozen<location_udt>,
    location_unit text,
    reading_time timestamp,
    PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
) WITH CLUSTERING ORDER BY (reading_time DESC);

 */
public interface DataModelConstants {
    
    /** Constants for table todo_tasks */
    String TABLE_JOURNEY            = "spacecraft_journey_catalog";
    String JOURNEY_SPACECRAFT_NAME  = "spacecraft_name";
    String JOURNEY_ID               = "journey_id";
    String JOURNEY_START            = "start";
    String JOURNEY_END              = "end";
    String JOURNEY_ACTIVE           = "active";
    String JOURNEY_SUMMARY          = "summary";

    /**
     *   CREATE TABLE IF NOT EXISTS spacecraft_journey_catalog (
     *    spacecraft_name text,
     *    journey_id timeuuid,
     *    start timestamp,
     *    end timestamp,
     *    active boolean,
     *    summary text,
     *    PRIMARY KEY ((spacecraft_name), journey_id)) 
     *  WITH CLUSTERING ORDER BY (journey_id desc);
     */
    SimpleStatement STMT_CREATE_TABLE_JOURNEY = 
            createTable(TABLE_JOURNEY).ifNotExists()
            .withPartitionKey(JOURNEY_SPACECRAFT_NAME, TEXT)
            .withClusteringColumn(JOURNEY_ID, TIMEUUID)
            .withColumn(JOURNEY_START, BOOLEAN)
            .withColumn(JOURNEY_END, TEXT)
            .withColumn(JOURNEY_ACTIVE, TEXT)
            .withColumn(JOURNEY_SUMMARY, TEXT)
            .withClusteringOrder(JOURNEY_ID, ClusteringOrder.DESC)
            .build();
    
    
    /** Constants for table todo_tasks */
    String TABLE_METRIC_SPEED         = "spacecraft_journey_catalog";
    String SPEED_SPACECRAFT_NAME      = "spacecraft_name";
    String SPEED_JOURNEY_ID           = "journey_id";
    String SPEED_SPEED                = "speed";
    String SPEED_TIME                 = "reading_time";
    String SPEED_UNIT                 = "speed_unit";
    SimpleStatement STMT_CREATE_TABLE_METRIC_SPEED = 
            createTable(TABLE_METRIC_SPEED).ifNotExists()
            .withPartitionKey(JOURNEY_SPACECRAFT_NAME, TEXT)
            .withPartitionKey(JOURNEY_ID, TIMEUUID)
            .withColumn(JOURNEY_START, BOOLEAN)
            .withColumn(JOURNEY_END, TEXT)
            .withColumn(JOURNEY_ACTIVE, TEXT)
            .withColumn(JOURNEY_SUMMARY, TEXT)
            .withClusteringOrder(JOURNEY_ID, ClusteringOrder.DESC)
            .build();
    
   

        CREATE TABLE IF NOT EXISTS spacecraft_speed_over_time (
            spacecraft_name text,
            journey_id timeuuid,
            speed double,
            reading_time timestamp,
            speed_unit text,
            PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
        ) WITH CLUSTERING ORDER BY (reading_time DESC);

        CREATE TABLE IF NOT EXISTS spacecraft_temperature_over_time (
            spacecraft_name text,
            journey_id timeuuid,
            temperature double,
            temperature_unit text,
            reading_time timestamp,
            PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
        ) WITH CLUSTERING ORDER BY (reading_time DESC);

        CREATE TABLE IF NOT EXISTS spacecraft_pressure_over_time (
            spacecraft_name text,
            journey_id timeuuid,
            pressure double,
            pressure_unit text,
            reading_time timestamp,
            PRIMARY KEY ((spacecraft_name, journey_id), reading_time)
        ) WITH CLUSTERING ORDER BY (reading_time DESC);
    

}
