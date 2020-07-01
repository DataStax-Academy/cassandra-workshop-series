package com.datastax.workshop;

import java.io.File;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

/**
 * First Connectivity test with Astra.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@RunWith(JUnitPlatform.class)
public class Test02_ConnectWithConfigFile {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger(Test02_ConnectWithConfigFile.class);
    
    @Test
    @DisplayName("Test connectivity to Astra delegate file")
    public void should_connect_to_Astra_withConfig() {
        String path = Test02_ConnectWithConfigFile.class.getResource("/application_test.conf").getFile();
        LOGGER.info("Loading file '{}'", path);
        File configFile = new File(path);
        LOGGER.info("File exist ? '{}'", configFile);
        DriverConfigLoader loader = DriverConfigLoader.fromFile(configFile);
        LOGGER.info("Configuration Loaded");
        try (CqlSession cqlSession = CqlSession.builder().withConfigLoader(loader).build()) {
            LOGGER.info("Connection Established to Astra with Keyspace {}", cqlSession.getKeyspace().get());
        }
    }
    
    /Users/cedricklunven/dev/WORKSPACES/cassandra-workshop-series/week3%20-%20Application%20Development%201%20CRUD%20with%20Cassandra/crud-java/target/test-classes/application_test.conf


    

}
