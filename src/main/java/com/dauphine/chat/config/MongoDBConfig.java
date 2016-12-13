package com.dauphine.chat.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by marti on 13/12/2016.
 */

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



}
