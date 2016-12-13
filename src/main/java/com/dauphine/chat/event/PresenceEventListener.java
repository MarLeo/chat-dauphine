package com.dauphine.chat.event;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

/**
 * Listener to track user presence.
 * Sends notifications to the login destination when a connect event is received
 * and notifications to the logout destination when a disconnect event is received
 *
 * Created by marti on 10/12/2016.
 */
public class PresenceEventListener {

        private static final Logger LOGGER = LogManager.getLogger(PresenceEventListener.class);

        private ParticipantRepository participantRepository;

        private SimpMessagingTemplate messagingTemplate;

        private  String loginDestination;

        private String logoutDestination;

        public PresenceEventListener(final SimpMessagingTemplate messagingTemplate, final ParticipantRepository participantRepository){
            this.messagingTemplate = messagingTemplate;
            this.participantRepository = participantRepository;
        }


        @EventListener
        private void handleSessionConnected(final SessionConnectEvent event){
            SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
            String username = headers.getUser().getName();

            LoginEvent loginEvent = new LoginEvent(username);
            messagingTemplate.convertAndSend(loginDestination, loginEvent);
            LOGGER.log(Level.INFO, String.format("new connection event from %s at %s", loginEvent.getUsername(), loginEvent.getDate()));

            // We store the session as we need to be idempotent in the disconnet event processing
            participantRepository.add(headers.getSessionId(), loginEvent);
        }


        @EventListener
        private void handleSessionDisconnect(final SessionDisconnectEvent event){
            Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
                        .ifPresent(login -> {
                                LogoutEvent logoutEvent = new LogoutEvent(login.getUsername());
                                messagingTemplate.convertAndSend(logoutDestination, logoutEvent);
                                participantRepository.removeParticipant(event.getSessionId());
                            LOGGER.log(Level.INFO, String.format("new disconnection event from %s at %s", logoutEvent.getUsername(), logoutEvent.getDate()));
                        });

        }

        public void setLoginDestination(final String loginDestination){
            this.loginDestination = loginDestination;
        }

        public void setLogoutDestination(final String logoutDestination){
            this.logoutDestination = logoutDestination;
        }






















}
