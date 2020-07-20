package com.killrvideo.dse.dao;

import static com.datastax.driver.mapping.Mapper.Option.timestamp;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.killrvideo.dse.model.SchemaConstants;

/**
 * Mutualization of code for DAO.
 *
 * @author DataStax evangelist team.
 */
public abstract class AbstractDseDao implements SchemaConstants {

    /** Loger for that class. */
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
   
    /** Hold Connectivity to DSE. */
    @Autowired
    protected DseSession dseSession;

    /** Hold Driver Mapper to implement ORM with Cassandra. */
    @Autowired
    protected MappingManager mappingManager;
    
    /**
     * Prepariation of statement before queries allow signifiant performance improvements.
     * This can only be done it the statement is 'static', mean the number of parameter
     * to bind() is fixed. If not the case you can find sample in method buildStatement*() in this class.
     */
    @PostConstruct
    protected abstract void initialize ();
    
    /**
     * Default constructor.
     */
    public AbstractDseDao() {}
    
    /**
     * Allow explicit intialization for test purpose.
     */
    public AbstractDseDao(DseSession dseSession) {
        this.dseSession     = dseSession;
        this.mappingManager = new MappingManager(dseSession);
        initialize();
    }
    
    /**
     * Allows to save with custom mapper if relevant.
     *
     * @param entity
     *      current entity
     * @param overridingMapper
     *      current mapper
     */
    protected <T> void save(T entity, Mapper<T> mapper) {
        long start = System.currentTimeMillis();
        mapper.save(entity, timestamp(System.currentTimeMillis()));
        LOGGER.debug("Saving entity {} in {} milli(s).", entity, System.currentTimeMillis() - start);
    }
    
    protected void assertNotNull(String mName, String pName, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Assertion failed: param " + pName + " is required for method " + mName);
        }
    }

}
