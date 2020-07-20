package com.killrvideo.rest.resource;

import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.killrvideo.dse.dao.CommentDseDao;
import com.killrvideo.dse.model.Comment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Exposition as REST API.
 *
 * @author DataStax Evangelist Team
 */
@RestController
@Api(value = "/api/v1/comments/{commentuuid}",  description = "CRUD operations working on comments")
@RequestMapping("/api/v1/comments/{commentuuid}")
public class CommentResource {
    
    @Autowired
    private CommentDseDao commentDao;
   
    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create or update a comment based on a given UUID", response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Comment uuid is blank (or), fields are not correctly filled"),
            @ApiResponse(code = 201, message = "Comment has been created"),
            @ApiResponse(code = 204, message = "No content, no changes made to the comment")})
    public ResponseEntity<Boolean> createOrUpdateFeature(@PathVariable(value = "commentuuid") String commentUuid, @RequestBody Comment comment) {
        commentDao.insertComment(comment);
        return new ResponseEntity<Boolean>(TRUE, HttpStatus.CREATED);
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(method = DELETE)
    @ApiOperation(value = "Delete a comment", response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "No content, comment is deleted"),
            @ApiResponse(code = 404, message = "Comment not found")
    })
    public ResponseEntity<?> deleteFeature(@PathVariable(value = "commentuuid") String commentUUid, @RequestBody Comment comment) {
        commentDao.deleteComment(comment);
        return new ResponseEntity(NO_CONTENT);
    }

    /**
     * Getter accessor for attribute 'commentDao'.
     *
     * @return
     *       current value of 'commentDao'
     */
    public CommentDseDao getCommentDao() {
        return commentDao;
    }

    /**
     * Setter accessor for attribute 'commentDao'.
     * @param commentDao
     * 		new value for 'commentDao '
     */
    public void setCommentDao(CommentDseDao commentDao) {
        this.commentDao = commentDao;
    }
}
