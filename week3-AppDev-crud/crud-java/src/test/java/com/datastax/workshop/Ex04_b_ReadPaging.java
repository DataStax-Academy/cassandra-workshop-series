package com.datastax.workshop;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Iterator;
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
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex04_b_ReadPaging implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger("Exercise4");
   
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
    public void read_a_journey() {
        Optional<Journey> j = journeyRepo.find(UUID.fromString(Ex03_b_TakeOff.JOURNEY_ID), Ex03_b_TakeOff.SPACECRAFT);
        if (j.isPresent()) {
            LOGGER.info("Journey has been found");
            LOGGER.info("- Uid:\t\t {}", j.get().getId());
            LOGGER.info("- Spacecraft:\t {}", j.get().getSpaceCraft());
            LOGGER.info("- Summary:\t {}", j.get().getSummary());
            LOGGER.info("- Takeoff:\t {}", j.get().getStart());
            LOGGER.info("- Landing:\t {}", j.get().getEnd());
        } else {
            LOGGER.info("Journey {} not found, check class 'Ex04_ReadParsePage' or DB", Ex03_b_TakeOff.JOURNEY_ID);
        }
    }

    @Test
    public void read_a_dimension() {
        SimpleStatement stmt = SimpleStatement.builder("select * from spacecraft_speed_over_time where spacecraft_name=? AND journey_id=?")
                .addPositionalValue(spaceCraft)
                .addPositionalValue(UUID.fromString(journeyId))
                .build();
        ResultSet rs = cqlSession.execute(stmt);
        // we fetch everything
        int offset= 0;
        for (Row row : rs.all()) {
            LOGGER.info("idx:{}, time={}, value={}", ++offset, row.getInstant("reading_time"), row.getDouble("speed"));
        }
    }
    
    @Test
    public void read_a_dimension_paging() {
        SimpleStatement stmt = SimpleStatement.builder("select * from spacecraft_speed_over_time where spacecraft_name=? AND journey_id=?")
                .addPositionalValue(spaceCraft)
                .addPositionalValue(UUID.fromString(journeyId))
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
        
    }
    
   
    
    
    @AfterAll
    public static void closeConnectionToCassandra() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }
}
