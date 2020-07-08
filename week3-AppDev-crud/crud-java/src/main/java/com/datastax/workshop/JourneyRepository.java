package com.datastax.workshop;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.datastax.oss.driver.shaded.guava.common.base.Optional;

/**
 * CRUD Operations on a journey.
 *
 * @author Developer Advocates Team
 */
public class JourneyRepository implements DataModelConstants {
    
    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JourneyRepository.class);
    
    /** Interaction with Cassandra. */
    private CqlSession cqlSession;
    
    
    
    
    
    /**
     * Inject Session in constructor.
     *
     * @param cqlSession
     *      cassandra session
     */
    public JourneyRepository(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }
    
    /**
     * Create the statement to create a new journey.
     * 
     * Tips:
     *  - For now we focus on creation, launching the spacecraft will come later
     *  - A journey is active only after launch
     *  - JourneyId is a TimeUUID not a default UUID
     *  
     *  Check result with 
     *  select journey_id, spacecraft_name,summary,start,end,active from killrvideo.spacecraft_journey_catalog; 
     */
    public UUID create(String spacecraft, String journeySummary) {
        String insertQuery = 
                "INSERT INTO spacecraft_journey_catalog (spacecraft_name, journey_id, active, summary) "
              + "VALUES(?,?,?,?)";
        UUID journeyId =  Uuids.timeBased();
        cqlSession.execute(SimpleStatement.builder(insertQuery)
                .addPositionalValue(spacecraft)
                .addPositionalValue(journeyId)
                .addPositionalValue(Boolean.FALSE)
                .addPositionalValue(journeySummary)
                .build());
        LOGGER.info("Journey with id '{}' has been inserted in DB", journeyId);
        return journeyId;
    }
    
    /**
     * Create the statement to takeoff the spacecraft.
     * 
     * Tips:
     *  - We need the full primnary key in where clause (journey, spacecraft)
     *  - We need to update both the active and start date
     *  
     *  Check result with 
     *  select journey_id, spacecraft_name,summary,start,end,active from killrvideo.spacecraft_journey_catalog; 
     */
    public void takeoff(UUID journeyId, String spacecraft) {
        cqlSession.execute(SimpleStatement.builder(
                "UPDATE spacecraft_journey_catalog "
                + "SET active=true, start=? "
                + "WHERE spacecraft_name=? AND journey_id=?")
                .addPositionalValue(Instant.now())
                .addPositionalValue(spacecraft)
                .addPositionalValue(journeyId)
                .build());
    }
    
    /**
     * Save a few readings 
     * 
     * Tips:
     *  - We would like a batch statement (edge computing)
     *  - We need to have all table recording the same reading_time
     *  - Timestamps are Instant in the Java world 
     *  
     *  Check result with 
     *  select journey_id, spacecraft_name,summary,start,end,active from killrvideo.spacecraft_journey_catalog; 
     */
    public void log(UUID journeyId, String spacecraft, double speed, 
            double pressure, double temperature, 
            double x, double y, double z, Instant readTime) {
        BatchStatementBuilder bb = new BatchStatementBuilder(BatchType.LOGGED);
        bb.addStatement(SimpleStatement.builder(
           "INSERT INTO spacecraft_speed_over_time ("
           + "spacecraft_name,journey_id,"
           + "speed,reading_time,speed_unit) "
           + "VALUES (?,?,?,?,?)")
            .addPositionalValue(spacecraft)
            .addPositionalValue(journeyId)
            .addPositionalValue(speed)
            .addPositionalValue(readTime)
            .addPositionalValue("km/hour")
            .build());
        bb.addStatement(SimpleStatement.builder(
           "INSERT INTO spacecraft_temperature_over_time ("
           + "spacecraft_name,journey_id,"
           + "temperature,reading_time,temperature_unit) "
           + "VALUES (?,?,?,?,?)")
            .addPositionalValue(spacecraft)
            .addPositionalValue(journeyId)
            .addPositionalValue(temperature)
            .addPositionalValue(readTime)
            .addPositionalValue("K")
            .build());
        bb.addStatement(SimpleStatement.builder(
                "INSERT INTO spacecraft_pressure_over_time ("
                + "spacecraft_name,journey_id,"
                + "pressure,reading_time,pressure_unit) "
                + "VALUES (?,?,?,?,?)")
                 .addPositionalValue(spacecraft)
                 .addPositionalValue(journeyId)
                 .addPositionalValue(pressure)
                 .addPositionalValue(readTime)
                 .addPositionalValue("Pa")
                 .build());
        
        // UDT
        UserDefinedType udtlocation = cqlSession.getMetadata()
                .getKeyspace(cqlSession.getKeyspace().get())
                .flatMap(ks -> ks.getUserDefinedType("location_udt"))
                .orElseThrow(() -> new IllegalArgumentException("Missing UDT 'location_udt'"));
        UdtValue location = udtlocation.newValue();
        location.setDouble("x_coordinate", x);
        location.setDouble("y_coordinate", y);
        location.setDouble("z_coordinate", z);
        bb.addStatement(SimpleStatement.builder(
                "INSERT INTO spacecraft_location_over_time ("
                + "spacecraft_name,journey_id,"
                + "location,reading_time,location_unit) "
                + "VALUES (?,?,?,?,?)")
                 .addPositionalValue(spacecraft)
                 .addPositionalValue(journeyId)
                 .addPositionalValue(location)
                 .addPositionalValue(readTime)
                 .addPositionalValue("AU")
                 .build());
        cqlSession.execute(bb.build());
    }
    
    public void landing(UUID journeyId, String spacecraft) {
        cqlSession.execute(SimpleStatement.builder(
                "UPDATE spacecraft_journey_catalog "
                + "SET active=false, end=? "
                + "WHERE spacecraft_name=? AND journey_id=?")
                .addPositionalValue(Instant.now())
                .addPositionalValue(spacecraft)
                .addPositionalValue(journeyId)
                .build());
    }
    
    public void delete(UUID journeyId, String spacecraft) {
        BatchStatementBuilder bb = new BatchStatementBuilder(BatchType.LOGGED);
        // Delete the journey
        bb.addStatements(SimpleStatement.builder(
                "DELETE FROM spacecraft_journey_catalog "
                + "WHERE spacecraft_name=? AND journey_id=?")
                .addPositionalValue(spacecraft)
                .addPositionalValue(journeyId)
                .build());
        // Delete all relevant metrics per partition
        bb.addStatement(SimpleStatement.builder(
           "DELETE FROM spacecraft_speed_over_time ("
         + "WHERE spacecraft_name=? AND journey_id=?")
            .addPositionalValue(spacecraft)
            .addPositionalValue(journeyId)
            .build());
        bb.addStatement(SimpleStatement.builder(
                "DELETE FROM spacecraft_location_over_time ("
              + "WHERE spacecraft_name=? AND journey_id=?")
                 .addPositionalValue(spacecraft)
                 .addPositionalValue(journeyId)
                 .build());
        bb.addStatement(SimpleStatement.builder(
                "DELETE FROM spacecraft_pressure_over_time ("
              + "WHERE spacecraft_name=? AND journey_id=?")
                 .addPositionalValue(spacecraft)
                 .addPositionalValue(journeyId)
                 .build());
        bb.addStatement(SimpleStatement.builder(
                "DELETE FROM spacecraft_temperature_over_time ("
              + "WHERE spacecraft_name=? AND journey_id=?")
                 .addPositionalValue(spacecraft)
                 .addPositionalValue(journeyId)
                 .build());
        cqlSession.execute(bb.build());
    }
    
    public Optional<Journey> find(UUID journeyId, String spacecraft) {
        ResultSet rs = cqlSession.execute(SimpleStatement.builder(
                "SELECT * FROM spacecraft_journey_catalog "
               + "WHERE spacecraft_name=? AND journey_id=?")
                .addPositionalValue(spacecraft)
                .addPositionalValue(journeyId)
                .build());
        // We query with full primary key
        return Optional.fromNullable(mapJourney(rs.one()));
    }
    
    public static  Journey mapJourney(Row row) {
        if (null == row) return null;
        Journey j = new Journey();
        j.setSpaceCraft(row.getString(JOURNEY_SPACECRAFT_NAME));
        j.setSummary(row.getString(JOURNEY_SUMMARY));
        j.setActive(row.getBoolean(JOURNEY_ACTIVE));
        j.setStart(row.getInstant(JOURNEY_START));
        j.setEnd(row.getInstant(JOURNEY_END));
        j.setId(row.getUuid(JOURNEY_ID));
        return j;
    }

}
