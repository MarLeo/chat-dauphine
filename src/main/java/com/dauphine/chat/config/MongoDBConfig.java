package com.dauphine.chat.config;

/*import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;*/

import com.dauphine.chat.service.SecurityContextService;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 13/12/2016.
 */
/*
@Configuration
@PropertySource("application.properties")
public class MongoDBConfig {

        private static final Logger LOGGER = LogManager.getLogger(MongoDBConfig.class);

        @Value("${spring.data.mongodb.host}")
        private String host;
        @Value("${spring.data.mongodb.port}")
        private int port;
        @Value("${spring.data.mongodb.database}")
        private String database;
        @Value("${spring.session.mongo.collection-name}")
        private String collection;

        public @Bean MongoClient mongoClient(){
                LOGGER.log(Level.INFO, String.format("Connection to mongodb server on %s : %s", host, port));
                return new MongoClient(host, port);
        }

        public @Bean
        MongoDatabase mongoDatabase(){
                LOGGER.log(Level.INFO, String.format("Connecting to database %s", database));
                return mongoClient().getDatabase(database);
        }

        public @Bean MongoCollection<Document> mongoCollection(){
            LOGGER.log(Level.INFO, String.format("Connecting to collection %s", collection));
            return mongoDatabase().getCollection(collection);
        }

}*/

@Configuration
@EnableConfigurationProperties(MongoSettings.class)
@ComponentScan(basePackages = {"com.dauphine.chat"})
@EnableMongoRepositories(basePackages = {"com.dauphine.chat.data"})
public class MongoDBConfig {

        @Bean
        public MongoTemplate mongoTemplate(final MongoClient mongoClient,
                                           final MongoSettings mongoSettings) throws Exception {
                return new MongoTemplate(mongoClient, mongoSettings.getDatabase());
        }

        @Configuration
        @EnableConfigurationProperties(MongoSettings.class)
        @Profile("!test")
        static class MongoClientConfiguration {

                @Bean
                public MongoClient mongoClient(final MongoSettings mongoSettings) throws Exception {
                        ServerAddress serverAddress = new ServerAddress(
                                mongoSettings.getHost(), mongoSettings.getPort());

                        /*MongoCredential credential = MongoCredential.createCredential(
                                mongoSettings.getUsername(),
                                mongoSettings.getDatabase(),
                                mongoSettings.getPassword().toCharArray());

                        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                        credentials.add(credential);*/

                        return new MongoClient(serverAddress/*, credentials*/);
                }
        }

        @Configuration
        static class SpringSecurityConfiguration {

                @Bean
                public SecurityContextService securityContextService() {
                        return new SecurityContextService();
                }

                @Bean
                public AuthenticationKeyGenerator authenticationKeyGenerator() {
                        return new DefaultAuthenticationKeyGenerator();
                }

                @Bean
                public PasswordEncoder passwordEncoder() {
                        return NoOpPasswordEncoder.getInstance(); //TODO change encoder
                }

                @Bean
                public ClientKeyGenerator clientKeyGenerator(){
                        return new DefaultClientKeyGenerator();
                }

        }
}
