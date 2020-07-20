package com.killrvideo.dse.model;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Specialization for VIDEO.
 *
 * @author DataStax evangelist team.
 */
@Table(keyspace = SchemaConstants.KILLRVIDEO_KEYSPACE, name = SchemaConstants.TABLENAME_COMMENTS_BY_VIDEO)
public class CommentByVideo extends Comment {
    
    /** Serial. */
    private static final long serialVersionUID = -6738790629520080307L;
    
    public CommentByVideo() {
    }
    
    public CommentByVideo(Comment c) {
        this.commentid  = c.getCommentid();
        this.userid     = c.getUserid();
        this.videoid    = c.getVideoid();
        this.comment    = c.getComment();
    }

    /**
     * Getter for attribute 'videoid'.
     *
     * @return
     *       current value of 'videoid'
     */
    @PartitionKey
    public UUID getVideoid() {
        return videoid;
    }
    
    
}
