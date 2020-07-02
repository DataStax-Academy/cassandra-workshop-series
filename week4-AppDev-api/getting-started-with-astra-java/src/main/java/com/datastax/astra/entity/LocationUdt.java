package com.datastax.astra.entity;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

/**
 * Bean for Location UDT.s
 */
@Entity
@CqlName(LocationUdt.UDT_TYPE_NAME)
public class LocationUdt {
    
    /** Constants for UDT. */
    public static final String UDT_TYPE_NAME = "location";
    public static final String XCOORDINATE   = "x_coordinate";
    public static final String YCOORDINATE   = "y_coordinate";
    public static final String ZCOORDINATE   = "z_coordinate";
    
    @CqlName(XCOORDINATE)
    private double x_coordinate;
    
    @CqlName(YCOORDINATE)
    private double y_coordinate;
    
    @CqlName(ZCOORDINATE)
    private double z_coordinate;
    
    /**
     * Default constructor.
     */
    public LocationUdt() {}
    
    /**
     * Init location with coordinates.
     */
    public LocationUdt(double x, double y, double z) {
        this.x_coordinate = x;
        this.y_coordinate = y;
        this.z_coordinate = z;
    }

    /**
     * Getter accessor for attribute 'x_coordinate'.
     *
     * @return
     *       current value of 'x_coordinate'
     */
    public double getX_coordinate() {
        return x_coordinate;
    }

    /**
     * Setter accessor for attribute 'x_coordinate'.
     * @param x_coordinate
     * 		new value for 'x_coordinate '
     */
    public void setX_coordinate(double x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    /**
     * Getter accessor for attribute 'y_coordinate'.
     *
     * @return
     *       current value of 'y_coordinate'
     */
    public double getY_coordinate() {
        return y_coordinate;
    }

    /**
     * Setter accessor for attribute 'y_coordinate'.
     * @param y_coordinate
     * 		new value for 'y_coordinate '
     */
    public void setY_coordinate(double y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    /**
     * Getter accessor for attribute 'z_coordinate'.
     *
     * @return
     *       current value of 'z_coordinate'
     */
    public double getZ_coordinate() {
        return z_coordinate;
    }

    /**
     * Setter accessor for attribute 'z_coordinate'.
     * @param z_coordinate
     * 		new value for 'z_coordinate '
     */
    public void setZ_coordinate(double z_coordinate) {
        this.z_coordinate = z_coordinate;
    }
    

}
