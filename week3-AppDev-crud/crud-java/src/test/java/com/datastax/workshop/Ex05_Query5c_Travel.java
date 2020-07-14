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
public class Ex05_Query5c_Travel implements DataModelConstants {

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
    public void save_readings() throws InterruptedException {
        for(int i=0;i<50;i++) {
            double speed        = 300+i+Math.random()*10;
            double pressure     = Math.random()*20;
            double temperature  = Math.random()*300;
            double x=13+i,y=14+i,z=36+i;
            Instant readingTime = Instant.now();
            journeyRepo.log(UUID.fromString(Ex04_Query5b_TakeOff.JOURNEY_ID), Ex04_Query5b_TakeOff.SPACECRAFT, 
                    speed, pressure, temperature, x, y, z, readingTime);
            Thread.sleep(200);
            LOGGER.info("{}/50 - travelling..", i);
        }
        LOGGER.info("Reading saved", Ex04_Query5b_TakeOff.JOURNEY_ID);
        LOGGER.info("SUCCESS");
        LOGGER.info("========================================");
        //select * from spacecraft_speed_over_time where spacecraft_name='DragonCrew,SpaceX' AND journey_id=b7fdf670-c5b8-11ea-9d41-49528c2e2634;
    }
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
