package com.datastax.workshop;

import java.nio.file.Paths;
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
public class Ex04_Query5b_TakeOff implements DataModelConstants {

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
        cqlSession = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get(DBConnection.SECURE_CONNECT_BUNDLE))
                .withAuthCredentials(DBConnection.USERNAME, DBConnection.PASSWORD)
                .withKeyspace(DBConnection.KEYSPACE)
                .build();
        journeyRepo = new JourneyRepository(cqlSession);
    }

    // ===> WE WILL USE THIS VALUES EVERYWHERE
    public static String SPACECRAFT  = "Crew Dragon Endeavour,SpaceX";
    public static String JOURNEY_ID  = "8dfd0a30-c73b-11ea-b87b-1325d5aaa06b";
    // <=====
    
    @Test
    public void takeoff_the_spacecraft() {
        LOGGER.info("9..8..7..6..5..4..3..2..1 Ignition");
        journeyRepo.takeoff(UUID.fromString(JOURNEY_ID), SPACECRAFT);
        LOGGER.info("Journey {} has now taken off", JOURNEY_ID);
        LOGGER.info("SUCCESS");
        LOGGER.info("========================================");
    }
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
