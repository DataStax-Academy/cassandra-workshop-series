package com.killrvideo.grpc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;

/**
 * Startup a GRPC server on expected port and register all services.
 *
 * @author DataStax evangelist team.
 */
@Component
public class GrpcServer {

    /** Some logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);
    
    /** Listening Port for GRPC. */
    @Value("${grpc.port: 8899}")
    private int grpcPort;
    
    @Autowired
    private CommentsGrpcService commentService;
  
    /**
     * GRPC Server to set up.
     */
    private Server server;
    
    @PostConstruct
    public void start() throws Exception {
        LOGGER.info("Initializing Grpc Server...");
        
        // Binding Services
        final ServerServiceDefinition commentService  = this.commentService.bindService();
        
        // Reference Service in Server
        server = ServerBuilder.forPort(grpcPort)
                   .addService(commentService)
                   .build();
    
            /**
             * Declare a shutdown hook otherwise the JVM
             * cannot be stop since the Grpc server
             * is listening on  a port forever
             */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    LOGGER.info("Calling shutdown for GrpcServer");
                    server.shutdown();
                }
            });

        // Start Grpc listener
        server.start();
        LOGGER.info("[OK] Grpc Server started on port: '{}'", grpcPort);
    }

    @PreDestroy
    public void stop() {
        server.shutdown();
    }
    
}
