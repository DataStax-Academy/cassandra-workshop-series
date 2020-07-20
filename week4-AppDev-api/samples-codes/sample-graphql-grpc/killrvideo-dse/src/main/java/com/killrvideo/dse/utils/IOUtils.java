package com.killrvideo.dse.utils;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for network.
 *
 * @author DataStax Evangelist Team
 */
public class IOUtils {
    
    /** Logger for Graph. */
    private static Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);
    
    /**
     * Hide constructor (Utility class).
     */
    private IOUtils() {}
    
    /**
     * Try to open socket on target Adress and port to evaluate if service is running.
     * 
     * @param service
     *      service name (label)
     * @param address
     *      target adress
     * @param port
     *      target port
     * @return
     *      if something is listening on target URL
     * @throws Exception
     *      exception occured during connection to remote host.
     */
    public static boolean isServiceReachableAndListening(String service, String address, int port) throws Exception {
        Socket s = null;
        try {
            s = new Socket(address, port);
            s.setReuseAddress(true);
            LOGGER.info("Connection to {}:{} is working for service {}", address, port, service);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot connect to service {} on {}:{} is working for service {}", service, address, port, e);
            return false;
        } finally {
            if (s != null) {
                try { s.close();} catch (IOException ex) {}
            }
        }
    }

}
