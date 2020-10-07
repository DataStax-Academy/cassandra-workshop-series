package com.datastax.workshop;

import java.util.Optional;
import java.util.UUID;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex08_Query4b_Read_Journey_Details extends TestFixtures implements DataModelConstants {
   
    @Test
    /*
     * CHECK on Astra CQL Console:
     * SELECT * FROM killrvideo.spacecraft_journey_catalog WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b; 
     */
    public void read_a_journey() {
        Optional<Journey> j = this.findTestJourney();
        
        if (j.isPresent()) {
            Instant takeoffTime = j.get().getStart();
            Instant landingTime = j.get().getEnd();

            LOGGER.info("Journey has been found");
            LOGGER.info("- Uid:\t\t {}", j.get().getId());
            LOGGER.info("- Spacecraft:\t {}", j.get().getSpaceCraft());
            LOGGER.info("- Summary:\t {}", j.get().getSummary());
            LOGGER.info("- Takeoff:\t {}", takeoffTime);
            LOGGER.info("- Landing:\t {}", landingTime);

            Assertions.assertTrue(landingTime.isAfter(takeoffTime), "Landing time should after takeoff");

            LOGGER.info("SUCCESS");
            LOGGER.info("========================================");
        } else {
            LOGGER.info("Journey {} not found, check class 'Ex04_ReadParsePage' or DB", this.TEST_JOURNEYID);
        }
    }

}
