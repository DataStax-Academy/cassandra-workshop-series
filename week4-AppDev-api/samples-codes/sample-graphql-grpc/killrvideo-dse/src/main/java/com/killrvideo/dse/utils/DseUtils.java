package com.killrvideo.dse.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.dse.DseSession;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Utility class for DSE.
 *
 * @author DataStax Evangelist Team
 */
public class DseUtils {
    
    /** Internal logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DseUtils.class);
    
    /** Replication Strategies. */
    public static enum ReplicationStrategy { SimpleStrategy, NetworkTopologyStrategy };
    
    private static final String KEY_CLASS             = "class";
    private static final String KEY_REPLICATIONFACTOR = "replication_factor";
    private static final String UTF8_ENCODING         = "UTF-8";
    private static final String NEW_LINE              = System.getProperty("line.separator");
    
    /**
     * Helper to create a KeySpace.
     *
     * @param keyspacename
     *      target keyspaceName
     */
    public static void createKeySpaceSimpleStrategy(DseSession dseSession, String keyspacename, int replicationFactor) {
        final Map<String, Object> replication = new HashMap<>();
        replication.put(KEY_CLASS, ReplicationStrategy.SimpleStrategy.name());
        replication.put(KEY_REPLICATIONFACTOR, replicationFactor);
        dseSession.execute(SchemaBuilder.createKeyspace(keyspacename).ifNotExists().with().replication(replication));
        useKeySpace(dseSession, keyspacename);
    }
    
    /**
     * Setup connection to use keyspace.
     *
     * @param dseSession
     *      current session
     * @param keyspacename
     *      target keyspace
     */
    public static void useKeySpace(DseSession dseSession, String keyspacename) {
        dseSession.execute("USE " + keyspacename);
    }
    
    /**
     * Empty table.
     *
     * @param dseSession
     *      current session
     * @param tableName
     *      table name
     */
    public static void truncate(DseSession dseSession, String tableName) {
        dseSession.execute(QueryBuilder.truncate(tableName));
    }
    
    /**
     * Allows to execute a CQL File.
     *
     * @param dseSession
     *      current dse Session
     * @param fileName
     *      cql file name to execute
     * @throws FileNotFoundException
     *      cql file has not been found.
     */
    public static void executeCQLFile(DseSession dseSession, String fileName)
    throws FileNotFoundException {
        long top = System.currentTimeMillis();
        LOGGER.info("Processing file: " + fileName);
        Arrays.stream(loadFileAsString(fileName).split(";")).forEach(statement -> {
            String query = statement.replaceAll(NEW_LINE, "").trim();
            try {
                if (query.length() > 0) {
                    dseSession.execute(query);
                    LOGGER.info(" + Executed. " + query);
                }
            } catch (InvalidQueryException e) {
                LOGGER.warn(" + Query Ignore. " + query, e);
            }
        });
        LOGGER.info("Execution done in {} millis.", System.currentTimeMillis() - top);
    }
    
    /**
     * Utils method to load a file as String.
     *
     * @param fileName
     *            target file Name.
     * @return target file content as String
     * @throws FileNotFoundException 
     */
    private static String loadFileAsString(String fileName)
    throws FileNotFoundException {
        InputStream in = DseUtils.class.getResourceAsStream(fileName);
        if (in == null) {
            // Fetch absolute classloader path
            in =  DseUtils.class.getClassLoader().getResourceAsStream(fileName);
        }
        if (in == null) {
            // Thread
            in =  Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        }
        if (in == null) {
            throw new FileNotFoundException("Cannot load file " + fileName + " please check");
        }
        Scanner currentScan = null;
        StringBuilder strBuilder = new StringBuilder();
        try {
            currentScan = new Scanner(in, UTF8_ENCODING);
            while (currentScan.hasNextLine()) {
                strBuilder.append(currentScan.nextLine());
                strBuilder.append(NEW_LINE);
            }
        } finally {
            if (currentScan != null) {
                currentScan.close();
            }
        }
        return strBuilder.toString();
    }
    
    /**
     * From Future<ResultSet> to completableFuture<ResultSet>, also useful for 
     * 
     * @param listenableFuture
     * @return
     */
    public static <T> CompletableFuture<T> buildCompletableFuture(final ListenableFuture<T> listenableFuture) {
        CompletableFuture<T> completable = new CompletableFuture<T>();
        Futures.addCallback(listenableFuture, new FutureCallback<T>() {
            public void onSuccess(T result)    { completable.complete(result); }
            public void onFailure(Throwable t) { completable.completeExceptionally(t);}
        });
        return completable;
    }
}
