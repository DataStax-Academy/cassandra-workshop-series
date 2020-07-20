package com.killrvideo.rest.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Landing page on '/'
 * 
 * @author DataStax Evangelist Team
 */
@RestController
@RequestMapping("/")
public class HomeResource {
    
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String sayHello() {
        return new StringBuilder(""
                + "<html>"
                + " <head>"
                + "  <meta http-equiv=\"refresh\" content=\"0; url=swagger-ui.html\" />"
                + " </head>"
                + " <body/></html>").toString();
    }

}
