package com.datastax.workshop;

/*
 * EXERCISE 2:
 *  
 * TODO Please change this constants with the values 
 * you used when you created the ASTRA instance.
 */
public interface DBConnection {
    
    // This is the Zip file you downloaded
    String SECURE_CONNECT_BUNDLE = "/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip";

    // This is the username, recommended value was KVUser
    String USERNAME = "todouser";

    // This is the password, recommended value was KVPassword
    String PASSWORD = "todopassword";
    
    // This is the keyspace name, recommended value was killrvideo
    String KEYSPACE = "todoapp";
    
}