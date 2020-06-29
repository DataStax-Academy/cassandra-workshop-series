package com.datastax.astra.entity;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * Bean Mapping table spacecraft_temperature_over_time.
 */
@Entity
@CqlName(SpacecraftPressureOverTime.TABLE_NAME)
public class SpacecraftPressureOverTime extends AbstractInstrumentReading {
    
    /** Constants. */
    public static final String TABLE_NAME            = "spacecraft_pressure_over_time";
    public static final String COLUMN_PRESSURE       = "pressure";
    public static final String COLUMN_PRESSURE_UNIT  = "pressure_unit";
    
    @CqlName(COLUMN_PRESSURE)
    private Double pressure;
    
    @CqlName(COLUMN_PRESSURE_UNIT)
    private String pressure_unit;

    /**
     * Getter accessor for attribute 'pressure'.
     *
     * @return
     *       current value of 'pressure'
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * Setter accessor for attribute 'pressure'.
     * @param pressure
     * 		new value for 'pressure '
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * Getter accessor for attribute 'pressure_unit'.
     *
     * @return
     *       current value of 'pressure_unit'
     */
    public String getPressure_unit() {
        return pressure_unit;
    }

    /**
     * Setter accessor for attribute 'pressure_unit'.
     * @param pressure_unit
     * 		new value for 'pressure_unit '
     */
    public void setPressure_unit(String pressure_unit) {
        this.pressure_unit = pressure_unit;
    }

}
