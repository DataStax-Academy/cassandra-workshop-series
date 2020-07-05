package com.datastax.workshop;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * Junit5 + Spring.
 * @author Cedrick LUNVEN (@clunven)
 */
@RunWith(JUnitPlatform.class)
public class Test03_CreateUpdateDelete implements DataModelConstants {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(Test03_CreateUpdateDelete.class);
    
    /** Connect once for all tests. */
    public static CqlSession cqlSession;
    
    @BeforeAll
    public static void connectToCassandra() {
        String configFile = Test03_CreateUpdateDelete.class.getResource("/application_test.conf").getFile();
        DriverConfigLoader configLoader = DriverConfigLoader.fromFile(new File(configFile));
        cqlSession =  CqlSession.builder().withConfigLoader(configLoader).build();
        LOGGER.info("Connection established to Cassandra.");
    }
    
    @Test
    public void insert_a_journey() {
        SimpleStatement stmtInsertTask = SimpleStatement.builder(""
                + "INSERT INTO todo_tasks(uid, title, offset, completed)" 
                + "VALUES (?, ?, ?, ?)")
                .addPositionalValue(dto.getUuid())
                .addPositionalValue(dto.getTitle())
                .addPositionalValue(dto.getOrder())
                .addPositionalValue(dto.isCompleted())
                .build();
        cqlSession.execute(stmtInsertTask);
    }
    
    @Test
    public void update_a_journey() {
    }
    
    @Test
    public void delete_a_journey() {
    }
    
    @Test
    public void insert_metrics() {
    }
    
    @AfterAll
    public static void closeConnectionToCassandra() {
    }
}
