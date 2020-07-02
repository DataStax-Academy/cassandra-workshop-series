package com.datastax.astra.entity;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * Bean Mapping table spacecraft_temperature_over_time.
 */
@Entity
@CqlName(SpacecraftTemperatureOverTime.TABLE_NAME)
public class SpacecraftTemperatureOverTime extends AbstractInstrumentReading {
    
    /** Constants. */
    public static final String TABLE_NAME               = "spacecraft_temperature_over_time";
    public static final String COLUMN_TEMPERATURE       = "temperature";
    public static final String COLUMN_TEMPERATURE_UNIT  = "temperature_unit";
    
    @CqlName(COLUMN_TEMPERATURE)
    private Double temperature;
    
    @CqlName(COLUMN_TEMPERATURE_UNIT)
    private String temperature_unit;

    /**
     * Getter accessor for attribute 'temperature'.
     *
     * @return
     *       current value of 'temperature'
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * Setter accessor for attribute 'temperature'.
     * @param temperature
     * 		new value for 'temperature '
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * Getter accessor for attribute 'temperature_unit'.
     *
     * @return
     *       current value of 'temperature_unit'
     */
    public String getTemperature_unit() {
        return temperature_unit;
    }

    /**
     * Setter accessor for attribute 'temperature_unit'.
     * @param temperature_unit
     * 		new value for 'temperature_unit '
     */
    public void setTemperature_unit(String temperature_unit) {
        this.temperature_unit = temperature_unit;
    }

}
