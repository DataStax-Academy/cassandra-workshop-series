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
public class Ex04_Query5b_TakeOff extends TestFixtures implements DataModelConstants {
    
  /**
   * CHECK on Astra CQL Console:
   * Must see: active=true, 'start' has valid timestamp
   * SELECT * FROM killrvideo.spacecraft_journey_catalog WHERE spacecraft_name='Crew Dragon Endeavour,SpaceX' AND journey_id=8dfd0a30-c73b-11ea-b87b-1325d5aaa06b;
   */
  @Test
  public void takeoff_the_spacecraft() {
    // When
    LOGGER.info("9..8..7..6..5..4..3..2..1 Ignition");
    journeyRepo.takeoff(
      UUID.fromString(this.TEST_JOURNEYID),
      this.TEST_SPACECRAFT
    );
    LOGGER.info("Journey {} has now taken off", this.TEST_JOURNEYID);

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

    Assertions.assertTrue(
      journey.get().isActive(),
      "Journey takeoff should set active = true"
    );

    Assertions.assertTrue(
        journey.get().getStart().compareTo(Instant.now()) <= 0,
        "Journey start timestamp should be <= now()"
    );

    LOGGER.info("SUCCESS");
  }
}
