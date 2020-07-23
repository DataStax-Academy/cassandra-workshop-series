package com.datastax.astra.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Allows CORS Filter
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Component
public class CorsController implements WebMvcConfigurer {

    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CorsController.class);
    
    /** {@inheritDoc} */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        LOGGER.info("Allowing CORS");
        registry.addMapping("/**");
    }
}
