package com.datastax.workshop;

import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

/**
 * EXERCISE 2 : Connect to Astra using zip bundle and credentials.
 * 
 * @author Developer Advocate Team
 */
@RunWith(JUnitPlatform.class)
public class Ex02_Connect_to_Cassandra {

  /** Logger for the class. */
  private static Logger LOGGER = LoggerFactory.getLogger(Ex02_Connect_to_Cassandra.class);

  @Test
  @DisplayName("Test connectivity to Astra")
  public void should_connect_to_Astra() {
      LOGGER.info("========================================");
      LOGGER.info("Start exercise");
      // Given
      AstraConnectionInfo conn = TestUtils.getInstance().getAstraDBConnectInfo();

      LOGGER.info("Astra Connection info configured in src/main/resources/application.conf");
      LOGGER.info(conn.getSecureBundleFilename());
      LOGGER.info("- USERNAME = {}", conn.getUserName());
      LOGGER.info("- PASSWORD = {}", conn.getPassword());
      LOGGER.info("- KEYSPACE = {}", conn.getKeyspace());

      // Then #1
      String failConnInfoMsg = "Please customize Connection info in src/main/resources/application.conf";
      Assertions.assertEquals(conn.getUserName(), "KVUser", failConnInfoMsg);
      Assertions.assertEquals(conn.getPassword(), "KVPassword", failConnInfoMsg);
      Assertions.assertEquals(conn.getKeyspace(), "killrvideo", failConnInfoMsg);

      Assertions.assertTrue((new File(conn.getSecureBundleFilename())).exists(), 
        "File '" + conn.getSecureBundleFilename() + "' NOT FOUND\n"
        + "To run this sample you need to download the secure bundle file from https://astra.datastax.com\n"
        + "More info here: https://docs.datastax.com/en/astra/aws/doc/dscloud/astra/dscloudShareClusterDetails.html");

      // When
      try (CqlSession cqlSession = CqlSession.builder()
        .withCloudSecureConnectBundle(Paths.get(conn.getSecureBundleFilename()))
        .withAuthCredentials(conn.getUserName(), conn.getPassword())
        .withKeyspace(conn.getKeyspace())
        .build()) {
    
        // Then
        Optional<CqlIdentifier> connectedKeyspace = cqlSession.getKeyspace();

        Assertions.assertTrue(
          connectedKeyspace.isPresent(),
          "cqlSession should connect to a valid Keyspace"
        );

        Assertions.assertEquals(
          connectedKeyspace.get().toString(),
          conn.getKeyspace(),
          "Keyspace name should == " + conn.getKeyspace()
        );

        LOGGER.info("Connected with Keyspace {}", connectedKeyspace.get());
      }
      LOGGER.info("SUCCESS");
      LOGGER.info("========================================");
  }

}
