/*
 * Copyright (C) 2017 Jeff Carpenter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.astra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.datastax.astra.dao.SessionManager;

@SpringBootApplication
public class GettingStartedWithAstra {

    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GettingStartedWithAstra.class);
    
	public static void main(String[] args) {
	    // Logging in the beggining
        SpringApplication.run(GettingStartedWithAstra.class, args);
        SessionManager.useAstra = shoudWeUseAstra();
        
        System.out.println( System.getenv());
	}
	
	 private static boolean shoudWeUseAstra() {
	        String sUseAstra = System.getenv().get(SessionManager.USE_ASTRA);
	        boolean useAstra = true;
	        if (null != sUseAstra && 
	                 ("false".equalsIgnoreCase(sUseAstra) 
	                || "true".equalsIgnoreCase(sUseAstra))) {
	            useAstra = Boolean.valueOf(sUseAstra);
	            LOGGER.info("Environment variable '{}' has been read as '{}'", SessionManager.USE_ASTRA, useAstra);
	        } else {
	            useAstra= true;
	            LOGGER.info("Environment variable '{}' not found defaulting to 'true'", SessionManager.USE_ASTRA);
	        }
	        return useAstra;
	 }
}
