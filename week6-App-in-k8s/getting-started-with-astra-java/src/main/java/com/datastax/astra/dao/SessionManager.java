package com.datastax.astra.dao;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.astra.utils.CqlFileUtils;
import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Changing Session Manager to leverage on environment variables.
 *
 * {@link CqlSession} will be created for each call and drop after.
 */
public class SessionManager {
    
    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);
    
    /** Singleton Pattern. */
    private static SessionManager _instance = null;
    
    /** Environment Variables to read. */
    public static final String USE_ASTRA         = "USE_ASTRA";
    public static final String CONNECTION_POINTS = "CONNECTION_POINTS";
    public static final String USERNAME          = "USERNAME";
    public static final String PASSWORD          = "PASSWORD"; 
    public static final String KEYSPACE          = "KEYSPACE";
    public static final String LOCAL_DATACENTER  = "LOCAL_DATACENTER";
    public static final String SECURE_CONNECT_BUNDLE_PATH = "SECURE_CONNECT_BUNDLE_PATH";
    
    /** Status and working session. */
    private CqlSession cqlSession;
    
    /**
     * Utility Method to initialized parameters.
     *
     * @return
     *      singletong of the session Manager
     */
    public static synchronized SessionManager getInstance() {
        if (null == _instance) {
            _instance = new SessionManager();
            // Initialization based on env variables
            Map<String, String> env = System.getenv();
            
            LOGGER.info("Initializing connection to Cassandra/Astra...");
            
            //String sUseAstra              = env.get(USE_ASTRA);
            String sUseAstra              = System.getProperty(USE_ASTRA);
            String sLocalDataCenter       = null;
            String sContactPoints         = null;
            String sCloudSecureBundlePath = null;
            boolean useAstra              = false;
            
            // Username
            String userName = System.getProperty(USERNAME);
            if (null != userName && !"".equals(userName)) {
                LOGGER.info("+ UserName: environment variable '{}' has been read as {}", USERNAME, userName);
            } else {
                userName = "KVUser";
                LOGGER.info("+ Username: environment variable '{}' not found defaulting to '{}'", USERNAME, userName);
            }
            
            // Password
            String password = System.getProperty(PASSWORD);
            if (null != password && !"".equals(password)) {
                LOGGER.info("+ Password: environment variable '{}' has been read.", PASSWORD);
            } else {
                password = "KVPassword";
                LOGGER.info("+ Password: environment variable '{}' not found defaulting to '{}'", PASSWORD, password);
            }
            
            // Keyspace
            String keyspace = System.getProperty(KEYSPACE);
            if (null != password && !"".equals(password)) {
                LOGGER.info("+ Keyspace: environment variable '{}' has been read as {}", KEYSPACE, keyspace);
            } else {
                keyspace = "killrvideo";
                LOGGER.info("+ Keyspace: environment variable '{}' not found defaulting to '{}'", KEYSPACE, keyspace);
            }
            
            // Use Astra
            if (null != sUseAstra && 
               ("false".equalsIgnoreCase(sUseAstra) || "true".equalsIgnoreCase(sUseAstra))) {
                useAstra = Boolean.valueOf(sUseAstra);
                LOGGER.info("+ Environment variable '{}' has been read as '{}'", USE_ASTRA, useAstra);
            } else {
                LOGGER.info("+ UseAstra: environment variable '{}' not found defaulting to 'false'", USE_ASTRA);
            }
                
            
            if (useAstra) {
                
                LOGGER.info("+ Initializing connection to ASTRA");
                
                // Checking required parameters for ASTRA deployments
                sCloudSecureBundlePath = System.getProperty(SECURE_CONNECT_BUNDLE_PATH);
                if (null != sCloudSecureBundlePath && !"".equals(sCloudSecureBundlePath)) {
                    LOGGER.info("+ [astra] secure connect bundle: environment variable '{}' "
                            + "has been read as '{}'", SECURE_CONNECT_BUNDLE_PATH, sCloudSecureBundlePath);
                } else {
                    sCloudSecureBundlePath = "/tmp/secure-connect-killrvideocluster.zip";
                    LOGGER.info("+ [astra] secure connect bundle:  "
                            + "environment variable '{}' not found "
                            + "= defaulting to '{}'", SECURE_CONNECT_BUNDLE_PATH, sCloudSecureBundlePath);
                }
                
                _instance.cqlSession = CqlSession.builder()
                        .withCloudSecureConnectBundle(Paths.get(sCloudSecureBundlePath))
                        .withAuthCredentials(userName, password)
                        .withKeyspace(keyspace).build();
                LOGGER.info("+ Connected to ASTRA");
                
            } else {
                
                LOGGER.info("+ Initializing connection to CASSANDRA THROUGH CONTACT POINTS");
                
                // Checking required parameters for local deployments
                sLocalDataCenter = System.getProperty(LOCAL_DATACENTER);
                if (null != sLocalDataCenter && !"".equals(sLocalDataCenter)) {
                    LOGGER.info("+ [local] localdataCenter: "
                            + "environment variable '{}' has been read {}", LOCAL_DATACENTER, sLocalDataCenter);
                } else {
                    sLocalDataCenter = "datacenter1";
                    LOGGER.info("+ [local] localdataCenter: "
                            + "environment variable '{}' not found defaulting to {}", LOCAL_DATACENTER, sLocalDataCenter);
                }
                
                sContactPoints = System.getProperty(CONNECTION_POINTS);
                if (null != sContactPoints && !"".equals(sContactPoints)) {
                    LOGGER.info("+ [local] contactPoints: "
                            + "environment variable '{}' has been read as '{}'", CONNECTION_POINTS, sContactPoints);
                } else {
                    sContactPoints = "127.0.0.1:9042";
                    LOGGER.info("+ [local] contactPoints: "
                            + "environment variable '{}' not found = defaulting to '{}'", CONNECTION_POINTS, sContactPoints);
                }
                
                _instance.cqlSession = CqlSession.builder()
                        .addContactPoints(parseContactPoints(sContactPoints))
                        .withAuthCredentials(userName, password)
                        .withLocalDatacenter(sLocalDataCenter)
                        .withKeyspace(keyspace).build();
                LOGGER.info("+ Connected to CASSANDRA");
            }
            
            try {
                CqlFileUtils.executeCQLFile(_instance.cqlSession, "schema.cql");
                LOGGER.info("+ Creating tables in keyspace if needed.");
            } catch (FileNotFoundException e) {}
            
            LOGGER.info("Connection Successfully established");
        }
        return _instance;
    }
    
    private static Collection<InetSocketAddress> parseContactPoints(String contactPoints) {
        return Arrays.stream(contactPoints.split(","))
                     .map(node -> {
                             String[] address = node.split(":");
                             return new InetSocketAddress(address[0], Integer.valueOf(address[1]));})
                     .collect(Collectors.toSet());
    }
    
    public CqlSession getCqlSession() {
        return cqlSession;
    }
    
    /**
     * Cleanup session
     */
    public void close() {
        if (null != cqlSession) {
            cqlSession.close();
        }
    }   
    
}
