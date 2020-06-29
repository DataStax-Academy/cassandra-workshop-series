package com.datastax.astra.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.datastax.astra.dao.SessionManager;
import com.datastax.astra.dao.SpacecraftInstrumentsDao;
import com.datastax.astra.dao.SpacecraftJourneyDao;
import com.datastax.astra.dao.SpacecraftMapper;
import com.datastax.astra.dao.SpacecraftMapperBuilder;
import com.datastax.astra.entity.SpacecraftJourneyCatalog;
import com.datastax.astra.entity.SpacecraftLocationOverTime;
import com.datastax.astra.entity.SpacecraftPressureOverTime;
import com.datastax.astra.entity.SpacecraftSpeedOverTime;
import com.datastax.astra.entity.SpacecraftTemperatureOverTime;
import com.datastax.astra.model.PagedResultWrapper;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.uuid.Uuids;

/**
 * Implementation of Service for controller
 * 
 */
@Component
public class AstraService {

    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AstraService.class);
   
    /** Driver Daos. */
    private SpacecraftJourneyDao     spacecraftJourneyDao;
    private SpacecraftInstrumentsDao spacecraftInstrumentsDao;
    
    /**
     * Find all spacecrafts in the catalog.
     */
    public List< SpacecraftJourneyCatalog > findAllSpacecrafts() {
        // no paging we don't expect more than 5k journeys
        return getSpaceCraftJourneyDao().findAll().all(); 
    }
    
    /**
     * Find all journeys for a spacecraft.
     * 
     * @param spacecraftName
     *      unique spacecraft name (PK)
     * @return
     *      list of journeys
     */
    public List < SpacecraftJourneyCatalog > findAllJourneysForSpacecraft(String spacecraftName) {
        // no paging we don't expect more than 5k journeys
        return getSpaceCraftJourneyDao().findAllJourneysForSpacecraft(spacecraftName).all();
    }
    
    /**
     * Search by primary key, unique record expect.
     *
     * @param spacecraftName
     *      unique spacecraft name (PK)
     * @param journeyid
     *      journey unique identifier
     * @return
     *      journey details if it exists
     */
    public Optional< SpacecraftJourneyCatalog > findJourneyById(String spacecraftName, UUID journeyId) {
        return getSpaceCraftJourneyDao().findById(spacecraftName, journeyId);
    }
    
    /**
     * Create a new {@link SpacecraftJourneyCatalog}.
     *
     * @param spacecraftName
     *       unique spacecraft name (PK)
     * @param summary
     *       short description
     * @return
     *       generated journey id
     */
    public UUID createSpacecraftJourney(String spacecraftName, String summary) {
        UUID journeyUid = Uuids.timeBased();
        LOGGER.debug("Creating journey {} for spacecraft {}", journeyUid, spacecraftName);
        SpacecraftJourneyCatalog dto = new SpacecraftJourneyCatalog();
        dto.setName(spacecraftName);
        dto.setSummary(summary);
        dto.setStart(Instant.now());
        dto.setEnd(Instant.now().plus(1000, ChronoUnit.MINUTES));
        dto.setActive(false);
        dto.setJourneyId(journeyUid);
        getSpaceCraftJourneyDao().upsert(dto);
        return journeyUid;
    }
    
    /**
     * Retrieve temperature readings for a journey.
     *
     * @param spacecraftName
     *      name of spacecrafr
     * @param journeyId
     *      journey identifier
     * @param pageSize
     *      page size
     * @param pageState
     *      page state
     * @return
     *      result page
     */
    public PagedResultWrapper<SpacecraftTemperatureOverTime> getTemperatureReading(
            String spacecraftName, UUID journeyId, 
            Optional<Integer> pageSize, Optional<String> pageState) {
        PagingIterable<SpacecraftTemperatureOverTime> daoResult = 
                getSpaceCraftInstrumentsDao().getTemperatureReading(spacecraftName, journeyId, pageSize, pageState);
        return new PagedResultWrapper<SpacecraftTemperatureOverTime>(daoResult, 
                pageSize.isPresent() ? pageSize.get() : 0);
    }

    /**
     * Create a new {@link SpacecraftTemperatureOverTime} for each item in the array.
     *
     * @param readings
     *       An array unique temperature readings
     * @return
     *       true if successful
     */
    public void insertTemperatureReading(SpacecraftTemperatureOverTime[] readings) {
        long top = System.currentTimeMillis();
        getSpaceCraftInstrumentsDao().insertTemperatureReadingAsync(readings)
                                     .whenComplete((res,ex) -> LOGGER.debug("{} temperature reading(s) inserted in {} millis", 
                                            readings.length, System.currentTimeMillis() - top));
    }

    /**
     * Create a new {@link SpacecraftLocationOverTime} for each item in the array.
     *
     * @param readings
     *       An array unique location readings
     * @return
     *       true if successful
     */
    public void insertLocationReading(SpacecraftLocationOverTime[] readings) {
        long top = System.currentTimeMillis();
        getSpaceCraftInstrumentsDao()
            .insertLocationReadingAsync(readings)
            .whenComplete((res,ex) -> LOGGER.debug("{} location reading(s) inserted in {} millis", 
                                            readings.length, System.currentTimeMillis() - top));
    }

    /**
     * Create a new {@link SpacecraftPressureOverTime} for each item in the array.
     *
     * @param readings
     *       An array unique pressure readings
     * @return
     *       true if successful
     */
    public void insertPressureReading(SpacecraftPressureOverTime[] readings) {
        long top = System.currentTimeMillis();
        getSpaceCraftInstrumentsDao()
            .insertPressureReadingAsync(readings)
            .whenComplete((res,ex) -> LOGGER.debug("{} pressure reading(s) inserted in {} millis", 
                                            readings.length, System.currentTimeMillis() - top));
    }

    /**
     * Create a new {@link SpacecraftSpeedOverTime} for each item in the array.
     *
     * @param readings
     *       An array unique pressure readings
     * @return
     *       true if successful
     */
    public void insertSpeedReading(SpacecraftSpeedOverTime[] readings) {
        long top = System.currentTimeMillis();
        getSpaceCraftInstrumentsDao()
            .insertSpeedReadingAsync(readings)
            .whenComplete((res,ex) -> LOGGER.debug("{} speed reading(s) inserted in {} millis", 
                                            readings.length, System.currentTimeMillis() - top));
    }
    
    /**
     * Retrieve pressure readings for a journey.
     *
     * @param spacecraftName
     *      name of spacecrafr
     * @param journeyId
     *      journey identifier
     * @param pageSize
     *      page size
     * @param pageState
     *      page state
     * @return
     *      result page
     */
    public PagedResultWrapper<SpacecraftPressureOverTime> getPressureReading(
            String spacecraftName, UUID journeyId, 
            Optional<Integer> pageSize, Optional<String> pageState) {
        PagingIterable<SpacecraftPressureOverTime> daoResult = 
                getSpaceCraftInstrumentsDao().getPressureReading(spacecraftName, journeyId, pageSize, pageState);
        return new PagedResultWrapper<SpacecraftPressureOverTime>(daoResult, 
                pageSize.isPresent() ? pageSize.get() : 0);
    }
    
    /**
     * Retrieve speed readings for a journey.
     *
     * @param spacecraftName
     *      name of spacecrafr
     * @param journeyId
     *      journey identifier
     * @param pageSize
     *      page size
     * @param pageState
     *      page state
     * @return
     *      result page
     */
    public PagedResultWrapper<SpacecraftSpeedOverTime> getSpeedReading(
            String spacecraftName, UUID journeyId, 
            Optional<Integer> pageSize, Optional<String> pageState) {
        PagingIterable<SpacecraftSpeedOverTime> daoResult = 
                getSpaceCraftInstrumentsDao().getSpeedReading(spacecraftName, journeyId, pageSize, pageState);
        return new PagedResultWrapper<SpacecraftSpeedOverTime>(daoResult, 
                pageSize.isPresent() ? pageSize.get() : 0);
    }
    
    /**
     * Retrieve speed readings for a journey.
     *
     * @param spacecraftName
     *      name of spacecrafr
     * @param journeyId
     *      journey identifier
     * @param pageSize
     *      page size
     * @param pageState
     *      page state
     * @return
     *      result page
     */
    public PagedResultWrapper<SpacecraftLocationOverTime> getLocationReading(
            String spacecraftName, UUID journeyId, 
            Optional<Integer> pageSize, Optional<String> pageState) {
        PagingIterable<SpacecraftLocationOverTime> daoResult = 
                getSpaceCraftInstrumentsDao().getLocationReading(spacecraftName, journeyId, pageSize, pageState);
        return new PagedResultWrapper<SpacecraftLocationOverTime>(daoResult, 
                pageSize.isPresent() ? pageSize.get() : 0);
    }
    
    protected synchronized SpacecraftJourneyDao getSpaceCraftJourneyDao() {
        if (spacecraftJourneyDao == null) {
            CqlSession cqlSession   = SessionManager.getInstance().connectToAstra();
            SpacecraftMapper mapper = new SpacecraftMapperBuilder(cqlSession).build();
            this.spacecraftJourneyDao = mapper.spacecraftJourneyDao(cqlSession.getKeyspace().get());
        }
        return spacecraftJourneyDao;
    }
    
    protected synchronized SpacecraftInstrumentsDao getSpaceCraftInstrumentsDao() {
        if (spacecraftInstrumentsDao == null) {
            CqlSession cqlSession   = SessionManager.getInstance().connectToAstra();
            SpacecraftMapper mapper = new SpacecraftMapperBuilder(cqlSession).build();
            this.spacecraftInstrumentsDao = mapper.spacecraftInstrumentsDao(cqlSession.getKeyspace().get());
        }
        return spacecraftInstrumentsDao;
    }
    
    /**
     * Properly close CqlSession
     */
    @PreDestroy
    public void cleanUp() {
        SessionManager.getInstance().close();
    }
    
}
