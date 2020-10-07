package com.datastax.workshop;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex03_Query5a_Insert_Journey extends TestFixtures implements DataModelConstants {
    
    @Test
    /**
     * CHECK on Astra CQL Console:
     * SELECT * FROM killrvideo.spacecraft_journey_catalog WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=<take output from console>; 
     */
    public void insert_a_journey() {
        // Given

        // When inserting a new
        UUID journeyId = journeyRepo.create(this.TEST_SPACECRAFT, this.TEST_JOURNEYSUMMARY);
        // Validate that journey has been create
        LOGGER.info("Journey created : {}", journeyId);
        LOGGER.info("SUCCESS");
    }    
}
