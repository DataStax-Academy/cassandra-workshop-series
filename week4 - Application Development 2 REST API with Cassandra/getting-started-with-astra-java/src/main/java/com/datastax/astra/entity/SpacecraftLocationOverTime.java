package com.datastax.astra.entity;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * Bean Mapping table spacecraft_temperature_over_time.
 */
@Entity
@CqlName(SpacecraftLocationOverTime.TABLE_NAME)
public class SpacecraftLocationOverTime extends AbstractInstrumentReading {
    
    /** Constants. */
    public static final String TABLE_NAME           = "spacecraft_location_over_time";
    public static final String COLUMN_LOCATION      = "location";
    public static final String COLUMN_LOCATION_UNIT = "location_unit";
    
    @CqlName(COLUMN_LOCATION)
    private LocationUdt location;
    
    @CqlName(COLUMN_LOCATION_UNIT)
    private String location_unit;

    /**
     * Getter accessor for attribute 'location'.
     *
     * @return
     *       current value of 'location'
     */
    public LocationUdt getLocation() {
        return location;
    }

    /**
     * Setter accessor for attribute 'location'.
     * @param location
     * 		new value for 'location '
     */
    public void setLocation(LocationUdt location) {
        this.location = location;
    }

    /**
     * Getter accessor for attribute 'location_unit'.
     *
     * @return
     *       current value of 'location_unit'
     */
    public String getLocation_unit() {
        return location_unit;
    }

    /**
     * Setter accessor for attribute 'location_unit'.
     * @param location_unit
     * 		new value for 'location_unit '
     */
    public void setLocation_unit(String location_unit) {
        this.location_unit = location_unit;
    }    

}
