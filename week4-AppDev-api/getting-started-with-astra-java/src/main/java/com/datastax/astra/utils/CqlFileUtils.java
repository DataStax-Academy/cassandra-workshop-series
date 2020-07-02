package com.datastax.astra.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;

/**
 * Allow to execute a CQL File
 */
public class CqlFileUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CqlFileUtils.class);
    
    /** Helper for CQL FILE. */
    private static final String UTF8_ENCODING         = "UTF-8";
    private static final String NEW_LINE              = System.getProperty("line.separator");
    
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
        InputStream in = CqlFileUtils.class.getResourceAsStream(fileName);
        if (in == null) {
            // Fetch absolute classloader path
            in =  CqlFileUtils.class.getClassLoader().getResourceAsStream(fileName);
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
     * Allows to execute a CQL File.
     *
     * @param dseSession
     *      current dse Session
     * @param fileName
     *      cql file name to execute
     * @throws FileNotFoundException
     *      cql file has not been found.
     */
    public static void executeCQLFile(CqlSession cqlSession, String fileName)
    throws FileNotFoundException {
        long top = System.currentTimeMillis();
        Arrays.stream(loadFileAsString(fileName).split(";")).forEach(statement -> {
            String query = statement.replaceAll(NEW_LINE, "").trim();
            try {
                if (query.length() > 0) {
                    cqlSession.execute(query);
                    LOGGER.info(" + Executed. " + query);
                }
            } catch (InvalidQueryException e) {
                LOGGER.warn(" + Query Ignore. " + query, e);
            }
        });
        LOGGER.info("Execution done in {} millis.", System.currentTimeMillis() - top);
    }

}
