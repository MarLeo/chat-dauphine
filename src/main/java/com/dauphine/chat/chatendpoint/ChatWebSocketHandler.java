package com.dauphine.chat.chatendpoint;

import com.dauphine.chat.data.MessageRepository;
import com.dauphine.chat.domain.Message;
import com.google.gson.Gson;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by marti on 03/01/2017.
 */

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler implements ApplicationListener<SessionConnectEvent> {

    private static final Logger LOGGER = LogManager.getLogger(ChatWebSocketHandler.class);
    private final MessageRepository messageRepository;
    HashSet<String> users = new HashSet<>();
    String username = null;
    private List<WebSocketSession> sessions = new ArrayList<>();
    @Autowired
    private HttpSession session;

    @Autowired
    public ChatWebSocketHandler(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    synchronized void addSession(final WebSocketSession webSocketSession) {
        this.sessions.add(webSocketSession);
    }


    @Override
    public void onApplicationEvent(SessionConnectEvent event) {

    }


    @Override
    public void afterConnectionEstablished(final WebSocketSession webSocketSession) throws Exception {
        //Principal principal = webSocketSession.getPrincipal();
        String[] uriParts = getStrings(webSocketSession);
        webSocketSession.getAttributes().put("room", uriParts[2]);
        addSession(webSocketSession);
        //username = principal.getName();
        LOGGER.log(Level.INFO, String.format("Session opened by %s with session id %s with URI %s in room %s", username, webSocketSession.getId(), webSocketSession.getUri().toString(), uriParts[2]));

    }


    @Override
    public void handleTextMessage(final WebSocketSession webSocketSession, final TextMessage textMessage) {
        String room = getRoom(webSocketSession);
        try {
            for (WebSocketSession session : sessions) {
                if (session.isOpen() && room.equals(session.getAttributes().get("room"))) {
                    //TODO Map message & generate date without gson
                    Map<String, String> value = getMap(textMessage);
                    session.getAttributes().put("sender", value.get("sender"));
                    session.sendMessage(new TextMessage(new Gson().toJson(value)));
                    Message message = new Message(room, value.get("sender"), value.get("message"));
                    this.messageRepository.save(message);
                    users.add(value.get("sender"));
                    LOGGER.log(Level.INFO, String.format("new message %s from %s in room %s at %s", value.get("message"), value.get("sender"), room, value.get("date")));
                    LOGGER.log(Level.INFO, String.format("Users connected in room %s are %s", room, users.toString()));

                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "handleMessage failed", e);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.log(Level.INFO, String.format("Error in session %s closed by %s from room %s at %d because of %s", webSocketSession.getId(), webSocketSession.getAttributes().get("sender"), getRoom(webSocketSession), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()), throwable));
        sessions.remove(webSocketSession);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        //LOGGER.log(Level.INFO, String.format("Session %s closed by %s from room %s at %d because of %s", webSocketSession.getId(), webSocketSession.getAttributes().get("sender"),getName(webSocketSession), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()), closeStatus.getReason()));
        LOGGER.log(Level.INFO, String.format("Session %s closed because of %s", webSocketSession.getId(), closeStatus.getReason()));
        sessions.remove(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    // =========================================> Private Methods <====================================================
    private String[] getStrings(WebSocketSession webSocketSession) {
        return webSocketSession.getUri().toString().split("/");
    }

    private String getRoom(WebSocketSession webSocketSession) {
        return (String) webSocketSession.getAttributes().get("room");
    }

    private Map<String, String> getMap(TextMessage textMessage) {
        Map<String, String> value = new Gson().fromJson(textMessage.getPayload(), Map.class);
        value.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return value;
    }


    private Set<String> getUserNames() {
        HashSet<String> set = new HashSet<>();
        Iterator<WebSocketSession> iterator = sessions.iterator();
        while (iterator.hasNext()) set.add(iterator.next().getAttributes().get("sender").toString());
        return set;
    }


}
