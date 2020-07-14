package com.datastax.workshop;

import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

public class TestUtils {
    
    public static void createKeyspaceForLocalInstance() {
        try (CqlSession cqlSession = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .build()) {
            cqlSession.execute(SchemaBuilder.createKeyspace("killrvideo")
                    .ifNotExists().withSimpleStrategy(1)
                    .withDurableWrites(true).build());
        }
    }
    
}
