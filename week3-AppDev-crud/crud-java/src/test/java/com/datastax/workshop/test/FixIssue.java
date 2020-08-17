package com.datastax.workshop.test;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.workshop.DBConnection;

/**
 * EXERCISE 2 : Connect to Astra using zip bundle and credentials.
 * 
 * @author Developer Advocate Team
 */
@RunWith(JUnitPlatform.class)
public class FixIssue implements DBConnection {

    /** Logger for the class. */
    private static Logger LOGGER = LoggerFactory.getLogger("Exercise2");
    
    @Test
    @DisplayName("Test connectivity to Astra")
    public void should_connect_to_Astra() {
        
        // When
        try (CqlSession cqlSession = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get(DBConnection.SECURE_CONNECT_BUNDLE))
                .withAuthCredentials(DBConnection.USERNAME, DBConnection.PASSWORD)
                .withKeyspace(DBConnection.KEYSPACE)
                .build()) {
            
            
            
            // Then
            LOGGER.info("Connected with Keyspace {}", cqlSession.getKeyspace().get());
        }
        LOGGER.info("SUCCESS");
        LOGGER.info("========================================");
    }
}
