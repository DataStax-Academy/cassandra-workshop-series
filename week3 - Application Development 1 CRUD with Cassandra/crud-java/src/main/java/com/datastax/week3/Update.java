package com.datastax.week3;
import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.oss.driver.api.core.cql.*;

public class Update {
    
    public static void main(String[] args) {

       try (DseSession session = DseSession.builder().withCloudSecureConnectBundle(DBConnection.getConnectionPath())
            .withAuthCredentials(DBConnection.getUsername(), DBConnection.getPassword())
            .build()) {

            session.execute(
                SimpleStatement.builder( "UPDATE killrvideo.user_credentials SET password = ? WHERE email = ?")
                        .addPositionalValues("Cr1st1n@sN3wP@ssW0rd", "cv@datastax.com")
                        .build());
            System.out.println("Update Succeeded");
        }
        catch(Throwable t) {
            System.out.println("Failed Update");
            t.printStackTrace();
        }
    }
}
