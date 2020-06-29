package com.datastax.astra.dao;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.datastax.astra.entity.SpacecraftLocationOverTime;
import com.datastax.astra.entity.SpacecraftPressureOverTime;
import com.datastax.astra.entity.SpacecraftSpeedOverTime;
import com.datastax.astra.entity.SpacecraftTemperatureOverTime;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * Operation to work with instruments
 */
@Dao
public interface SpacecraftInstrumentsDao {
    
    /**
     * Search for temperature readings with MAPPER
     */
    @Select(customWhereClause = "spacecraft_name= :spacecraftName AND journey_id= :journeyId")
    PagingIterable<SpacecraftTemperatureOverTime> getTemperatureReading(
            String spacecraftName, UUID JourneyId,
            Function<BoundStatementBuilder, BoundStatementBuilder> setAttributes);
    
    /**
     * Search for temperature readings with QUERY PROVIDER
     */
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
       entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                         SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    PagingIterable<SpacecraftTemperatureOverTime> getTemperatureReading(
            String spacecraftName, UUID JourneyId, 
            Optional<Integer> pageSize, 
            Optional<String> pagingState);
    
    /**
     * Upsert a temperature reading.
     *
     * @param reading
     *      The temperature reading
     * @return
     *      if statement was applied
     */
    @Insert
    boolean upsertTemperature(SpacecraftTemperatureOverTime reading);
    
    /**
     * Bulk inserts of temperature readings.
     *
     * @param reading
     *      The temperature readings
     */
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
            entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                              SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    CompletionStage<Boolean> insertTemperatureReadingAsync(SpacecraftTemperatureOverTime[] readings);
    
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
            entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                              SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    CompletionStage<Boolean> insertLocationReadingAsync(SpacecraftLocationOverTime[] readings);
    
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
            entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                              SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    CompletionStage<Boolean> insertPressureReadingAsync(SpacecraftPressureOverTime[] readings);
    
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
            entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                              SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    CompletionStage<Boolean> insertSpeedReadingAsync(SpacecraftSpeedOverTime[] readings);
    
    
    

    /**
     * Upsert a location reading.
     *
     * @param reading
     *      The location reading
     * @return
     *      if statement was applied
     */
    @Insert
    boolean upsertLocation(SpacecraftLocationOverTime reading);

    /**
     * Upsert a pressure reading.
     *
     * @param reading
     *      The pressure reading
     * @return
     *      if statement was applied
     */
    @Insert
    boolean upsertPressure(SpacecraftPressureOverTime reading);

    /**
     * Upsert a speed reading.
     *
     * @param reading
     *      The speed reading
     * @return
     *      if statement was applied
     */
    @Insert
    boolean upsertSpeed(SpacecraftSpeedOverTime reading);

    /**
     * Search for pressure readings.
     */
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
       entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                         SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    PagingIterable<SpacecraftPressureOverTime> getPressureReading(
            String spacecraftName, UUID JourneyId, Optional<Integer> pageSize, Optional<String> pagingState);
    
    /**
     * Search for speed readings.
     */
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
       entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                         SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    PagingIterable<SpacecraftSpeedOverTime> getSpeedReading(
            String spacecraftName, UUID JourneyId, Optional<Integer> pageSize, Optional<String> spagingState);
    
    /**
     * Search for location readings.
     */
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
       entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                         SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    PagingIterable<SpacecraftLocationOverTime> getLocationReading(
            String spacecraftName, UUID JourneyId, Optional<Integer> pageSize, Optional<String> pagingState);
    
    /**
     * Insert instruments.
     */
    @QueryProvider(providerClass = SpacecraftInstrumentsQueryProvider.class, 
            entityHelpers = { SpacecraftTemperatureOverTime.class, SpacecraftPressureOverTime.class, 
                              SpacecraftLocationOverTime.class, SpacecraftSpeedOverTime.class})
    void insertInstruments(
            SpacecraftTemperatureOverTime temperature, SpacecraftPressureOverTime pressure,  
            SpacecraftSpeedOverTime speed, SpacecraftLocationOverTime location);
}
