package com.datastax.workshop;

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
public class Ex07_Query4a_ListJourneys extends TestFixtures implements DataModelConstants {
    
    @Test
    /*
     * CHECK on Astra CQL Console:
     * SELECT * FROM killrvideo.spacecraft_journey_catalog WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b; 
     */
    public void listJourneys() {
        SimpleStatement stmt = SimpleStatement.builder(
            "SELECT * FROM spacecraft_journey_catalog WHERE spacecraft_name=?"
            )
            .addPositionalValue(this.TEST_SPACECRAFT)
            .build();

        ResultSet rs = cqlSession.execute(stmt);
        for (Row row : rs.all()) {
            LOGGER.info("- Journey: {} Summary: {}", 
                row.getUuid("journey_id"), 
                row.getString("summary"));
        }
        LOGGER.info("SUCCESS");
    }
}
