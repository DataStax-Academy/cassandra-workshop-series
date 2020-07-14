package com.datastax.workshop;

import java.nio.file.Paths;
import java.util.UUID;

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
public class Ex03_Query5a_Insert_Journey implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger("Exercise3");
    
    /** Connect once for all tests. */
    public static CqlSession cqlSession;
    
    /** Use the Repository Pattern. */
    private static JourneyRepository journeyRepo;
    
    @BeforeAll
    public static void initConnection() {
        LOGGER.info("========================================");
        LOGGER.info("Start exercise");
        //TestUtils.createKeyspaceForLocalInstance();
        cqlSession = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get(DBConnection.SECURE_CONNECT_BUNDLE))
                .withAuthCredentials(DBConnection.USERNAME, DBConnection.PASSWORD)
                .withKeyspace(DBConnection.KEYSPACE)
                .build();
        journeyRepo = new JourneyRepository(cqlSession);
    }
   
    @Test
    public void insert_a_journey() {
        // Given
        String spaceCraft     = "Crew Dragon Endeavour,SpaceX";
        String journeySummary = "Bring Astronauts to ISS";
        // When inserting a new
        UUID journeyId = journeyRepo.create(spaceCraft, journeySummary);
        // Validate that journey has been create
        LOGGER.info("Journey created : {}", journeyId);
        LOGGER.info("SUCCESS");
        LOGGER.info("========================================");
    }
    
    /*
     * select * from spacecraft_journey_catalog WHERE journey_id=47b04070-c4fb-11ea-babd-17b91da87c10 AND spacecraft_name='DragonCrew,SpaceX';
     */
    
}
