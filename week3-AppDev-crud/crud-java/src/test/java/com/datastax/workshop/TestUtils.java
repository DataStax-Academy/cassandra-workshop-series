package com.datastax.workshop;

import java.net.InetSocketAddress;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Singleton class providing various utilities method
 * Ex: Singleton.getInstance().createKeyspaceForLocalInstance(); 
 */
public class TestUtils {
  private static TestUtils single_instance = null; 

  private static Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
  
  // private constructor to prevent instantiation of this singleton class
  private TestUtils() { }

  public static TestUtils getInstance( ) {
    if (single_instance == null) 
      single_instance = new TestUtils(); 

    return single_instance; 
  }
  
  public void createKeyspaceForLocalInstance() {
      try (CqlSession cqlSession = CqlSession.builder()
              .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
              .withLocalDatacenter("datacenter1")
              .build()) {
          cqlSession.execute(SchemaBuilder.createKeyspace("killrvideo")
                  .ifNotExists().withSimpleStrategy(1)
                  .withDurableWrites(true).build());
      }
  }
  
  /**
   * Read Astra Connection config values from src/main/resources/application.conf
   * These configs are unrelated to Cassandra driver config.
   * We just leverage the HOCON config format of the Typesafe Config framework
   * which is implemented in the DataStax driver to allow us to read the custom config
   * https://docs.datastax.com/en/developer/java-driver/4.6/manual/core/configuration/
   */
  public AstraConnectionInfo getAstraDBConnectInfo() {
    final String configSection = "AstraConnectionAuth";
    AstraConnectionInfo connectInfo = null;

    try {
      // Make sure we see the changes when reloading:
      ConfigFactory.invalidateCaches();

      // Every config file in the classpath, without stripping the prefixes
      Config rootConfig = ConfigFactory.load();

      // The Custom config section added in resources/application.conf:
      Config astraConnCfg = rootConfig.getConfig(configSection);

      String secureBundleFilename = astraConnCfg.getString("SECURE_CONNECT_BUNDLE_FILE");
      String userName = astraConnCfg.getString("USERNAME");
      String clearPwd = astraConnCfg.getString("PASSWORD");
      String keyspaceName = astraConnCfg.getString("KEYSPACE");

      connectInfo = new AstraConnectionInfo(secureBundleFilename, userName, clearPwd, keyspaceName);

    } catch (ConfigException ex) {
      String errMsg = String.format("Missing section %s in src/main/resources/application.conf", configSection);
      LOGGER.error(errMsg);
      ex.printStackTrace();
    }

    return connectInfo;
  }
}
