package com.datastax.astra.dao;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import com.datastax.astra.utils.CqlFileUtils;
import com.datastax.oss.driver.api.core.CqlSession;

/**
 * {@link CqlSession} will be created for each call and drop after.
 */
public class SessionManager {
    
    /** Singleton Pattern. */
    private static SessionManager _instance = null;
    
    /** Connectivity Attributes. */
    private String userName;
    private String password;
    private String keySpace;
    private String secureConnectionBundlePath;
    
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
        }
        return _instance;
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
    public CqlSession connectToAstra() {
        if (!isInitialized()) {
            throw new IllegalStateException("Please initialize the connection parameters first with saveCredentials(...)");
        }
        if (null == cqlSession) {
            cqlSession = CqlSession.builder().withCloudSecureConnectBundle(Paths.get(getSecureConnectionBundlePath()))
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
     * IfO simple command failing => invalid connection
     */
    public void checkConnection() {
        try {
            connectToAstra().execute(QUERY_HEALTH_CHECK);
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
