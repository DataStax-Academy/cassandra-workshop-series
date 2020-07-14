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
public class Ex06_Query5d_Landing implements DataModelConstants {

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
    
    @Test
    public void landing_journey() {
        journeyRepo.landing(UUID.fromString(Ex04_Query5b_TakeOff.JOURNEY_ID), Ex04_Query5b_TakeOff.SPACECRAFT);
        LOGGER.info("Journey {} has now landed", Ex04_Query5b_TakeOff.JOURNEY_ID);
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
