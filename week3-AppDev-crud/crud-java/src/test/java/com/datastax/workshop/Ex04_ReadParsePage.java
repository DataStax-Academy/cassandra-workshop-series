package com.datastax.workshop;

import java.nio.file.Paths;
import java.util.Optional;

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
public class Ex04_ReadParsePage implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(Ex04_ReadParsePage.class);
   
    /** Connect once for all tests. */
    public static CqlSession         cqlSession;
    
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
    public void read_a_journey() {
        String journeyId   = "47b04070-c4fb-11ea-babd-17b91da87c10";
        String spaceCraft  = "DragonCrew,SpaceX";
       Optional<Journey> j = journeyRepo.find(journeyId, spaceCraft);
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
