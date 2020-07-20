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
import com.killrvideo.dse.dao.dto.QueryCommentByUser;
import com.killrvideo.dse.dao.dto.ResultListPage;
import com.killrvideo.dse.model.Comment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/users/{useruuid}")
@Api(value = "Resource User",  description = "Operation on single User")
public class UserResource {
    
    /** Logger for that class. */
    private static Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    
    @Autowired
    private CommentDseDao commentDao;
    
    @RequestMapping(value = "/comments", method = GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List comment for a specified user", response = ResultListPage.class)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Retrieve comments for a dedicated user")
    )
    public ResultListPage< Comment > getComments(
            @ApiParam(name="useruuid", value="Unique identifier for a user", required=true ) 
            @PathVariable(value = "useruuid") String useruuid,
            @ApiParam(name="pageSize", value="Requested page size, default is 10", required=false ) 
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam(name="pageState", value="Use to retrieve next pages", required=false ) 
            @RequestParam("pageState") Optional<String> pageState,
            @ApiParam(name="commentuuid", value="starting point for next page", required=false )
            @RequestParam("commentuuid") Optional<String> commentuuid) {
       
        // Parsing Result
        QueryCommentByUser qcbu = new QueryCommentByUser();
        qcbu.setUserId(UUID.fromString(useruuid));
        qcbu.setPageState(pageState);
        pageSize.ifPresent(qcbu::setPageSize);
        if (commentuuid.isPresent()) {
            qcbu.setCommentId(commentuuid.map(UUID::fromString));
        }
        
        // Async DAO invocation
        LOGGER.info("Retrieving comments for a user ");
        return commentDao.findCommentsByUserId(qcbu);
    }
    
}
