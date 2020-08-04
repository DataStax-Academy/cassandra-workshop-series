package com.datastax.astra.dao;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.astra.utils.CqlFileUtils;
import com.datastax.oss.driver.api.core.CqlSession;

/**
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
    
    /** Connectivity Attributes. */
    public static boolean useAstra = true;
    
    private String  userName;
    private String  password;
    private String  keySpace;
    private String  secureConnectionBundlePath;
    
    /** Status and working session. */
    private boolean initialized = false;
    
    private CqlSession cqlSession;
    
    public static final String QUERY_HEALTH_CHECK = "select data_center from system.local";
    
    /**
     * Utility Method to initialized parameters.
     *
     * @return
     *      singletong of the session Manager
     */
    public static synchronized SessionManager getInstance() {
        if (null == _instance) {
            _instance = new SessionManager();
            
            if (!useAstra) {
                LOGGER.info("Initializing connection using Environment Variables (local)");
                connectToLocalCassandra();
                _instance.initialized = true;
            } else {
                LOGGER.info("Expecting secure connect bundle from UI");
                // Do nothing default behaviour
            }
        }
        return _instance;
    }
    
    private static Collection<InetSocketAddress> parseContactPoints(String contactPoints) {
        return Arrays.stream(contactPoints.split(","))
                     .map(node -> {
                             String[] address = node.split(":");
                             if (address.length > 1) { 
                                 return new InetSocketAddress(address[0], Integer.valueOf(address[1]));
                             } else {
                                 return new InetSocketAddress(address[0], 9042);
                             }
                      })
                     .collect(Collectors.toSet());
    }
    
    private static void connectToLocalCassandra() {
        // Username
        _instance.userName = System.getenv().get(USERNAME);
        if (null != _instance.userName && !"".equals(_instance.userName)) {
            LOGGER.info("+ UserName: environment variable '{}' has been read as {}", USERNAME, _instance.userName);
        } else {
            _instance.userName = "KVUser";
            LOGGER.info("+ Username: environment variable '{}' not found defaulting to '{}'", USERNAME, _instance.userName);
        }
        // Password
        _instance.password = System.getenv().get(PASSWORD);
        if (null != _instance.password && !"".equals(_instance.password)) {
            LOGGER.info("+ Password: environment variable '{}' has been read.", _instance.password);
        } else {
            _instance.password = "KVPassword";
            LOGGER.info("+ Password: environment variable '{}' not found defaulting to '{}'", PASSWORD, _instance.password);
        }
        // Keyspace
        _instance.keySpace = System.getenv().get(KEYSPACE);
        if (null != _instance.keySpace && !"".equals(_instance.keySpace)) {
            LOGGER.info("+ Keyspace: environment variable '{}' has been read as {}", KEYSPACE, _instance.keySpace);
        } else {
            _instance.keySpace  = "killrvideo";
            LOGGER.info("+ Keyspace: environment variable '{}' not found defaulting to '{}'", KEYSPACE, _instance.keySpace);
        }
        // Checking required parameters for local deployments
        String sLocalDataCenter = System.getenv().get(LOCAL_DATACENTER);
        if (null != sLocalDataCenter && !"".equals(sLocalDataCenter)) {
            LOGGER.info("+ [local] localdataCenter: "
                    + "environment variable '{}' has been read {}", LOCAL_DATACENTER, sLocalDataCenter);
        } else {
            sLocalDataCenter = "datacenter1";
            LOGGER.info("+ [local] localdataCenter: "
                    + "environment variable '{}' not found defaulting to {}", LOCAL_DATACENTER, sLocalDataCenter);
        }
        // Contact Points
        String sContactPoints = System.getenv().get(CONNECTION_POINTS);
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
                .withAuthCredentials(_instance.userName, _instance.password)
                .withLocalDatacenter(sLocalDataCenter)
                .withKeyspace(_instance.keySpace).build();
        LOGGER.info("+ Connected to CASSANDRA");
    }
    
    /**
     * Initialize parameters.
     *
     * @param userName
     *      current username
     * @param password
     *      current password
     * @param secureConnectionBundlePath
     *      zip bundle path on disl
     * @param keyspace
     *      current keyspace
     */
    public void saveCredentials(String userName, String password, String keyspace, String secureConnectionBundlePath) {
        this.userName                   = userName;
        this.password                   = password;
        this.keySpace                   = keyspace;
        this.secureConnectionBundlePath = secureConnectionBundlePath;
        this.initialized                = true;
    }
    
    /**
     * Test with no persistence.
     * 
     * @param user
     *      sample user name
     * @param password
     *      sample password
     * @param keyspace
     *      sample keyspace
     * @param secureConnectionBundlePath
     *      temp file
     */
    public void testCredentials(String user, String passwd, String keyspce, String secureConnectionBundlePath) {
        // Autocloseable temporary session
        try (CqlSession tmpSession = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get(secureConnectionBundlePath))
                .withAuthCredentials(user, passwd)
                .withKeyspace(keyspce).build()) {
            tmpSession.execute(QUERY_HEALTH_CHECK);
        } catch(RuntimeException re) {
            throw new IllegalStateException(re);
        }
    }
    
    /**
     * Getter accessor for attribute 'cqlSession'.
     *
     * @return
     *       current value of 'cqlSession'
     */
    public CqlSession getCqlSession() {
        if (!isInitialized()) {
            throw new IllegalStateException("Please initialize the connection parameters first with saveCredentials(...)");
        }
        if (null == cqlSession) {
            cqlSession = CqlSession.builder()
                    .withCloudSecureConnectBundle(Paths.get(getSecureConnectionBundlePath()))
                    .withAuthCredentials(getUserName(),getPassword())
                    .withKeyspace(getKeySpace())
                    .build();
            
            // Once session has been initialized, creating schema
            createSchemaIfNeeded(cqlSession);
        }
        return cqlSession;
    }
    
    protected void createSchemaIfNeeded(CqlSession cqlSession) {
        try {
            CqlFileUtils.executeCQLFile(cqlSession, "schema.cql");
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * If simple command failing => invalid connection
     */
    public void checkConnection() {
        try {
            getCqlSession().execute(QUERY_HEALTH_CHECK);
        } catch(RuntimeException re) {
            throw new IllegalStateException(re);
        }
    }
    
    /**
     * Cleanup session
     */
    public void close() {
        if (isInitialized() && null != cqlSession) {
            cqlSession.close();
        }
    }

    /**
     * Getter accessor for attribute 'userName'.
     *
     * @return
     *       current value of 'userName'
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter accessor for attribute 'password'.
     *
     * @return
     *       current value of 'password'
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter accessor for attribute 'secureConnectionBundlePath'.
     *
     * @return
     *       current value of 'secureConnectionBundlePath'
     */
    public String getSecureConnectionBundlePath() {
        return secureConnectionBundlePath;
    }

    /**
     * Getter accessor for attribute 'keySpace'.
     *
     * @return
     *       current value of 'keySpace'
     */
    public String getKeySpace() {
        return keySpace;
    }

    /**
     * Getter accessor for attribute 'initialized'.
     *
     * @return
     *       current value of 'initialized'
     */
    public boolean isInitialized() {
        return initialized;
    }    
    
}
