package com.datastax.astra.dao;

import static com.datastax.astra.entity.AbstractInstrumentReading.COLUMN_JOURNEY_ID;
import static com.datastax.astra.entity.AbstractInstrumentReading.COLUMN_SPACECRAFT_NAME;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.relation.Relation.column;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import com.datastax.astra.entity.SpacecraftLocationOverTime;
import com.datastax.astra.entity.SpacecraftPressureOverTime;
import com.datastax.astra.entity.SpacecraftSpeedOverTime;
import com.datastax.astra.entity.SpacecraftTemperatureOverTime;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;
import com.datastax.oss.protocol.internal.util.Bytes;

/**
 * Implementation of Dynamic queries.
 *
 */
public class SpacecraftInstrumentsQueryProvider {
    
    private CqlSession cqlSession;
    
    /** Helper for bean, tables mappings. */
    private EntityHelper<SpacecraftTemperatureOverTime> ehTemperature;
    private EntityHelper<SpacecraftPressureOverTime>    ehPressure;
    private EntityHelper<SpacecraftLocationOverTime>    ehLocation;
    private EntityHelper<SpacecraftSpeedOverTime>       ehSpeed;
    
    /** Statements Against Astra. */
    private PreparedStatement psInsertTemperatureReading;
    private PreparedStatement psInsertPressureReading;
    private PreparedStatement psInsertLocationReading;
    private PreparedStatement psInsertSpeedReading;
    
    private PreparedStatement psSelectTemperatureReading;
    private PreparedStatement psSelectPressureReading;
    private PreparedStatement psSelectLocationReading;
    private PreparedStatement psSelectSpeedReading;
    
    /**
     * Constructor invoked by the DataStax driver based on Annotation {@link QueryProvider} 
     * set on class {@link SpacecraftInstrumentsDao}.
     * 
     * @param context
     *      context to extrat dse session
     * @param ehTemperature
     *      entity helper to interact with bean {@link SpacecraftTemperatureOverTime}
     * @param ehPressure
     *      entity helper to interact with bean {@link SpacecraftPressureOverTime}
     * @param ehLocation
     *      entity helper to interact with bean {@link SpacecraftLocationOverTime}      
     */
    public SpacecraftInstrumentsQueryProvider(MapperContext context,
            EntityHelper<SpacecraftTemperatureOverTime> ehTemperature,
            EntityHelper<SpacecraftPressureOverTime>    ehPressure,
            EntityHelper<SpacecraftLocationOverTime>    ehLocation,
            EntityHelper<SpacecraftSpeedOverTime>       ehSpeed) {
        this.cqlSession     = context.getSession();
        this.ehTemperature  = ehTemperature;
        this.ehPressure     = ehPressure;
        this.ehSpeed        = ehSpeed;
        this.ehLocation     = ehLocation;
        
        // Leveraging EntityHelper for insert queries
        psInsertTemperatureReading = cqlSession.prepare(ehTemperature.insert().asCql());
        psInsertPressureReading    = cqlSession.prepare(ehPressure.insert().asCql());
        psInsertLocationReading    = cqlSession.prepare(ehLocation.insert().asCql());
        psInsertSpeedReading       = cqlSession.prepare(ehSpeed.insert().asCql());
        
        psSelectTemperatureReading = cqlSession.prepare(
                selectFrom(SpacecraftTemperatureOverTime.TABLE_NAME).all()
                .where(column(COLUMN_SPACECRAFT_NAME).isEqualTo(bindMarker(COLUMN_SPACECRAFT_NAME)))
                .where(column(COLUMN_JOURNEY_ID).isEqualTo(bindMarker(COLUMN_JOURNEY_ID)))
                .build());
        psSelectPressureReading = cqlSession.prepare(
                selectFrom(SpacecraftPressureOverTime.TABLE_NAME).all()
                .where(column(COLUMN_SPACECRAFT_NAME).isEqualTo(bindMarker(COLUMN_SPACECRAFT_NAME)))
                .where(column(COLUMN_JOURNEY_ID).isEqualTo(bindMarker(COLUMN_JOURNEY_ID)))
                .build());
        psSelectSpeedReading = cqlSession.prepare(
                selectFrom(SpacecraftSpeedOverTime.TABLE_NAME).all()
                .where(column(COLUMN_SPACECRAFT_NAME).isEqualTo(bindMarker(COLUMN_SPACECRAFT_NAME)))
                .where(column(COLUMN_JOURNEY_ID).isEqualTo(bindMarker(COLUMN_JOURNEY_ID)))
                .build());
        psSelectLocationReading = cqlSession.prepare(
                selectFrom(SpacecraftLocationOverTime.TABLE_NAME).all()
                .where(column(COLUMN_SPACECRAFT_NAME).isEqualTo(bindMarker(COLUMN_SPACECRAFT_NAME)))
                .where(column(COLUMN_JOURNEY_ID).isEqualTo(bindMarker(COLUMN_JOURNEY_ID)))
                .build());
    }
    
    /**
     * Insert instruments values for a timestamp.
     */
    public void insertInstruments(
            SpacecraftTemperatureOverTime temperature, SpacecraftPressureOverTime pressure,  
            SpacecraftSpeedOverTime speed, SpacecraftLocationOverTime location) {
        cqlSession.executeAsync(BatchStatement.builder(DefaultBatchType.LOGGED)
                .addStatement(bind(psInsertTemperatureReading, temperature, ehTemperature))
                .addStatement(bind(psInsertPressureReading, pressure, ehPressure))
                .addStatement(bind(psInsertSpeedReading, speed, ehSpeed))
                .addStatement(bind(psInsertLocationReading, location, ehLocation))
                .build()).thenApply(rs -> null);
    }
    
    public CompletionStage<Boolean> insertLocationReadingAsync(SpacecraftLocationOverTime[] readings) {
        BatchStatementBuilder myBatch = BatchStatement.builder(DefaultBatchType.LOGGED);
        Arrays.stream(readings).forEach(read -> myBatch.addStatement(bind(psInsertLocationReading, read, ehLocation)));
        return cqlSession.executeAsync(myBatch.build()).thenApply(rs ->  rs.wasApplied());
    }
    
    public CompletionStage<Boolean> insertTemperatureReadingAsync(SpacecraftTemperatureOverTime[] readings) {
        BatchStatementBuilder myBatch = BatchStatement.builder(DefaultBatchType.LOGGED);
        Arrays.stream(readings).forEach(read -> myBatch.addStatement(bind(psInsertTemperatureReading, read, ehTemperature)));
        return cqlSession.executeAsync(myBatch.build()).thenApply(rs -> rs.wasApplied());
    }
    
    public CompletionStage<Boolean> insertPressureReadingAsync(SpacecraftPressureOverTime[] readings) {
        BatchStatementBuilder myBatch = BatchStatement.builder(DefaultBatchType.LOGGED);
        Arrays.stream(readings).forEach(read -> myBatch.addStatement(bind(psInsertPressureReading, read, ehPressure)));
        return cqlSession.executeAsync(myBatch.build()).thenApply(rs -> rs.wasApplied());
    }
    
    public CompletionStage<Boolean> insertSpeedReadingAsync(SpacecraftSpeedOverTime[] readings) {
        BatchStatementBuilder myBatch = BatchStatement.builder(DefaultBatchType.LOGGED);
        Arrays.stream(readings).forEach(read -> myBatch.addStatement(bind(psInsertSpeedReading, read, ehSpeed)));
        return cqlSession.executeAsync(myBatch.build()).thenApplyAsync(rs -> rs.wasApplied());
    }
    
    /**
     * Retrieve Temperature reading for a journey.
     */
    public PagingIterable<SpacecraftTemperatureOverTime> getTemperatureReading(
            String spacecraftName,
            UUID journeyId,
            Optional<Integer> pageSize,
            Optional<String>  pagingState) {
        
        // Detailing operations for the first (next will be much compact)
        
        // (1) - Bind the prepared statement with parameters 
        BoundStatement bsTemperature = psSelectTemperatureReading.bind()
                .setUuid(COLUMN_JOURNEY_ID, journeyId)
                .setString(COLUMN_SPACECRAFT_NAME, spacecraftName);

        // (2) - Update the bound statement to add paging metadata (pageSize, pageState)
        bsTemperature = paging(bsTemperature, pageSize, pagingState);
        
        // (3) - Executing query
        ResultSet resultSet = cqlSession.execute(bsTemperature);
        
        // (4) - Using the entity Help to marshall to expect bean
        return resultSet.map(ehTemperature::get);
    }
    
    /**
     * Retrieve Pressure reading for a journey.
     */
    public PagingIterable<SpacecraftPressureOverTime> getPressureReading(
            String spacecraftName, UUID journeyId, Optional<Integer> pageSize, Optional<String>  pagingState) {
       return cqlSession.execute(paging(psSelectPressureReading.bind()
                       .setUuid(COLUMN_JOURNEY_ID, journeyId)
                       .setString(COLUMN_SPACECRAFT_NAME, spacecraftName), pageSize, pagingState))
               .map(ehPressure::get);
    }
    
    /**
     * Retrieve Location reading for a journey.
     */
    public PagingIterable<SpacecraftLocationOverTime> getLocationReading(
            String spacecraftName, UUID journeyId, Optional<Integer> pageSize, Optional<String>  pagingState) {
       return cqlSession.execute(paging(psSelectLocationReading.bind()
                       .setUuid(COLUMN_JOURNEY_ID, journeyId)
                       .setString(COLUMN_SPACECRAFT_NAME, spacecraftName), pageSize, pagingState))
               .map(ehLocation::get);
    }
    
    /**
     * Retrieve Pressure reading for a journey.
     */
    public PagingIterable<SpacecraftSpeedOverTime> getSpeedReading(
            String spacecraftName, UUID journeyId, Optional<Integer> pageSize, Optional<String>  pagingState) {
       return cqlSession.execute(paging(psSelectSpeedReading.bind()
                       .setUuid(COLUMN_JOURNEY_ID, journeyId)
                       .setString(COLUMN_SPACECRAFT_NAME, spacecraftName), pageSize, pagingState))
               .map(ehSpeed::get);
    }
    
    /**
     * Syntaxic sugar to help with paging
     */
    private BoundStatement paging(BoundStatement bs, Optional<Integer> pageSize, Optional<String>  pagingState) {
        if(pageSize.isPresent()) {
            bs = bs.setPageSize(pageSize.get());
         }
         if (pagingState.isPresent()) {
            bs = bs.setPagingState(Bytes.fromHexString(pagingState.get()));
         };
         return bs;
    }
    
    /**
     * Syntaxic sugar to help with mapping
     */
    public static <T> BoundStatement bind(PreparedStatement preparedStatement, T entity, EntityHelper<T> entityHelper) {
        BoundStatementBuilder boundStatement = preparedStatement.boundStatementBuilder();
        entityHelper.set(entity, boundStatement, NullSavingStrategy.DO_NOT_SET);
        return boundStatement.build();
    }
}
