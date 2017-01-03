package com.dauphine.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by marti on 10/12/2016.
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

        @Override
        public void registerStompEndpoints(final StompEndpointRegistry registry) {
                registry.addEndpoint("/chat-app").withSockJS();
        }

        @Override
        public void configureMessageBroker(final MessageBrokerRegistry registry){
                registry.enableSimpleBroker("/queue/", "/topic/", "/exchange/");
                registry.setApplicationDestinationPrefixes("/app");
        }

}
