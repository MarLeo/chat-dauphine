package com.dauphine.chat.config;

import com.dauphine.chat.event.ParticipantRepository;
import com.dauphine.chat.event.PresenceEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by marti on 13/12/2016.
 */
@Configuration
public class ChatConfig {
    public static class Destinations{
        private Destinations(){
        }

        private static final String LOGIN = "/topic/chat.login";
        private static final String LOGOUT = "/topic/chat.logout";
    }



    @Bean
    @Description("Tracks user presence (join / leave) and broadcasts it to all connected users")
    public PresenceEventListener presenceEventListener(final SimpMessagingTemplate simpMessagingTemplate){
        PresenceEventListener presence = new PresenceEventListener(simpMessagingTemplate, participantRepository());
        presence.setLoginDestination(Destinations.LOGIN);
        presence.setLogoutDestination(Destinations.LOGOUT);
        return presence;
    }


    @Bean
    @Description("Keeps connected users")
    public ParticipantRepository participantRepository(){
        return new ParticipantRepository();
    }
}
