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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.datastax.astra.dao.SessionManager;

@SpringBootApplication
public class GettingStartedWithAstra {

	public static void main(String[] args) {
	    /* ASTRA
	    //System.setProperty(SessionManager.USE_ASTRA, String.valueOf(true));
	    //System.setProperty(SessionManager.USERNAME, "todouser");
	    //System.setProperty(SessionManager.PASSWORD, "todopassword");
	    //System.setProperty(SessionManager.KEYSPACE, "todoapp");
	    //System.setProperty(SessionManager.SECURE_CONNECT_BUNDLE_PATH, 
	    //        "/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip");
	    */
	    
	    /* LOCAL
	    System.setProperty(SessionManager.USE_ASTRA, String.valueOf(false));
        System.setProperty(SessionManager.KEYSPACE, "killrvideo");
        System.setProperty(SessionManager.CONNECTION_POINTS, "localhost:9042");
        System.setProperty(SessionManager.LOCAL_DATACENTER, "datacenter1");
        */
	    
        SpringApplication.run(GettingStartedWithAstra.class, args);
	}
}
