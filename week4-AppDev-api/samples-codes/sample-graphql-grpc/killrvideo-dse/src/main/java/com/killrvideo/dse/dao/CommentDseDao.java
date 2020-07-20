package com.killrvideo.dse.dao;

import static com.killrvideo.dse.model.Comment.COLUMN_COMMENT;
import static com.killrvideo.dse.model.Comment.COLUMN_COMMENTID;
import static com.killrvideo.dse.model.Comment.COLUMN_USERID;
import static com.killrvideo.dse.model.Comment.COLUMN_VIDEOID;
import static com.killrvideo.dse.utils.FutureUtils.asCompletableFuture;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.schemabuilder.SchemaBuilder.Direction;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.killrvideo.dse.dao.dto.QueryCommentByUser;
import com.killrvideo.dse.dao.dto.QueryCommentByVideo;
import com.killrvideo.dse.dao.dto.ResultListPage;
import com.killrvideo.dse.model.Comment;
import com.killrvideo.dse.model.CommentByUser;
import com.killrvideo.dse.model.CommentByVideo;
import com.killrvideo.dse.model.SchemaConstants;


/**
 * Implementation of queries and related to {@link Comment} objects within DataStax Enterprise.
 * Comments are store in 2 tables and all queries are performed against Apache Cassandra.
 * 
 * @author DataStax evangelist team.
 */
@Repository
public class CommentDseDao extends AbstractDseDao {

    /** Logger for that class. */
    private static Logger LOGGER = LoggerFactory.getLogger(CommentDseDao.class);
    
    /** Mapper to ease queries. */
    protected Mapper < CommentByUser >  mapperCommentByUser;
    protected Mapper < CommentByVideo > mapperCommentByVideo;

    /** Precompile statements to speed up queries. */
    private PreparedStatement findCommentsByUser;
    private PreparedStatement findCommentsByUserPageable;
    private PreparedStatement findCommentsByVideo;
    private PreparedStatement findCommentsByVideoPageable;
     
    /**
     * Default constructor.
     */
    public CommentDseDao() {
        super();
    }
    
    /**
     * Allow explicit initialization for test purpose.
     */
    public CommentDseDao(DseSession dseSession) {
        super(dseSession);
    }
    
    /**
     * Cassandra and DSE Session are stateless. For each request a coordinator is chosen
     * and execute query against the cluster. The Driver is stateful, it has to maintain some
     * network connections pool, here we properly cleanu things.
     * 
     * @throws Exception
     *      error on cleanup.
     */
    @PreDestroy
    public void onDestroy() throws Exception {
        if (dseSession  != null && !dseSession.isClosed()) {
            LOGGER.info("Closing DSE Cluster (clean up at shutdown)");
            dseSession.getCluster().close();
            LOGGER.info(" + DSE Cluster is now closed.");
        }
    }
    
    /** {@inheritDoc} */
    @PostConstruct
    protected void initialize () {
        createTablesIfNotExists();
      
        mapperCommentByUser  = mappingManager.mapper(CommentByUser.class);
        mapperCommentByVideo = mappingManager.mapper(CommentByVideo.class);
        
        // Using Mapper and annotated bean to get constants value
        String keyspaceCommentByUser   = mapperCommentByUser.getTableMetadata().getKeyspace().getName();
        String tableNameCommentByUser  = mapperCommentByUser.getTableMetadata().getName();
        
        // Prepare statements with Query Builder
        RegularStatement queryFindComments = QueryBuilder
                .select()
                    .column(COLUMN_USERID).column(COLUMN_COMMENTID)
                    .column(COLUMN_VIDEOID).column(COLUMN_COMMENT)
                    .fcall("toTimestamp", QueryBuilder.column(COLUMN_COMMENTID)).as("comment_timestamp")
                .from(keyspaceCommentByUser, tableNameCommentByUser)
                .where(QueryBuilder.eq(COLUMN_USERID, QueryBuilder.bindMarker()));
        findCommentsByUser = dseSession.prepare(queryFindComments);
        findCommentsByUser.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);

        // Prepare statements with Query Builder
        RegularStatement queryFindCommentsPage = QueryBuilder
                .select()
                    .column(COLUMN_USERID).column(COLUMN_COMMENTID)
                    .column(COLUMN_VIDEOID).column(COLUMN_COMMENT)
                    .fcall("toTimestamp", QueryBuilder.column(COLUMN_COMMENTID)).as("comment_timestamp")
                .from(keyspaceCommentByUser, tableNameCommentByUser)
                .where(QueryBuilder.eq(COLUMN_USERID, QueryBuilder.bindMarker()))
                .and(QueryBuilder.lte(COLUMN_COMMENTID, QueryBuilder.bindMarker()));
        findCommentsByUserPageable = dseSession.prepare(queryFindCommentsPage);
        findCommentsByUserPageable.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
        
        // Using Mapper and annotated bean to get constants value
        String keyspaceCommentByVideo  = mapperCommentByVideo.getTableMetadata().getKeyspace().getName();
        String tableNameCommentByVideo = mapperCommentByVideo.getTableMetadata().getName();
        
        // Prepare statements with Query Builder
        RegularStatement auerySearchAllCommentForvideo = QueryBuilder
                .select()
                    .column(COLUMN_VIDEOID).column(COLUMN_COMMENTID)
                    .column(COLUMN_USERID).column(COLUMN_COMMENT)
                    .fcall("toTimestamp", QueryBuilder.column(COLUMN_COMMENTID)).as("comment_timestamp")
                .from(keyspaceCommentByVideo, tableNameCommentByVideo)
                .where(QueryBuilder.eq(COLUMN_VIDEOID, QueryBuilder.bindMarker()));
        findCommentsByVideo = dseSession.prepare(auerySearchAllCommentForvideo);
        findCommentsByVideo.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
        
        // Prepare statements with Query Builder
        RegularStatement auerySearchCommentForVideo = QueryBuilder
                .select()
                    .column(COLUMN_VIDEOID).column(COLUMN_COMMENTID)
                    .column(COLUMN_USERID).column(COLUMN_COMMENT)
                    .fcall("toTimestamp", QueryBuilder.column(COLUMN_COMMENTID)).as("comment_timestamp")
                .from(keyspaceCommentByVideo, tableNameCommentByVideo)
                .where(QueryBuilder.eq(COLUMN_VIDEOID, QueryBuilder.bindMarker()))
                .and(QueryBuilder.lte(COLUMN_COMMENTID, QueryBuilder.bindMarker()));
        findCommentsByVideoPageable = dseSession.prepare(auerySearchCommentForVideo);
        findCommentsByVideoPageable.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
    }
    
    protected void createTablesIfNotExists() {
      
      dseSession.execute(
          SchemaBuilder.createTable(SchemaConstants.TABLENAME_COMMENTS_BY_VIDEO)
              .ifNotExists()
              .addPartitionKey(COLUMN_VIDEOID, DataType.uuid())
              .addClusteringColumn(COLUMN_COMMENTID, DataType.timeuuid())
              .addColumn(COLUMN_USERID, DataType.uuid())
              .addColumn(COLUMN_COMMENT, DataType.text())
              .withOptions().clusteringOrder(COLUMN_COMMENTID, Direction.DESC)
              .buildInternal());
      LOGGER.info(" + Table '{}' created in keyspace '{}' (if needed)",TABLENAME_COMMENTS_BY_VIDEO,
                    dseSession.getLoggedKeyspace());
      
      dseSession.execute(
          SchemaBuilder.createTable(SchemaConstants.TABLENAME_COMMENTS_BY_USER)
              .ifNotExists()
              .addPartitionKey(COLUMN_USERID, DataType.uuid())
              .addClusteringColumn(COLUMN_COMMENTID, DataType.timeuuid())
              .addColumn(COLUMN_VIDEOID, DataType.uuid())
              .addColumn(COLUMN_COMMENT, DataType.text())
              .withOptions().clusteringOrder(COLUMN_COMMENTID, Direction.DESC)
              .buildInternal());
      LOGGER.info(" + Table '{}' created in keyspace '{}' (if needed)",TABLENAME_COMMENTS_BY_USER,
                    dseSession.getLoggedKeyspace());
    }
    
    /**
     * Insert Comment entity in 2 tables.
     */
    public void insertComment(final Comment comment) {
        
        // Create statement
        BatchStatement batchStatement = buildBatchStatementInsertComment(comment); 
        
        // Execute statement (nothing to return, just INSERT here)
        dseSession.execute(batchStatement);                                        
    }
   
    /**
     * Insert a comment for a video. (in multiple table at once). When executing query aync result will be a completable future.
     * Note the 'executeAsync'> No result are expected from insertion and we return CompletableFuture<VOID>.
     * 
     * @param comment
     *     comment to be inserted by signup user.
     */
    public CompletableFuture<Void> insertCommentAsync(final Comment comment) {
        
        // Create statement
        BatchStatement batchStatement = buildBatchStatementInsertComment(comment);  
        
        // Create a callback for processing result (hey but here it is VOID so no mapping in Success())
        CompletableFuture<Void>   cfv        = new CompletableFuture<>();
        FutureCallback<ResultSet> myCallback = new FutureCallback<ResultSet>() {
            public void onFailure(Throwable ex) { cfv.completeExceptionally(ex); }
            public void onSuccess(ResultSet rs) { cfv.complete(null); } 
        };
        
        // Bind execution and callback
        Futures.addCallback(dseSession.executeAsync(batchStatement), myCallback);
        return cfv;
    }
    
    /**
     * Search comment_by_video Asynchronously with Pagination.
     */
    public ResultListPage<Comment> findCommentsByVideoId(final QueryCommentByVideo query) {
        BoundStatement boundStatement = buildStatementVideoComments(query);                     // Parse input to create statement
        ResultSet      resultSet      = dseSession.execute(boundStatement);                     // Execute statement to get a resultSet
        return mapToCommentList(resultSet);                                                     // Iterate on resultSet to build result bean
    }
    
    /**
     * Search comment_by_video Asynchronously with Pagination.
     */
    public CompletableFuture < ResultListPage<Comment> > findCommentsByVideosIdAsync(final QueryCommentByVideo query) {
        BoundStatement  boundStatement  = buildStatementVideoComments(query);                   // Parse input to create statement
        ResultSetFuture resultSetFuture = dseSession.executeAsync(boundStatement);              // Execute statement to get a FUTURE resultSet (Async)
        return asCompletableFuture(resultSetFuture).thenApplyAsync(this::mapToCommentList);  // Iterate on resultSet to build result bean
    }
    
    /**
     * Execute a query against the 'comment_by_user' table.
     */
    public ResultListPage<Comment> findCommentsByUserId(final QueryCommentByUser query) {
        // Like before but inlined as a boss 
        return mapToCommentList(dseSession.execute(buildStatementUserComments(query))); 
    }
    
    /**
     * Execute a query against the 'comment_by_user' table (ASYNC).
     */
    public CompletableFuture< ResultListPage<Comment> > findCommentsByUserIdAsync(final QueryCommentByUser query) {
        // Like before but inlined as a boss.. again 
        return asCompletableFuture(dseSession.executeAsync(buildStatementUserComments(query)))
                                                .thenApplyAsync(this::mapToCommentList);
    }
    
    /**
     * Update a commet with the new text.
     *
     * @param c
     * 		sample comment
     */
    public void updateComment(final Comment c) {
    	Assert.notNull(c, "Comment object is required");
    	Assert.notNull(c.getUserid(), "userid is required to update a comment");
    	Assert.notNull(c.getVideoid(), "videoid is required to update a comment");
    	Assert.notNull(c.getCommentid(), "commentid is required to update a comment");
    	mappingManager.createAccessor(CommentAccessor.class)
    				  .update(c.getCommentid(), c.getVideoid(), c.getUserid(), c.getComment());
    }
    
    /**
     * When you need a 'custom' query you may use an {@link Accessor} and provide explicity Query.
     *
     * @author DataStax evangelist team.
     */
    @Accessor
    public interface CommentAccessor {
    	
        @Query("BEGIN BATCH\n" + 
        	   "UPDATE " +  KILLRVIDEO_KEYSPACE + ".comments_by_user SET comment = :comment " + 
        	   "WHERE userid = :userid AND commentid= :commentid;\n" + 
        	   "UPDATE " +  KILLRVIDEO_KEYSPACE + ".comments_by_video SET comment = :comment " + 
        	   "WHERE videoid = :videoid AND commentid= :commentid;\n" +
        	   "APPLY BATCH;")
        void update(@Param("commentid") UUID commentId, @Param("videoid") UUID videoId, 
        			@Param("userid") 	UUID userId, 	@Param("comment") String comment);
    }
    
    /**
     * Delete a comment.
     * 
     * @param comment
     * 		entity with identifiers
     */
    public void deleteComment(final Comment comment) {
    	// Check parameterss
    	Assert.notNull(comment, 			   "Comment object is required");
    	Assert.notNull(comment.getUserid(),    "userId is required to delete a comment");
    	Assert.notNull(comment.getVideoid(),   "VideoId is required to delete a comment");
    	Assert.notNull(comment.getCommentid(), "CommetId is required to delete a comment");
    	
    	// Creating statements
    	Statement q1 = mapperCommentByVideo.deleteQuery(new CommentByVideo(comment));
    	Statement q2 = mapperCommentByUser.deleteQuery(new CommentByUser(comment));
    	LOGGER.debug("Deleting with :" + ((BoundStatement) q1).preparedStatement().getQueryString());
    	LOGGER.debug("Deleting with :" + ((BoundStatement) q2).preparedStatement().getQueryString());
    	
        // Run as LWT Batch
        dseSession.execute(new BatchStatement(BatchStatement.Type.LOGGED).add(q1).add(q2)); 
    }
    
    /** 
     * Create batch statement to insert a Comment in 2 tables at the same time.
     * 
     * When inserting an entity in multiple Cassandra tables it is important that the same data is written to
     * both tables to keep them in synchronization. The {@link BatchStatement} ensure light transactions and minimum
     * consistency in the date.
     * 
     * @see https://docs.datastax.com/en/latest-java-driver-api/com/datastax/driver/core/BatchStatement.html
     * 
     * @param comment
     *      current comment.
     */
    private BatchStatement buildBatchStatementInsertComment(Comment comment) {
        BatchStatement batchStatement = new BatchStatement(BatchStatement.Type.UNLOGGED)
                .add(mapperCommentByVideo.saveQuery(new CommentByVideo(comment))) // Insert Query generate from annotated bean CommentByVideo
                .add(mapperCommentByUser.saveQuery(new CommentByUser(comment)));  // Insert Query generate from annotated bean CommentByUser
        batchStatement.setDefaultTimestamp(System.currentTimeMillis());
        batchStatement.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
        return batchStatement;
    }
    
    /**
     * Implementation of mapping. 
     * 
     * Explicitly set dateOfComment because the @Computed
     * annotation set on the dateOfComment field when using QueryBuilder is not executed
     * This gives us the "proper" return object expected for the response to the front-end
     * UI.  It does not function if this value is null or not the correct type.
     * This is the reason why we did not (simply) use commentByVideoMapper.map(rs).
     *
     * @param rs
     * @return
     *      target result
     */
    private ResultListPage<Comment> mapToCommentList(ResultSet rs) {
    	ResultListPage<Comment> result = new ResultListPage<>();
    	Iterator<Row> iterResults = rs.iterator();
    	
        IntStream.range(0, rs.getAvailableWithoutFetching())
                 .forEach(idx -> {
                     Row row = iterResults.next();
                     Comment c = new Comment();
                     c.setComment(row.getString(COLUMN_COMMENT));
                     c.setUserid(row.getUUID(COLUMN_USERID));
                     c.setCommentid(row.getUUID(COLUMN_COMMENTID));
                     c.setVideoid(row.getUUID(COLUMN_VIDEOID));
                     c.setDateOfComment(row.getTimestamp("comment_timestamp"));
                     result.getResults().add(c);
                 });
        result.setPagingState(
                Optional.ofNullable(rs.getExecutionInfo().getPagingState())
                        .map(PagingState::toString));
        return result;
    }
    
    /**
     * This statement is dynamic this is the reason why it is not implemented as a
     * {@link PreparedStatement} but simple {@link BoundStatement}.
     * 
     * @param userId
     *      user unique identifier (required)
     * @param commentId
     *     comment id as offsert or starting point for the query/page 
     * @param pageSize
     *      pageable query, here is the page size
     * @param pageState
     *      provie the PagingState
     * @return
     *      statement to retrieve comments
     */
    private BoundStatement buildStatementUserComments(final QueryCommentByUser query) {
        BoundStatement statement = null;
        if (query.getCommentId().isPresent()) {
            statement = findCommentsByUserPageable.bind()
                            .setUUID(COLUMN_USERID, query.getUserId())
                            .setUUID(COLUMN_COMMENTID, query.getCommentId().get());
        } else {
            statement = findCommentsByUser.bind()
                            .setUUID(COLUMN_USERID, query.getUserId());
        }
        if (query.getPageState().isPresent() && query.getPageState().get().length() > 0) {
            statement.setPagingState(PagingState.fromString(query.getPageState().get()));
        }
        statement.setFetchSize(query.getPageSize());
        statement.setConsistencyLevel(ConsistencyLevel.QUORUM);
        return statement;
    }
    
    /**
     * Init statement based on comment tag.
     *  
     * @param request
     *      current request
     * @return
     *      statement
     */
    private BoundStatement buildStatementVideoComments(final QueryCommentByVideo query) {
        BoundStatement statement = null;
        if (query.getCommentId().isPresent()) {
            statement = findCommentsByVideoPageable.bind()
                        .setUUID(COLUMN_VIDEOID, query.getVideoId())
                        .setUUID(COLUMN_COMMENTID, query.getCommentId().get());
        } else {
            statement = findCommentsByVideo.bind()
                        .setUUID(COLUMN_VIDEOID, query.getVideoId());
        }
        if (query.getPageState().isPresent() && query.getPageState().get().length() > 0) {
            statement.setPagingState(PagingState.fromString(query.getPageState().get()));
        }
        statement.setFetchSize(query.getPageSize());
        statement.setConsistencyLevel(ConsistencyLevel.QUORUM);
        return statement;
    }
            
}
