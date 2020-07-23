package com.datastax.astra.dao;

import java.util.Optional;
import java.util.UUID;

import com.datastax.astra.entity.SpacecraftJourneyCatalog;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * Defining Dao for Spacecraft requests.
 */
@Dao
public interface SpacecraftJourneyDao {
    
    /**
     * Find All spacecraft in the table.
     */
    @Query("SELECT * FROM ${keyspaceId}.${tableId}")
    PagingIterable<SpacecraftJourneyCatalog> findAll();
    
    /**
     * Find all journeys for a spacecraft name.
     * 
     * @param spacecraftName
     *      spacecraftname (Partition key)
     * @return
     *      list of journeys
     */
    @Select(customWhereClause = SpacecraftJourneyCatalog.COLUMN_SPACECRAFT_NAME + "= :spacecraftName")
    PagingIterable<SpacecraftJourneyCatalog> findAllJourneysForSpacecraft(String spacecraftName);
    
    /**
     * Find a journey from its id and a spacecraft name (PK)
     * 
     * @param spacecraftName
     *      spacecraft name
     * @param journeyId
     *      journey unique identifier
     * @return
     *      journey details if it exists or empty
     */
    @Select
    Optional<SpacecraftJourneyCatalog> findById(String spacecraftName, UUID journeyId);
    
    /**
     * Upsert a new journey.
     * 
     * @param spacecraftJourney
     *      bean representing a journey
     * @return
     *      if statement was applied
     */
    @Insert
    boolean upsert(SpacecraftJourneyCatalog spacecraftJourney);

}
