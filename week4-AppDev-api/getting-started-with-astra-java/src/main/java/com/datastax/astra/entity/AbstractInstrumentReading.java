package com.datastax.astra.entity;

import java.time.Instant;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

/**
 * Mutualized fields for all readings.
 */
public abstract class AbstractInstrumentReading {

    /** Column Names. */
    public static final String COLUMN_SPACECRAFT_NAME = "spacecraft_name";
    public static final String COLUMN_JOURNEY_ID      = "journey_id";
    public static final String COLUMN_READING_TIME    = "reading_time";
    
    @PartitionKey(0)
    @CqlName(COLUMN_SPACECRAFT_NAME)
    private String spacecraft_name;
    
    @PartitionKey(1)
    @CqlName(COLUMN_JOURNEY_ID)
    private UUID journey_id;
    
    @ClusteringColumn
    @CqlName(COLUMN_READING_TIME)
    private Instant reading_time;

    /**
     * Getter accessor for attribute 'spacecraft_name'.
     *
     * @return
     *       current value of 'spacecraft_name'
     */
    public String getSpacecraft_name() {
        return spacecraft_name;
    }

    /**
     * Setter accessor for attribute 'spacecraft_name'.
     * @param spacecraft_name
     * 		new value for 'spacecraft_name '
     */
    public void setSpacecraft_name(String spacecraft_name) {
        this.spacecraft_name = spacecraft_name;
    }

    /**
     * Getter accessor for attribute 'journey_id'.
     *
     * @return
     *       current value of 'journey_id'
     */
    public UUID getJourney_id() {
        return journey_id;
    }

    /**
     * Setter accessor for attribute 'journey_id'.
     * @param journey_id
     * 		new value for 'journey_id '
     */
    public void setJourney_id(UUID journey_id) {
        this.journey_id = journey_id;
    }

    /**
     * Getter accessor for attribute 'reading_time'.
     *
     * @return
     *       current value of 'reading_time'
     */
    public Instant getReading_time() {
        return reading_time;
    }

    /**
     * Setter accessor for attribute 'reading_time'.
     * @param reading_time
     * 		new value for 'reading_time '
     */
    public void setReading_time(Instant reading_time) {
        this.reading_time = reading_time;
    }
    
}
