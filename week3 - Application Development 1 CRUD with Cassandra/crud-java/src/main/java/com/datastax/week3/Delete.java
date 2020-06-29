package com.datastax.week3;
import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.oss.driver.api.core.cql.*;

public class Delete {
    
    public static void main(String[] args) {

       try (DseSession session = DseSession.builder().withCloudSecureConnectBundle(DBConnection.getConnectionPath())
           .withAuthCredentials(DBConnection.getUsername(), DBConnection.getPassword())
           .build()) {

            session.execute(
                SimpleStatement.builder( "DELETE FROM killrvideo.user_credentials WHERE email = ?")
                        .addPositionalValues("cv@datastax.com")
                        .build());
            System.out.println("Successful Delete");
        }
        catch(Throwable t) {
            System.out.println("Failed Delete");
            t.printStackTrace();
        }
    }
}
