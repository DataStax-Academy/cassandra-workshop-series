package com.datastax.workshop;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Iterator;
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
import com.datastax.oss.protocol.internal.util.Bytes;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex10_Query4c_ReadMetrics_Paging implements DataModelConstants {

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
    public void read_a_dimension_paging() {
        SimpleStatement stmt = SimpleStatement.builder("select * from spacecraft_speed_over_time where spacecraft_name=? AND journey_id=?")
                .addPositionalValue(Ex04_Query5b_TakeOff.SPACECRAFT)
                .addPositionalValue(UUID.fromString(Ex04_Query5b_TakeOff.JOURNEY_ID))
                .build();

        // Set page to 10
        stmt = stmt.setPageSize(10);
        
        ResultSet rs = cqlSession.execute(stmt);
        ByteBuffer pagingStateAsBytes = rs.getExecutionInfo().getPagingState();
        
        // we fetch everything
        int items = rs.getAvailableWithoutFetching();
        LOGGER.info("Page1: {}", items);
        Iterator<Row> rows = rs.iterator();
        for (int offset=0;offset < items;offset++) {
            Row row = rows.next();
            LOGGER.info("- time={}, value={}",row.getInstant("reading_time"), row.getDouble("speed"));
        }
        // Here is if you NEXT THE DRIVERS WILL FETCH page 2
        
        // We can go directly to page2 with 
       
        stmt = stmt.setPagingState(pagingStateAsBytes);
        rs = cqlSession.execute(stmt);
        items = rs.getAvailableWithoutFetching();
        LOGGER.info("Page2: {}", items);
        rows = rs.iterator();
        for (int offset=0; offset < items; offset++) {
            Row row = rows.next();
            LOGGER.info("- time={}, value={}",row.getInstant("reading_time"), row.getDouble("speed"));
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
