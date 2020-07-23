package com.datastax.astra.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datastax.astra.dao.SessionManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@CrossOrigin
@RestController
@Api(
   value = "/api/credentials", 
   description = "Send connectivity parameters to initialize component")
@RequestMapping("/api/credentials")
public class CredentialsController {
    
    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialsController.class);
    
    /**
     * POST on /api/credentials
     * 
     * Request body is the zipfile
     * 
     * @param username
     *      HTTP POST PARAM user name
     * @param password
     *      HTTP POST PARAM password
     * @param keyspace
     *      name of keysapce
     * @return
     *      status of connection
     * @throws IOException 
     *      cannot process incoming file
     */
    @PostMapping
    @ApiOperation(value = "Save credentials and initiate connection", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Credentials are saved and system is now connected"),
            @ApiResponse(code = 401, message = "Invalid Credentials"),
            @ApiResponse(code = 400, message = "Invalid or missing parameters"),
            @ApiResponse(code = 500, message = "Internal error - cannot save file")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "file",
            value = "A binary zip file provided by astra to initiate a 2-ways SSL connection",
            required = true, dataType = "file", paramType = "form")
    })
    public ResponseEntity<String> saveCredentials(
        @RequestParam("username") 
        @ApiParam(name="username", value="login for user authentication", required=true)
        String username,
        @RequestParam("password") 
        @ApiParam(name="password", value="password for user authentication", required=true)
        String password,
        @RequestParam("keyspace") 
        @ApiParam(name="keyspace", value="keyspace to use", required=true)
        String keyspace,
        @RequestParam("file") MultipartFile file) throws IOException {
           LOGGER.info("Initializing credentials and connection");
           LOGGER.info("+ Zip File found with {} bytes", file.getSize());
           
           // Save File Locally in temp folder with generated UID
           File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".zip");
           Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
           LOGGER.info("+ Creating temporary file {}", tempFile.getAbsolutePath());
           
           SessionManager.getInstance()
                         .saveCredentials(username, password, keyspace, tempFile.getAbsolutePath());
           LOGGER.info("+ Saving credentials into SessionManager");
           
           // Checking connection
           return checkConnection();
    }
    
    /**
     * Check if system is initialized and connected.
     */
     @RequestMapping(method = GET)
     @ApiOperation(value = "Status for component", response = String.class)
     @ApiResponses({
         @ApiResponse(code = 200, message = "System is connected"),
         @ApiResponse(code = 401, message = "Invalid Credentials or not initialized")
     })
     public ResponseEntity<String> checkConnection() {
         SessionManager.getInstance().checkConnection();
         LOGGER.info("Session is successfully initialized and connected");
         return ResponseEntity.ok("Connection Successful");
     }
     
     @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<String> testCredentials(
             @RequestParam("username") String username,
             @RequestParam("password") String password,
             @RequestParam("keyspace") String keyspace,
             @RequestParam("file") MultipartFile file) throws IOException {
         
         File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".zip");
         Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
         LOGGER.info("+ Creating temporary file {}", tempFile.getAbsolutePath());
         
         SessionManager.getInstance().testCredentials(username, password, keyspace, tempFile.getAbsolutePath());
         LOGGER.info("Session has been successfully established");
         
         return ResponseEntity.ok("Valid Parameters");
     }
    
}
