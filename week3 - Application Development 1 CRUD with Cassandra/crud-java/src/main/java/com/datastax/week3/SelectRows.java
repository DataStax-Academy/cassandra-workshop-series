package com.datastax.week3;
import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.oss.driver.api.core.cql.*;

public class SelectRows {
    
    public static void main(String args[]) {
       try (DseSession session = DseSession.builder().withCloudSecureConnectBundle(DBConnection.getConnectionPath())
           .withAuthCredentials(DBConnection.getUsername(), DBConnection.getPassword())
           .build()) {

            ResultSet results = session.execute(
                SimpleStatement.builder( "SELECT * FROM killrvideo.user_credentials WHERE email = ?")
                        .addPositionalValues("cv@datastax.com")
                        .build());
            Row row = results.one();
            System.out.println("***************************************************************************************");
            if (row == null) {
                System.out.println("No row selected");
            } else {
                System.out.format("%s %s %s\n",
                    row.getString("email"),
                    row.getString("password"),
                    row.getUuid("userid"));
            }
            System.out.println("***************************************************************************************");
        }
         catch(Throwable t) {
            System.out.println("Failed Select");
            t.printStackTrace(); 
        }
   }
}
