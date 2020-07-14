package com.datastax.workshop;

import java.nio.file.Paths;
import java.util.Optional;
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
public class Ex08_Query4b_Read_Journey_Details implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger("Exercise4");
   
    /** Connect once for all tests. */
    public static CqlSession cqlSession;
    
    /** Use the Repository Pattern. */
    private static JourneyRepository journeyRepo;
    
    @BeforeAll
    public static void initConnection() {
        LOGGER.info("========================================");
        LOGGER.info("Start exercise");
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
    public void read_a_journey() {
        Optional<Journey> j = journeyRepo.find(UUID.fromString(Ex04_Query5b_TakeOff.JOURNEY_ID), Ex04_Query5b_TakeOff.SPACECRAFT);
        if (j.isPresent()) {
            LOGGER.info("Journey has been found");
            LOGGER.info("- Uid:\t\t {}", j.get().getId());
            LOGGER.info("- Spacecraft:\t {}", j.get().getSpaceCraft());
            LOGGER.info("- Summary:\t {}", j.get().getSummary());
            LOGGER.info("- Takeoff:\t {}", j.get().getStart());
            LOGGER.info("- Landing:\t {}", j.get().getEnd());
            LOGGER.info("SUCCESS");
            LOGGER.info("========================================");
        } else {
            LOGGER.info("Journey {} not found, check class 'Ex04_ReadParsePage' or DB", Ex04_Query5b_TakeOff.JOURNEY_ID);
        }
    }
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
