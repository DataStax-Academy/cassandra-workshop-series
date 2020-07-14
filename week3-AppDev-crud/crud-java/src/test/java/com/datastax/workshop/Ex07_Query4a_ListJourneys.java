package com.datastax.workshop;

import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex07_Query4a_ListJourneys implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger("Exercise4");
   
    /** Connect once for all tests. */
    public static CqlSession cqlSession;
    
    @BeforeAll
    public static void initConnection() {
        LOGGER.info("========================================");
        LOGGER.info("Start exercise");
        cqlSession = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get(DBConnection.SECURE_CONNECT_BUNDLE))
                .withAuthCredentials(DBConnection.USERNAME, DBConnection.PASSWORD)
                .withKeyspace(DBConnection.KEYSPACE)
                .build();
    }
    
    @Test
    /*
     * select * from spacecraft_journey_catalog WHERE journey_id=47b04070-c4fb-11ea-babd-17b91da87c10 AND spacecraft_name='DragonCrew,SpaceX';
     */
    public void listJourneys() {
        SimpleStatement stmt = SimpleStatement.builder("select * from spacecraft_journey_catalog where spacecraft_name=?")
                .addPositionalValue(Ex04_Query5b_TakeOff.SPACECRAFT)
                .build();
        ResultSet rs = cqlSession.execute(stmt);
        for (Row row : rs.all()) {
            LOGGER.info("- Journey: {} Summary: {}", 
                    row.getUuid("journey_id"), 
                    row.getString("summary"));
        }
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
