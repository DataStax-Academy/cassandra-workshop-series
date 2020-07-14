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
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex09_Query4c_ReadMetrics implements DataModelConstants {

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
    public void read_a_dimension() {
        SimpleStatement stmt = SimpleStatement.builder("select * from spacecraft_speed_over_time where spacecraft_name=? AND journey_id=?")
                .addPositionalValue(Ex04_Query5b_TakeOff.SPACECRAFT)
                .addPositionalValue(UUID.fromString(Ex04_Query5b_TakeOff.JOURNEY_ID))
                .build();
        ResultSet rs = cqlSession.execute(stmt);
        // we fetch everything
        int offset= 0;
        for (Row row : rs.all()) {
            LOGGER.info("idx:{}, time={}, value={}", ++offset, row.getInstant("reading_time"), row.getDouble("speed"));
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
