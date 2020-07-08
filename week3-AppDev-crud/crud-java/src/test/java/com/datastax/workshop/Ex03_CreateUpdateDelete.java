package com.datastax.workshop;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.UUID;

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
public class Ex03_CreateUpdateDelete implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(Ex03_CreateUpdateDelete.class);
    
    /** Work locally or against ASTRA. */
    public static final String CONFIG_ASTRA           = Ex03_CreateUpdateDelete.class.getResource("/application_test.conf").getFile();
    public static final String CONFIG_LOCAL_CASSANDRA = Ex03_CreateUpdateDelete.class.getResource("/application_test_local.conf").getFile();
    
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
    public void insert_a_journey() {
        // Given
        String spaceCraft     = "DragonCrew,SpaceX";
        String journeySummary = "Bring Astronaut to ISS";
        // When inserting a new
        UUID   journeyId      = journeyRepo.create(spaceCraft, journeySummary);
        // Validate that journey has been create
    }
   
    @Test
    public void launch_the_spacecraft() {
        // Set Parameters
        String journeyId   = "2ead2f90-c02b-11ea-9af9-e1abe6976204";
        String spaceCraft  = "DragonCrew,SpaceX";
        // Invoke the tested service
        journeyRepo.takeoff(UUID.fromString(journeyId), spaceCraft);
    }
    
    
    @Test
    public void save_3_readings() {
        UUID journeyId     = UUID.fromString("2ead2f90-c02b-11ea-9af9-e1abe6976204");
        String spaceCraft  = "DragonCrew,SpaceX";
        Instant readingTime = Instant.now();
        double speed        = 10;
        double pressure     = 20;
        double temperature  = 300;
        double x=13,y=14,z=36;
        journeyRepo.log(journeyId, spaceCraft, speed, pressure, temperature, x, y, z, readingTime);
    }
    
    @Test
    public void stop_the_journey() {
        String journeyId   = "2ead2f90-c02b-11ea-9af9-e1abe6976204";
        String spaceCraft  = "DragonCrew,SpaceX";
        journeyRepo.landing(UUID.fromString(journeyId), spaceCraft);
    }
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
