package com.datastax.workshop;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DBConnection {
    
    private static String connectionPath = "/Users/cedricklunven/Downloads/secure-connect-devworkshopdb.zip";
    
    private static String username = "todouser";
    
    private static String password = "todopassword";

    public static Path getConnectionPath() {
        return Paths.get(connectionPath);
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}