package com.killrvideo.graphql.api;


import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.killrvideo.dse.dao.CommentDseDao;
import com.killrvideo.dse.model.Comment;
import com.killrvideo.graphql.domain.CommentGQL;

@Component
public class KillrvideoMutation implements GraphQLMutationResolver {
  
  @Autowired
  private CommentDseDao commentDseDao;
  
  /**
   * Add a comment for dedicated video and known user.
   *
   * @param videoid
   *        unique video identifier (required)
   * @param userid
   *        unique user identifier (required)
   * @param text
   *        current comment
   * @return
   */
  public CommentGQL commentOnVideo(String commentid, String videoid, String userid, String text) {
      Assert.hasText(commentid, "Comment identifier is required here");
      Assert.hasText(videoid, "Video identifier is required here");
      Assert.hasText(userid, "User identifier is required here");
      Assert.hasText(text, "Comment text is required here");
      
      Comment newComment = new Comment();
      newComment.setCommentid(UUID.fromString(commentid));
      newComment.setVideoid(UUID.fromString(videoid));
      newComment.setUserid(UUID.fromString(userid));
      newComment.setComment(text);
      newComment.setDateOfComment(new Date());
      commentDseDao.insertComment(newComment);
      return new CommentGQL(newComment);
   }
  
}
