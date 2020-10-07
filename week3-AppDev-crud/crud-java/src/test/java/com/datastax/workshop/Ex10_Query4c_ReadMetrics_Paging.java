package com.datastax.workshop;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex10_Query4c_ReadMetrics_Paging extends TestFixtures implements DataModelConstants {
   
    @Test
    public void read_a_dimension_paging() {
        SimpleStatement stmt = SimpleStatement.builder(
            "SELECT * FROM spacecraft_speed_over_time WHERE spacecraft_name=? AND journey_id=?"
            )
            .addPositionalValue(this.TEST_SPACECRAFT)
            .addPositionalValue(UUID.fromString(this.TEST_JOURNEYID))
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
    }
}
