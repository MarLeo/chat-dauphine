package com.dauphine.chat.web;



/**
 * Controller that handles WebSocket chat messages
 *
 * Created by marti on 10/12/2016.
 */

public class ChatController {
 /*

        @Autowired private ParticipantRepository participantRepository;

        //@Autowired private SimpMessagingTemplate simpMessagingTemplate;

        private static final Logger LOGGER = LogManager.getLogger(ChatController.class);


        @SubscribeMapping("/chat.participants")
        public Collection<LoginEvent> retrieveParticipants(){
            return participantRepository.getActiveSessions().values();
        }


        @MessageMapping("/chat.message")
        public ChatMessage filterMessage(@Payload final ChatMessage message, final Principal principal){
            message.setUsername(principal.getName());
            LOGGER.log(Level.INFO, String.format(" new message from %s %s", message.getUsername(), message));
            return message;
        }


        @MessageMapping("/chat.private.{username}")
        public void filterPrivateMessage(@Payload final ChatMessage message, @DestinationVariable("username") final String username, Principal principal){
            message.setUsername(principal.getName());
            LOGGER.log(Level.INFO,String.format("%s is sending a private message to %s (%s)", message.getUsername(), username, message.getMessage()));
            //simpMessagingTemplate.convertAndSend("/user/" + username + "/exchange/amq.direct/chat.message", message);
        }
*/

}
