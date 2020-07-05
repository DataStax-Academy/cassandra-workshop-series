package com.datastax.workshop;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.sample.model.TodoAppSchema;

@RunWith(JUnitPlatform.class)
public class Test05_CreateSchema implements TodoAppSchema {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(Test05_CreateSchema.class);

    @Test
    public void should_create_expected_table() {
        
        // Config loader from file
        DriverConfigLoader loader = DriverConfigLoader.fromFile(
                new File(Test05_CreateSchema.class.getResource("/application_test.conf").getFile()));
        
        // Use it to create the session
        try (CqlSession cqlSession = CqlSession.builder().withConfigLoader(loader).build()) {
        
            LOGGER.info("Connection Established to Astra with Keyspace '{}'", 
                    cqlSession.getKeyspace().get());
            
            // Given a statement
            SimpleStatement stmtCreateTable = SchemaBuilder.createTable(TABLE_TODO_TASKS).ifNotExists()
                    .withPartitionKey(TASK_COL_UID, DataTypes.UUID)
                    .withColumn(TASK_COL_TITLE, DataTypes.TEXT)
                    .withColumn(TASK_COL_COMPLETED, DataTypes.BOOLEAN)
                    .withColumn(TASK_COL_OFFSET, DataTypes.INT)
                    .build();
            
            // When creating the table
            cqlSession.execute(stmtCreateTable);
            
            // Then table is created
            LOGGER.info("Table '{}' has been created (if needed).", TABLE_TODO_TASKS);
        }
    }
}
