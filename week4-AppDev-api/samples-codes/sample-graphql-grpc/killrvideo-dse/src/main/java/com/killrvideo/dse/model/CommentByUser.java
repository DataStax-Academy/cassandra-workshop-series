package com.killrvideo.dse.model;

import static com.killrvideo.dse.model.SchemaConstants.KILLRVIDEO_KEYSPACE;
import static com.killrvideo.dse.model.SchemaConstants.TABLENAME_COMMENTS_BY_USER;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Specialization for USER.
 *
 * @author DataStax evangelist team.
 */
@Table(keyspace=KILLRVIDEO_KEYSPACE, name=TABLENAME_COMMENTS_BY_USER)
public class CommentByUser extends Comment {
    
    /** Serial. */
    private static final long serialVersionUID = 1453554109222565840L;
    
    /**
     * Default constructor.
     */
    public CommentByUser() {}
    
    /**
     * Copy constructor.
     *
     * @param c
     */
    public CommentByUser(Comment c) {
        this.commentid  = c.getCommentid();
        this.userid     = c.getUserid();
        this.videoid    = c.getVideoid();
        this.comment    = c.getComment();
    }

    /**
     * Getter for attribute 'userid'.
     *
     * @return
     *       current value of 'userid'
     */
    @PartitionKey
    public UUID getUserid() {
        return userid;
    }

}
