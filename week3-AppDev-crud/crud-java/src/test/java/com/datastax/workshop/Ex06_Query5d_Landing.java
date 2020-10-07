package com.datastax.workshop;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Let's play !
 */ 
@RunWith(JUnitPlatform.class)
public class Ex06_Query5d_Landing extends TestFixtures implements DataModelConstants {
    
  @Test
  /**
   * CHECK on Astra CQL Console:
   * Must see: active=false, 'end' has valid timestamp
   * SELECT * FROM killrvideo.spacecraft_journey_catalog WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b;
   */
  public void landing_journey() {
    // When
    journeyRepo.landing(
      UUID.fromString(this.TEST_JOURNEYID),
      this.TEST_SPACECRAFT
    );
    LOGGER.info("Journey {} has now landed", this.TEST_JOURNEYID);

    // Then
    Optional<Journey> journey = this.findTestJourney();

    Assertions.assertTrue(journey.isPresent(),
      String.format(
        "spacecraft_journey_catalog should have a row with\n" +
          "spacecraft_name = '%s'" +
          "journey_id = %s",
        this.TEST_SPACECRAFT,
        this.TEST_JOURNEYID)
    );

    Assertions.assertFalse(
      journey.get().isActive(),
      "Journey takeoff should set active = false"
    );

    Instant startTimestamp = journey.get().getStart();
    Instant endTimestamp   = journey.get().getEnd();

    Assertions.assertTrue(
    endTimestamp.compareTo(Instant.now()) <= 0,
    "Journey end timestamp should be <= now()"
    );

    Assertions.assertTrue(
      endTimestamp.isAfter(startTimestamp),
      String.format(
        "Journey end timestamp (%s) should be < start timestamp (%s)",
        endTimestamp.toString(),
        startTimestamp.toString())
    );

    LOGGER.info("SUCCESS");
  }
}
