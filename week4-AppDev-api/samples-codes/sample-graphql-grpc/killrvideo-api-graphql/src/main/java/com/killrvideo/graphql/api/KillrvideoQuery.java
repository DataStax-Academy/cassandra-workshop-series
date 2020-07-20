package com.killrvideo.graphql.api;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.killrvideo.dse.dao.CommentDseDao;
import com.killrvideo.dse.dao.dto.QueryCommentByUser;
import com.killrvideo.dse.dao.dto.QueryCommentByVideo;
import com.killrvideo.dse.dao.dto.ResultListPage;
import com.killrvideo.dse.model.Comment;
import com.killrvideo.graphql.domain.CommentGQL;
import com.killrvideo.graphql.domain.ResultPageCommentGQL;

/**
 * Read only queries GraphQL query resolver.
 */
@Component
public class KillrvideoQuery implements GraphQLQueryResolver {
  
  @Autowired
  private CommentDseDao commentDseDao;
  
  /**
   * Retrieve comment for a dedicated VIDEOS.
   * 
   * @param videoid
   *        video id
   * @param commentid
   *        comment id
   * @param pageSize
   *        page size
   * @param pageState
   *        state for other pages.
   * @return
   */
   public ResultPageCommentGQL getVideoComments (String videoid, String commentid, int pageSize, String pageState) {
      
      // Mapping GraphQl => DAO
      QueryCommentByVideo qcbv = new QueryCommentByVideo();
      qcbv.setVideoId(UUID.fromString(videoid));
      qcbv.setPageSize(pageSize);
      if (pageState != null) {
          qcbv.setPageState(Optional.of(pageState));
      }
      
      // Invoke DAO
      ResultListPage<Comment> resultComments = commentDseDao.findCommentsByVideoId(qcbv);
      
      // DTO => GraphQL beans
      ResultPageCommentGQL result = new ResultPageCommentGQL();
      resultComments.getPagingState().ifPresent(result::setNextPage);
      result.setListOfResults(
              resultComments.getResults().stream()
                            .map(CommentGQL::new)
                            .collect(Collectors.toList()));
      return result;
  }
   
   /**
    * Retrieve comment for a dedicated USER.
    * 
    * @param videoid
    *        video id
    * @param commentid
    *        comment id
    * @param pageSize
    *        page size
    * @param pageState
    *        state for other pages.
    * @return
    */
   public ResultPageCommentGQL getUserComments (String userid, String commentid, int pageSize, String pageState) {
       // Mapping GraphQl => DAO
       QueryCommentByUser qcbu = new QueryCommentByUser();
       qcbu.setUserId(UUID.fromString(userid));
       qcbu.setPageSize(pageSize);
       if (pageState != null) {
           qcbu.setPageState(Optional.of(pageState));
       }
       
       // Invoke DAO
       ResultListPage<Comment> resultComments = commentDseDao.findCommentsByUserId(qcbu);
       
       // DTO => GraphQL beans
       ResultPageCommentGQL result = new ResultPageCommentGQL();
       resultComments.getPagingState().ifPresent(result::setNextPage);
       result.setListOfResults(
               resultComments.getResults().stream()
                             .map(CommentGQL::new)
                             .collect(Collectors.toList()));
       return result;
   }
  
}
