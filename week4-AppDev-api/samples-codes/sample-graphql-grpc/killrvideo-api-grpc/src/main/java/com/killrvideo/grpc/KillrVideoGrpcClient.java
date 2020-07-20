package com.killrvideo.grpc;

import org.springframework.util.Assert;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import killrvideo.comments.CommentsServiceGrpc;
import killrvideo.comments.CommentsServiceGrpc.CommentsServiceBlockingStub;

/**
 * As Unit Test or cany consumer you may want USE the runing GRPC API.
 *
 * @author DataStax Evangelist Team
 */
public class KillrVideoGrpcClient {
    
    /** Grpc Endpoint */
    private ManagedChannel grpcEndPoint;
   
    /** Clients for different services in GRPC. */
    public CommentsServiceBlockingStub commentServiceGrpcClient;
    
    /**
     * Connection to GRPC Server.
     * 
     * @param grpcServer
     *      current grpc hostname
     * @param grpcPort
     *      current grpc portnumber
     */
    public KillrVideoGrpcClient(String grpcServer, int grpcPort) {
       this(ManagedChannelBuilder.forAddress(grpcServer, grpcPort).usePlaintext(true).build());
    }
    
    /**
     * Extension point for your own GRPC channel.
     * 
     * @param grpcEnpoint
     *      current GRPC Channe
     */
    public KillrVideoGrpcClient(ManagedChannel grpcEnpoint) {
        this.grpcEndPoint = grpcEnpoint;
        initServiceClients();
    }
    
    /**
     * Init item
     */
    public void initServiceClients() {
        Assert.notNull(grpcEndPoint, "GrpcEnpoint must be setup");
        commentServiceGrpcClient = CommentsServiceGrpc.newBlockingStub(grpcEndPoint);
    }

    /**
     * Getter accessor for attribute 'commentServiceGrpcClient'.
     *
     * @return
     *       current value of 'commentServiceGrpcClient'
     */
    public CommentsServiceBlockingStub getCommentService() {
        return commentServiceGrpcClient;
    }
   
}
