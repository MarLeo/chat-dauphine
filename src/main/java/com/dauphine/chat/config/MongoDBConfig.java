package com.dauphine.chat.config;


import com.dauphine.chat.service.SecurityContextService;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

/**
 * Created by marti on 13/12/2016.
 */

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
                        return new MongoClient (serverAddress);
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
