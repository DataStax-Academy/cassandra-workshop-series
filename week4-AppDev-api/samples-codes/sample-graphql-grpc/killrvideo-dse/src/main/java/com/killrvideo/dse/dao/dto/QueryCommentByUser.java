package com.killrvideo.dse.dao.dto;

import java.util.Optional;
import java.util.UUID;

/**
 * Query to search comments for a User.
 *
 * @author DataStax evangelist team.
 */
public class QueryCommentByUser extends QueryDefinition {

    /** Serial. */
    private static final long serialVersionUID = -1182083603451480239L;
    
    /** User uniaue identifier. */
    private UUID userId = null;
    
    /** Comment offset if specified (Optional) */
    private Optional< UUID > commentId = Optional.empty();

    /**
     * Default constructor.
     */
    public QueryCommentByUser() {
        super();
    }
    
    /**
     * Simple query with an id.
     *
     * @param userId
     *      user id
     */
    public QueryCommentByUser(UUID userId) {
        this.userId = userId;
    }
    
    /**
     * Comment by users.
     *
     * @param userId
     *      user identifuier
     * @param commentId
     *      offset comment comment
     */
    public QueryCommentByUser(UUID userId, UUID commentId) {
        this(userId);
        this.commentId = Optional.ofNullable(commentId);
    }
    
    /**
     * Full flegde constructor.
     */
    public QueryCommentByUser(UUID userId, UUID commentId, int pageSize, String pageState) {
        super(pageSize, pageState);
        this.userId    = userId;
        this.commentId = Optional.ofNullable(commentId);
    }
    
    /**
     * Getter for attribute 'userId'.
     *
     * @return
     *       current value of 'userId'
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Setter for attribute 'userId'.
     * @param userId
     * 		new value for 'userId '
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Getter for attribute 'commentId'.
     *
     * @return
     *       current value of 'commentId'
     */
    public Optional<UUID> getCommentId() {
        return commentId;
    }

    /**
     * Setter for attribute 'commentId'.
     * @param commentId
     * 		new value for 'commentId '
     */
    public void setCommentId(Optional<UUID> commentId) {
        this.commentId = commentId;
    }
    
}
