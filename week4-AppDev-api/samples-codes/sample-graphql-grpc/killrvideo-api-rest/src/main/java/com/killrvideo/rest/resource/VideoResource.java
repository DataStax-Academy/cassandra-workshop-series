package com.killrvideo.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.killrvideo.dse.dao.CommentDseDao;
import com.killrvideo.dse.dao.dto.QueryCommentByVideo;
import com.killrvideo.dse.dao.dto.ResultListPage;
import com.killrvideo.dse.model.Comment;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/videos/{videouuid}")
public class VideoResource {
  
    /** Logger for that class. */
    private static Logger LOGGER = LoggerFactory.getLogger(VideoResource.class);

    @Autowired
    private CommentDseDao commentDao;
    
    @RequestMapping(value = "/comments", method = GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List comment for a specified video", response = ResultListPage.class)
    @ApiResponses(@ApiResponse(code = 200, message = "Retrieve comments for a dedicated video"))
    public ResultListPage< Comment > getComments(
            @ApiParam(name="videouuid", value="Unique identifier for a video", required=true ) 
            @PathVariable(value = "videouuid") String videouuid,
            @ApiParam(name="pageSize", value="Requested page size, default is 10", required=false ) 
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam(name="pageState", value="Use to retrieve next pages", required=false ) 
            @RequestParam("pageState") Optional<String> pageState,
            @ApiParam(name="startviduuid", value="starting point for next page", required=false )
            @RequestParam("startviduuid") Optional<String> commentuuid) {

        QueryCommentByVideo qcbv = new QueryCommentByVideo();
        qcbv.setVideoId(UUID.fromString(videouuid));
        qcbv.setPageState(pageState);
        pageSize.ifPresent(qcbv::setPageSize);
        if (commentuuid.isPresent()) {
            qcbv.setCommentId(commentuuid.map(UUID::fromString));
        }
        
        LOGGER.info("Retrieving comments for a video.");
        return commentDao.findCommentsByVideoId(qcbv);
    }
    
    
}
