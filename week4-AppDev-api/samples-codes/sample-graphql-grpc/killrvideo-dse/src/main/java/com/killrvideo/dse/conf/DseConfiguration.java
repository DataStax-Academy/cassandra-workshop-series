package com.killrvideo.dse.conf;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.dse.DseCluster.Builder;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.auth.DsePlainTextAuthProvider;
import com.datastax.driver.mapping.DefaultPropertyMapper;
import com.datastax.driver.mapping.MappingConfiguration;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.PropertyMapper;
import com.datastax.driver.mapping.PropertyTransienceStrategy;
import com.google.common.collect.ImmutableMap;
import com.killrvideo.dse.utils.LongToTimeStampCodec;

/**
 * Connectivity to DSE (cassandra, graph, search).
 */
@Configuration
public class DseConfiguration {

	/** Internal logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DseConfiguration.class);
    
    @Value("#{'${dse.contactPoints}'.split(',')}")
    public List < String > contactPoints;
    
    @Value("${dse.port: 9042}")
    public int port;
    
    @Value("${dse.keyspace: system}")
    public String keyspace;
    
    @Value("${dse.username}")
    public Optional < String > dseUsername;
   
    @Value("${dse.password}")
    public Optional < String > dsePassword;
    
    @Value("${dse.localdc}")
    public String localDc;
    
    @Bean
    public DseSession dseSession() {
        long top = System.currentTimeMillis();
        LOGGER.info("Initializing connection to DSE Cluster");
        
        Builder clusterConfig = new Builder();
        LOGGER.info(" + Contact Points : {}" , contactPoints);
        contactPoints.stream().forEach(clusterConfig::addContactPoint);
        LOGGER.info(" + Listening Port : {}", port);
        clusterConfig.withPort(port);
        
        if (dseUsername.isPresent() && dsePassword.isPresent() && dseUsername.get().length() > 0) {
            AuthProvider cassandraAuthProvider = new DsePlainTextAuthProvider(dseUsername.get(), dsePassword.get());
            clusterConfig.withAuthProvider(cassandraAuthProvider);
            LOGGER.info(" + With username  : {}", dseUsername.get());
        }
         
        // OPTIONS
        clusterConfig.withQueryOptions(
                new QueryOptions().setConsistencyLevel(ConsistencyLevel.QUORUM));
        
        // LOAD BALANCING
        clusterConfig.withLoadBalancingPolicy(
                new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().withLocalDc(localDc).build()));
        
        // Long <-> Timestamp
        clusterConfig.withCodecRegistry(new CodecRegistry().register(new LongToTimeStampCodec()));
        
          try {
            
            // First Connect without Keyspace (to create if needed)
            DseSession tmpSession = null;
            try {
              tmpSession = clusterConfig.build().connect();
              tmpSession.execute(
                  SchemaBuilder.createKeyspace(keyspace)
                      .ifNotExists()
                      .with()
                      .replication(ImmutableMap.of("class", "SimpleStrategy", "replication_factor", 1)));
              LOGGER.info(" + Creating keyspace '{}' (if needed)", keyspace);
            } finally {
              if (tmpSession != null) {
                tmpSession.close();
              }
            }
          
            DseSession dseSession = clusterConfig.build().connect(keyspace);
            LOGGER.info(" + Connection established to DSE Cluster \\_0_/ in {} millis.", System.currentTimeMillis() - top);
            return dseSession;
        } catch(InvalidQueryException iqe) {
            LOGGER.error("\n-----------------------------------------\n\n"
                    + "Keyspace '{}' seems does not exist. \nPlease update 'application.yml' with correct keyspace name or create one with:\n\n"
                    + "  create keyspace {} WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}; \n\nI will create the "
                    + "tables I need after that.\n-----------------------------------------", 
                    keyspace, keyspace);
            throw new IllegalStateException("", iqe);
        }
    }
    
    /**
     * Use to create mapper and perform ORM on top of Cassandra tables.
     * 
     * @param session
     *      current dse session.
     * @return
     *      mapper
     */
    @Bean
    public MappingManager mappingManager(DseSession session) {
        // Do not map all fields, only the annotated ones with @Column or @Fields
        PropertyMapper propertyMapper = new DefaultPropertyMapper()
                .setPropertyTransienceStrategy(PropertyTransienceStrategy.OPT_IN);
        // Build configuration from mapping
        MappingConfiguration configuration = MappingConfiguration.builder()
                .withPropertyMapper(propertyMapper)
                .build();
        // Sample Manager with advance configuration
        return new MappingManager(session, configuration);
    }  
    
}
