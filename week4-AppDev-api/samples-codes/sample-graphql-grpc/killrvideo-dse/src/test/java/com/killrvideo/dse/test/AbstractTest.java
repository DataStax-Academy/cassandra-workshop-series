package com.killrvideo.dse.test;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseCluster.Builder;
import com.datastax.driver.dse.DseSession;
import com.google.common.collect.ImmutableMap;
import com.killrvideo.dse.model.SchemaConstants;
import com.killrvideo.dse.utils.BlobToStringCodec;

/**
 * Superclass holding session and cluster.
 *
 * @author DataStax evangelist team.
 */
public abstract class AbstractTest implements SchemaConstants {

    /** Limit. **/
    protected static final int DEFAULT_LIMIT_COUNT = 1000000;
    
    /** You may want to access cluster during your test. */
    protected DseCluster dseCluster = null;
    
    /** You may want to access dsSession during your test. */
    protected DseSession dseSession = null;
   
    /**
     * Initialization of DSESession and cluster from Address.
     */
    @BeforeEach
    public void init() {
        if (dseCluster == null) {
            Builder clusterConfig = new Builder();
            clusterConfig.addContactPoint(getContactPointAdress());
            clusterConfig.withPort(getContactPointPort());
            dseCluster = clusterConfig.build();
            dseCluster.getConfiguration().getCodecRegistry().register(new BlobToStringCodec());
            dseSession = dseCluster.connect();
        }
    }
        
    /**
     * Access Session.
     */
    protected DseSession getCurrentDseSession() {
        return dseSession;
    }
    
    /**
     * Access Cluster.
     */
    protected DseCluster getCurrentDseCluster() {
        return dseCluster;
    }
    
    /**
     * Test Helpers
     */
    protected void assertReturnedRecordsNumber(int expected, String cqlQuery) {
        Assert.assertEquals(expected, dseSession.execute(cqlQuery).getAvailableWithoutFetching());
    }
  
    /**
     * Query count all with the limit clause.
     */
    protected long countAll(String keyspace, String tableName) {
        return dseSession.execute(
                QueryBuilder.select().countAll()
                            .from(keyspace, tableName)
                            .limit(DEFAULT_LIMIT_COUNT).toString()).iterator().next().getLong(0);
    }
    
    /**
     * Test Helpers
     */
    protected void assertTableIsNotEmpty(String keyspace, String tableName) {
        Assert.assertNotEquals(0, countAll(keyspace, tableName));
    }
    
    /**
     * Test Helpers
     */
    protected void assertCountItemInTable(int expected, String keyspace, String tableName) {
        Assert.assertEquals(expected, countAll(keyspace, tableName));
    }
    
    /**
     * Test Helpers
     */
    protected void assertTableIsEmpty(String keyspace, String tableName) {
        assertCountItemInTable(0, keyspace, tableName);
    }
    
    /**
     * Test Helpers
     */
    protected void assertTableExist(String keyspace, String tableName) {
        KeyspaceMetadata ks = dseCluster.getMetadata().getKeyspace(keyspace);
        Assert.assertNotNull(ks);
        TableMetadata table = ks.getTable(tableName);
        Assert.assertNotNull(table);
    }
    
    /**
     * CreateIf not exist and connect.
     */
    public void connectKeyspace(String keyspace) {
        createKeySpace(keyspace, "SimpleStrategy", 1);
        dseSession = dseCluster.connect(keyspace);
    }
    
    /**
     * Create KeySpace
     */
    protected void createKeySpace(String keySpace) {
        createKeySpace(keySpace, "SimpleStrategy", 1);
    }
    
    /**
     * Create KeySpace
     */
    protected void createKeySpace(String keySpace, String strategy, int replicationFactor) {
        dseSession.execute(SchemaBuilder.createKeyspace(keySpace)
                .ifNotExists().with()
                .replication(ImmutableMap.of("class", strategy, "replication_factor", replicationFactor)));
    }
    
    /** Define Your Cassandra target. */
    protected abstract String getContactPointAdress();
   
    /** Define Your Cassandra target. */
    protected abstract int getContactPointPort();
    
    /** Define Your default level. */
    protected abstract ConsistencyLevel getConsistencyLevel();
    
}
