package com.datastax.workshop;

import java.io.File;
import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex04_ReadParsePage implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(Ex04_ReadParsePage.class);
    
    /** Work locally or against ASTRA. */
    public static final String CONFIG_ASTRA           = Ex04_ReadParsePage.class.getResource("/application_test.conf").getFile();
    public static final String CONFIG_LOCAL_CASSANDRA = Ex04_ReadParsePage.class.getResource("/application_test_local.conf").getFile();
    
    /** Connect once for all tests. */
    public static CqlSession         cqlSession;
    
    /** Use the Repository Pattern. */
    private static JourneyRepository journeyRepo;
    
    @BeforeAll
    public static void connectToCassandra() {
        // If you run locally you need a keyspace
        try (CqlSession cqlSession = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .build()) {
            cqlSession.execute(SchemaBuilder.createKeyspace("killrvideo")
                    .ifNotExists().withSimpleStrategy(1)
                    .withDurableWrites(true).build());
        }
        DriverConfigLoader configLoader = DriverConfigLoader.fromFile(new File(CONFIG_LOCAL_CASSANDRA));
        cqlSession =  CqlSession.builder().withConfigLoader(configLoader).build();
        LOGGER.info("Connection established to Cassandra.");
        journeyRepo = new JourneyRepository(cqlSession);
    }
    
    @BeforeEach
    public void setupDB() {
        createSchema(cqlSession);
        //truncateTables(cqlSession);
    }
   
    @Test
    public void read_a_journey() {
       // 
    }
   

    @Test
    public void read_a_dimension() {
        //
    }
   
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
