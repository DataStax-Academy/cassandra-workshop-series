package com.datastax.workshop;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex05_Query5c_Travel extends TestFixtures implements DataModelConstants {
   
    /**
     * CHECK on Astra CQL Console:
     * SELECT * FROM killrvideo.spacecraft_speed_over_time WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b; 
     * SELECT * FROM killrvideo.spacecraft_temperature_over_time WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b; 
     * SELECT * FROM killrvideo.spacecraft_pressure_over_time WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b; 
     * SELECT * FROM killrvideo.spacecraft_location_over_time WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b; 
     */
    @Test
    public void save_readings() throws InterruptedException {
        // delete previous readings to make this test idempotent
        // Reason: the sensor readings have PK = ((spacecraft, journeyId), reading_time)
        // Each time this test is executed, 50 rows are inserted (and not upserted) in eash sensor table
        // As a results, when this test is executed several times, this makes the verification
        // a bit complex as there will be many rows (4 test runs = 200 rows per sensor table)
        LOGGER.info("Delete previous sensors readings ...");
        journeyRepo.deleteSensorReadings(
            this.TEST_SPACECRAFT,
            UUID.fromString(this.TEST_JOURNEYID)
        );

        LOGGER.info("Record new sensors readings ...");
        for(int i=0;i<50;i++) {
            double speed        = 300+i+Math.random()*10;
            double pressure     = Math.random()*20;
            double temperature  = Math.random()*300;
            double x=13+i,y=14+i,z=36+i;
            Instant readingTime = Instant.now();
            journeyRepo.log(UUID.fromString(this.TEST_JOURNEYID), this.TEST_SPACECRAFT, 
                    speed, pressure, temperature, x, y, z, readingTime);
            Thread.sleep(200);
            LOGGER.info("{}/50 - travelling..", i);
        }
        LOGGER.info("Reading saved", this.TEST_JOURNEYID);
        LOGGER.info("SUCCESS");
    }
}
