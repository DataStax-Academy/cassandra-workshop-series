package com.datastax.workshop;

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
public class Ex09_Query4c_ReadMetrics extends TestFixtures implements DataModelConstants {

    @Test
    public void read_a_dimension() {
        SimpleStatement stmt = SimpleStatement.builder(
            "SELECT * FROM spacecraft_speed_over_time WHERE spacecraft_name=? AND journey_id=?"
            )
            .addPositionalValue(this.TEST_SPACECRAFT)
            .addPositionalValue(UUID.fromString(this.TEST_JOURNEYID))
            .build();
        
        ResultSet rs = cqlSession.execute(stmt);
        // we fetch everything
        int offset= 0;
        for (Row row : rs.all()) {
            LOGGER.info("idx:{}, time={}, value={}", ++offset, row.getInstant("reading_time"), row.getDouble("speed"));
        }
        LOGGER.info("SUCCESS");
    }
}
