package com.datastax.workshop;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex03_CreateUpdateDelete implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger("Exercise3");
    
    /** Connect once for all tests. */
    public static CqlSession cqlSession;
    
    /** Use the Repository Pattern. */
    private static JourneyRepository journeyRepo;
    
    @BeforeAll
    public static void initConnection() {
        //TestUtils.createKeyspaceForLocalInstance();
        cqlSession = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get(DBConnection.SECURE_CONNECT_BUNDLE))
                .withAuthCredentials(DBConnection.USERNAME, DBConnection.PASSWORD)
                .withKeyspace(DBConnection.KEYSPACE)
                .build();
        journeyRepo = new JourneyRepository(cqlSession);
    }
   
    @Test
    /*
     * select * from spacecraft_journey_catalog WHERE journey_id=47b04070-c4fb-11ea-babd-17b91da87c10 AND spacecraft_name='DragonCrew,SpaceX';
     */
    public void insert_a_journey() {
        // Given
        String spaceCraft     = "DragonCrew,SpaceX";
        String journeySummary = "Bring Astronaut to ISS";
        // When inserting a new
        UUID journeyId = journeyRepo.create(spaceCraft, journeySummary);
        // Validate that journey has been create
        LOGGER.info("Journey created : {}", journeyId);
    }
    
    @Test
    public void takeoff_the_spacecraft() {
        String journeyId   = "47b04070-c4fb-11ea-babd-17b91da87c10";
        String spaceCraft  = "DragonCrew,SpaceX";
        journeyRepo.takeoff(UUID.fromString(journeyId), spaceCraft);
        LOGGER.info("Journey {} has now taken off", journeyId);
    }
    
    @Test
    public void save_readings() {
        UUID journeyId     = UUID.fromString("47b04070-c4fb-11ea-babd-17b91da87c10");
        String spaceCraft  = "DragonCrew,SpaceX";
        Instant readingTime = Instant.now();
        double speed        = 10;
        double pressure     = 20;
        double temperature  = 300;
        double x=13,y=14,z=36;
        journeyRepo.log(journeyId, spaceCraft, speed, pressure, temperature, x, y, z, readingTime);
        LOGGER.info("Reading saved", journeyId);
    }
    
    @Test
    public void landing_journey() {
        String journeyId   = "47b04070-c4fb-11ea-babd-17b91da87c10";
        String spaceCraft  = "DragonCrew,SpaceX";
        journeyRepo.landing(UUID.fromString(journeyId), spaceCraft);
        LOGGER.info("Journey {} has now landed", journeyId);
    }
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
