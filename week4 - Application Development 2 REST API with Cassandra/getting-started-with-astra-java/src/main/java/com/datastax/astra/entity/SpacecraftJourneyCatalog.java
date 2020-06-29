package com.datastax.astra.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Entity representing Table 
 */
@Entity
@CqlName(SpacecraftJourneyCatalog.TABLE_NAME)
public class SpacecraftJourneyCatalog implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 1L;
    
    /** Constants.*/
    public static final String TABLE_NAME             = "spacecraft_journey_catalog";
    public static final String COLUMN_SPACECRAFT_NAME = "spacecraft_name";
    public static final String COLUMN_ID              = "journey_id";
    public static final String COLUMN_START           = "start";
    public static final String COLUMN_END             = "end";
    public static final String COLUMN_ACTIVE          = "active";
    public static final String COLUMN_SUMMARY         = "summary";
    
    @PartitionKey
    @CqlName(COLUMN_SPACECRAFT_NAME)
    @JsonProperty(COLUMN_SPACECRAFT_NAME)
    private String name;
    
    @ClusteringColumn
    @CqlName(COLUMN_ID)
    @JsonProperty(COLUMN_ID)
    private UUID journeyId;
    
    @CqlName(COLUMN_START)
    private Instant start;
    
    @CqlName(COLUMN_END)
    private Instant end;
    
    @CqlName(COLUMN_ACTIVE)
    private Boolean active;
    
    @CqlName(COLUMN_SUMMARY)
    private String summary;
    
    public SpacecraftJourneyCatalog() {}
    
    /**
     * Create table if not exist.
     *
     * @param session
     *      current cql session
     */
    public static void createTable(CqlSession session) {
        session.execute(SchemaBuilder
                    .createTable(CqlIdentifier.fromCql(TABLE_NAME))
                    .ifNotExists()
                    .withPartitionKey(COLUMN_SPACECRAFT_NAME, DataTypes.TEXT)
                    .withClusteringColumn(COLUMN_ID, DataTypes.TIMEUUID)
                    .withColumn(COLUMN_START,   DataTypes.TIMESTAMP)
                    .withColumn(COLUMN_END,     DataTypes.TIMESTAMP)
                    .withColumn(COLUMN_ACTIVE,  DataTypes.BOOLEAN)
                    .withColumn(COLUMN_SUMMARY, DataTypes.TEXT)
                    .withClusteringOrder(COLUMN_ID, ClusteringOrder.DESC)
                    .build());
    }

    /**
     * Getter accessor for attribute 'name'.
     *
     * @return
     *       current value of 'name'
     */
    public String getName() {
        return name;
    }

    /**
     * Setter accessor for attribute 'name'.
     * @param name
     * 		new value for 'name '
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter accessor for attribute 'journeyId'.
     *
     * @return
     *       current value of 'journeyId'
     */
    public UUID getJourneyId() {
        return journeyId;
    }

    /**
     * Setter accessor for attribute 'journeyId'.
     * @param journeyId
     * 		new value for 'journeyId '
     */
    public void setJourneyId(UUID journeyId) {
        this.journeyId = journeyId;
    }

    /**
     * Getter accessor for attribute 'start'.
     *
     * @return
     *       current value of 'start'
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Setter accessor for attribute 'start'.
     * @param start
     * 		new value for 'start '
     */
    public void setStart(Instant start) {
        this.start = start;
    }

    /**
     * Getter accessor for attribute 'end'.
     *
     * @return
     *       current value of 'end'
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * Setter accessor for attribute 'end'.
     * @param end
     * 		new value for 'end '
     */
    public void setEnd(Instant end) {
        this.end = end;
    }

    /**
     * Getter accessor for attribute 'active'.
     *
     * @return
     *       current value of 'active'
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Setter accessor for attribute 'active'.
     * @param active
     * 		new value for 'active '
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Getter accessor for attribute 'summary'.
     *
     * @return
     *       current value of 'summary'
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Setter accessor for attribute 'summary'.
     * @param summary
     * 		new value for 'summary '
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }
     
    
}
