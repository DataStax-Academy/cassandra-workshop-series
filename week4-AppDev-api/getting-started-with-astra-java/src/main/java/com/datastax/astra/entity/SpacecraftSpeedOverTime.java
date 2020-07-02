package com.datastax.astra.entity;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * Bean Mapping table spacecraft_temperature_over_time.
 */
@Entity
@CqlName(SpacecraftSpeedOverTime.TABLE_NAME)
public class SpacecraftSpeedOverTime extends AbstractInstrumentReading {
    
    /** Constants. */
    public static final String TABLE_NAME         = "spacecraft_speed_over_time";
    public static final String COLUMN_SPEED       = "speed";
    public static final String COLUMN_SPEED_UNIT  = "speed_unit";
    
    @CqlName(COLUMN_SPEED)
    private Double speed;
    
    @CqlName(COLUMN_SPEED_UNIT)
    private String speed_unit;

    /**
     * Getter accessor for attribute 'speed'.
     *
     * @return
     *       current value of 'speed'
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * Setter accessor for attribute 'speed'.
     * @param speed
     * 		new value for 'speed '
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * Getter accessor for attribute 'speed_unit'.
     *
     * @return
     *       current value of 'speed_unit'
     */
    public String getSpeed_unit() {
        return speed_unit;
    }

    /**
     * Setter accessor for attribute 'speed_unit'.
     * @param speed_unit
     * 		new value for 'speed_unit '
     */
    public void setSpeed_unit(String speed_unit) {
        this.speed_unit = speed_unit;
    }    

}
