package com.datastax.astra.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datastax.astra.entity.SpacecraftJourneyCatalog;
import com.datastax.astra.service.AstraService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * REST Controller for operations on spacecrafts catalog.O
 */
@CrossOrigin
@RestController
@Api(
 value = "/api/spacecraft",
 description = "Operations on spacecrafts catalog")
@RequestMapping("/api/spacecraft")
public class SpacecraftController {
    
    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpacecraftController.class);
    
    /** Service implementation Injection. */
    private AstraService astraService;

    /**
     * Constructor.
     *
     * @param spacecraftService
     *      service implementation
     */
    public SpacecraftController(AstraService spacecraftService) {
        this.astraService = spacecraftService;
    }
    
    /**
     * List all spacecrafts from the catalog
     *  
     * @return
     *      list all {@link SpacecraftJourneyCatalog} available in the table 
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all spacecrafts and journeys", response = List.class)
    @ApiResponse(code = 200, message = "List all journeys for a spacecraft")
    public ResponseEntity<List<SpacecraftJourneyCatalog>> findAllSpacecrafts() {
        LOGGER.info("Retrieving all spacecrafts");
        return ResponseEntity.ok(astraService.findAllSpacecrafts());
    }
    
    /**
     * List all journeys for a dedicated spacecraft. If the spacecraft is not found we will show an empty list (an dnot 404.)
     *
     * @param spacecraft_name
     *      spacecraft_name to locate journeys
     * @return
     *     list of associated journey, can be empty
     */
    @GetMapping(value = "/{spacecraftName}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all journeys for a dedicated spacecraft name", response = List.class)
    @ApiResponse(code = 200, message = "List all journeys for a dedicated spacecraft name")
    public ResponseEntity<List<SpacecraftJourneyCatalog>> findAllJourneysForSpacecraft(
            @ApiParam(name="spacecraftName", value="Spacecraft name",example = "gemini3",required=true )
            @PathVariable(value = "spacecraftName") String spaceCraftName) {
        LOGGER.info("Retrieving all journey for spacecraft {}", spaceCraftName);
        return ResponseEntity.ok(astraService.findAllJourneysForSpacecraft(spaceCraftName));
    }
    
    /**
     * Find a unique spacecraft journey from its reference.
     *
     * @param spacecraft_name
     *      spacecraft_name to locate journeys
     * @return
     *     list of associated journey, can be empty
     */
    @RequestMapping(
            value = "/{spacecraftName}/{journeyId}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieve a journey from its spacecraftname and journeyid", response = List.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Returnings SpacecraftJourneyCatalog"),
        @ApiResponse(code = 400, message = "spacecraftName is blank or contains invalid characters (expecting AlphaNumeric)"),
        @ApiResponse(code = 404, message = "No journey exists for the provided spacecraftName and journeyid")
    })
    public ResponseEntity<SpacecraftJourneyCatalog> findSpacecraftJourney(
            @ApiParam(name="spacecraftName", value="Spacecraft name",example = "gemini3",required=true )
            @PathVariable(value = "spacecraftName") String spacecraftName,
            @ApiParam(name="journeyId", value="Identifer for journey",example = "abb7c000-c310-11ac-8080-808080808080",required=true )
            @PathVariable(value = "journeyId") UUID journeyId) {
        LOGGER.info("Fetching journey with spacecraft name {} and journeyid {}", spacecraftName, journeyId);
        // Invoking Service
        Optional<SpacecraftJourneyCatalog> journey = astraService.findJourneyById(spacecraftName, journeyId);
        // Routing Result
        if (!journey.isPresent()) {
            LOGGER.warn("Journey with spacecraft name {} and journeyid {} has not been found", spacecraftName, journeyId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(journey.get());
    }
    
    /**
     * Create a new Journey for a Spacecraft
     */
    @PostMapping(value = "/{spacecraftName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    @ApiOperation(value = " Create a new Journey for a Spacecraft", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Journey has been created"),
            @ApiResponse(code = 400, message = "Invalid Spacecraft name provided")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "summary",
            value = "Body of the request is a string representing the summary",
            required = true, dataType = "string", paramType = "body", example = "Going to the Moon")
    })
    public ResponseEntity<String> createSpacecraftJourney(
            HttpServletRequest request,
            @ApiParam(name="spacecraftName", value="Spacecraft name",example = "soyuztm-8",required=true )
            @PathVariable(value = "spacecraftName") String spacecraftName,
            @RequestBody String summary) {
        UUID journeyId = astraService.createSpacecraftJourney(spacecraftName, summary);
        // HTTP Created spec, return target resource in 'location' header
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/spacecrafts/{spacecraftName}/{journeyId}")
                .buildAndExpand(spacecraftName, journeyId)
                .toUri();
        // HTTP 201 with confirmation number
        return ResponseEntity.created(location).body(journeyId.toString());
    }
   

}
