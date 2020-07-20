package com.killrvideo.grpc;

import static java.util.UUID.fromString;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.killrvideo.dse.dao.CommentDseDao;
import com.killrvideo.dse.dao.dto.QueryCommentByUser;
import com.killrvideo.dse.dao.dto.QueryCommentByVideo;
import com.killrvideo.dse.model.Comment;
import com.killrvideo.grpc.utils.CommentGrpcHelper;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import killrvideo.comments.CommentsServiceGrpc.CommentsServiceImplBase;
import killrvideo.comments.CommentsServiceOuterClass.CommentOnVideoRequest;
import killrvideo.comments.CommentsServiceOuterClass.CommentOnVideoResponse;
import killrvideo.comments.CommentsServiceOuterClass.GetUserCommentsRequest;
import killrvideo.comments.CommentsServiceOuterClass.GetUserCommentsResponse;
import killrvideo.comments.CommentsServiceOuterClass.GetVideoCommentsRequest;
import killrvideo.comments.CommentsServiceOuterClass.GetVideoCommentsResponse;

/**
 * Exposition of comment services with GPRC Technology & Protobuf Interface
 * 
 * @author DataStax evangelist team.
 */
@Service
public class CommentsGrpcService extends CommentsServiceImplBase {
    
    /** Loger for that class. */
    private static Logger LOGGER = LoggerFactory.getLogger(CommentsGrpcService.class);
  
    @Autowired
    private CommentGrpcHelper helper;
    
    /** Communications and queries to DSE (Comment). */
    @Autowired
    private CommentDseDao dseCommentDao;
    
    /** {@inheritDoc} */
    @Override
    public void commentOnVideo(final CommentOnVideoRequest grpcReq, StreamObserver<CommentOnVideoResponse> grpcResObserver) {

        // Validate Parameters
        helper.validateGrpcRequestCommentOnVideo(LOGGER, grpcReq, grpcResObserver);

        // Stands as stopwatch for logging and messaging
        final Instant starts = Instant.now();

        // Mapping GRPC => Domain (Dao)
        Comment q = new Comment();
        q.setVideoid(fromString(grpcReq.getVideoId().getValue()));
        q.setCommentid(fromString(grpcReq.getCommentId().getValue()));
        q.setUserid(fromString(grpcReq.getUserId().getValue()));
        q.setComment(grpcReq.getComment());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Insert comment on video {} for user {} : {}", q.getVideoid(), q.getUserid(), q);
        }

        // ASYNCHRONOUS works with ComputableFuture
        dseCommentDao.insertCommentAsync(q).whenComplete((result, error) -> {
            if (error != null) {
                helper.traceError(LOGGER, "commentOnVideo", starts, error);
                grpcResObserver.onError(Status.INTERNAL.withCause(error).asRuntimeException());
            } else {
                helper.traceSuccess(LOGGER, "commentOnVideo", starts);
                grpcResObserver.onNext(CommentOnVideoResponse.newBuilder().build());
                grpcResObserver.onCompleted();
            }
        });
            
        /* SYNCHRONOUS with the classic try/catch
        try {
          dseCommentDao.insertComment(q);
          helper.traceSuccess(LOGGER, "commentOnVideo", starts);
          grpcResObserver.onNext(CommentOnVideoResponse.newBuilder().build());
          grpcResObserver.onCompleted();
        } catch(Throwable t) {
           helper.traceError(LOGGER, "commentOnVideo", starts, t);
           grpcResObserver.onError(t);
        }*/
        
    }
    
    /** {@inheritDoc} */
    @Override
    public void getVideoComments(final GetVideoCommentsRequest grpcReq, StreamObserver<GetVideoCommentsResponse> responseObserver) {
        
        // Parameter validations
        helper.validateGrpcRequestGetVideoComment(LOGGER, grpcReq, responseObserver);
        
        // Stands as stopwatch for logging and messaging 
        final Instant starts = Instant.now();
        
        // Mapping GRPC => Domain (Dao) : Dedicated bean creating for flexibility
        QueryCommentByVideo query = helper.mapFromGrpcVideoCommentToDseQuery(grpcReq);
            
        // ASYNCHRONOUS works with ComputableFuture
        dseCommentDao.findCommentsByVideosIdAsync(query).whenComplete((result, error) -> {
            if (result != null) {
                helper.traceSuccess(LOGGER, "getVideoComments", starts);
                responseObserver.onNext(helper.mapFromDseVideoCommentToGrpcResponse(result));
                responseObserver.onCompleted();
            } else if (error != null){
                helper.traceError(LOGGER,"getVideoComments", starts, error);
                responseObserver.onError(error);
            }
        });
            
            /* Synchronous
            try {
                helper.traceSuccess(LOGGER, "getVideoComments", starts);
                responseObserver.onNext(helper.mapFromDseVideoCommentToGrpcResponse(dseCommentDao.findCommentsByVideoId(query)));
                responseObserver.onCompleted();
            } catch(Throwable t) {
                helper.traceError(LOGGER, "getVideoComments", starts, t);
                responseObserver.onError(t);
            }
            */
    }
    
    /** {@inheritDoc} */
    @Override
    public void getUserComments(final GetUserCommentsRequest grpcReq, StreamObserver<GetUserCommentsResponse> responseObserver) {

        // GRPC Parameters Validation
        helper.validateGrpcRequest_GetUserComments(LOGGER, grpcReq, responseObserver);
        
        // Stands as stopwatch for logging and messaging 
        final Instant starts = Instant.now();
        
        // Mapping GRPC => Domain (Dao) : Dedicated bean creating for flexibility
        QueryCommentByUser query = helper.mapFromGrpcUserCommentToDseQuery(grpcReq);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Listing comment for user {}",  query.getUserId());
        }
        
        // ASYNCHRONOUS works with ComputableFuture
        dseCommentDao.findCommentsByUserIdAsync(query).whenComplete((result, error) -> {
            if (result != null) {
                helper.traceSuccess(LOGGER, "getUserComments", starts);
                responseObserver.onNext(helper.mapFromDseUserCommentToGrpcResponse(result));
                responseObserver.onCompleted();
            } else if (error != null){
                helper.traceError(LOGGER,"getUserComments", starts, error);
                responseObserver.onError(error);
            }
        });
            
            /*synchronous
            try {
                helper.traceSuccess(LOGGER, "getUserComments", starts);
                responseObserver.onNext(helper.mapFromDseUserCommentToGrpcResponse(dseCommentDao.findCommentsByUserId(query)));
                responseObserver.onCompleted();
            } catch(Throwable t) {
                helper.traceError(LOGGER,"getUserComments", starts, t);
                responseObserver.onError(t);
            }*/
    }

}
