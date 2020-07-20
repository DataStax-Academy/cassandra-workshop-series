package com.killrvideo.dse.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.utils.UUIDs;
import com.killrvideo.dse.dao.CommentDseDao;
import com.killrvideo.dse.dao.dto.QueryCommentByUser;
import com.killrvideo.dse.dao.dto.QueryCommentByVideo;
import com.killrvideo.dse.dao.dto.ResultListPage;
import com.killrvideo.dse.model.Comment;

/** 
 * Utility to test DAO on remote SERVER (just provide the URL).
 */
public class CommentDaoTestIT extends AbstractTest {

	// Where to look : ¯\_(ツ)_/¯
	protected String getContactPointAdress()         { return "127.0.0.1"; }
    protected int    getContactPointPort()           { return 9042;        }
    protected ConsistencyLevel getConsistencyLevel() { return ConsistencyLevel.ONE; }
   
    /** Target DAO     ¯\_(ツ)_/¯      */
    private CommentDseDao commentDao;
    
    @BeforeEach
    public void initDAO() {
        if (commentDao == null) {
            connectKeyspace(KILLRVIDEO_KEYSPACE);
            commentDao = new CommentDseDao(dseSession);
        }
    }
    
    @Test
    @DisplayName("Generation de commentaires")
    public void genererDesCommentaires() {
        // Given
        UUID randomUserId = UUID.randomUUID();
        System.out.println("Your UserID " + randomUserId );
        QueryCommentByUser query = new QueryCommentByUser(randomUserId);
        Assert.assertEquals(0, commentDao.findCommentsByUserId(query).getResults().size());
         
        for(int idx =0;idx<5;idx++) {
            Comment comment = new Comment(getListComments().get(idx));
            comment.setCommentid(UUIDs.timeBased());
            comment.setUserid(randomUserId);
            comment.setVideoid(UUID.randomUUID());
            commentDao.insertComment(comment);
        }
    }

    @Test
    @DisplayName("When inserting Comment into empty tables you got 1 record")
    public void testInsertComment() {
        // When
        Comment comment = new Comment("Hello World !");
        comment.setCommentid(UUIDs.timeBased());
        comment.setUserid(UUID.randomUUID());
        comment.setVideoid(UUID.randomUUID());
        commentDao.insertComment(comment);
        // Then
        assertCountItemInTable(1, KILLRVIDEO_KEYSPACE, TABLENAME_COMMENTS_BY_USER);
        assertCountItemInTable(1, KILLRVIDEO_KEYSPACE, TABLENAME_COMMENTS_BY_VIDEO);
    }
    
    @Test
    public void getCommentsByUser() throws Exception {
    	QueryCommentByUser query = new QueryCommentByUser(UUID.fromString("d963dad3-15d2-4652-98c3-23060c958c4b"));
    	query.setPageSize(10);
    	ResultListPage<Comment> myComments = commentDao.findCommentsByUserId(query);
    	for (Comment comment : myComments.getResults()) {
    	    System.out.println("Comment:" + comment.getComment() );
        }
    }
  
    @Test
    public void getCommentsByVideo() throws Exception {
    	QueryCommentByVideo query = new QueryCommentByVideo(UUID.fromString("3ffd0bbb-dd80-4816-af39-cd6e6c2e7507"));
    	ResultListPage<Comment> myComments = commentDao.findCommentsByVideoId(query);
    	System.out.println(myComments.toString());
    }
    
    @Test
    public void updateComment() throws Exception {
    	Comment target =  new Comment();
    	target.setComment("COMMENT FROM TEST");
    	target.setCommentid(UUID.fromString("f4e65c40-3be4-11e8-8ded-3195fe4e490f"));
    	target.setVideoid(UUID.fromString("3ffd0bbb-dd80-4816-af39-cd6e6c2e7507"));
    	target.setUserid(UUID.fromString("805d83ad-2fbc-43e9-a61b-9ebb3dae0950"));
    	commentDao.updateComment(target);
    }
    
    @Test
    public void deleteComment() throws Exception {
    	Comment target =  new Comment();
    	target.setCommentid(UUID.fromString("52f095b0-3bdd-11e8-8ded-3195fe4e490f"));
    	target.setVideoid(UUID.fromString("fd7f1690-2a48-4751-9afb-9073af0c5c30"));
    	target.setUserid(UUID.fromString("805d83ad-2fbc-43e9-a61b-9ebb3dae0950"));
    	commentDao.deleteComment(target);
    }
    
    
    public List < String > getListComments() {
      List< String > comments = new ArrayList<>();
      comments.add("...a 4H du matin, ce sont les seuls qui tiennent la marée");
      comments.add("Comment reconnais-tu un breton en soirée...");
      comments.add("Quimper, c'est le sud.");
      comments.add("T'es Brestois... si t'as déja fini dans le Lagen");
      comments.add("T'es Brestois... si tu connais les ribines pour éviter la bleuzaille");
      return comments;
    }
    
}