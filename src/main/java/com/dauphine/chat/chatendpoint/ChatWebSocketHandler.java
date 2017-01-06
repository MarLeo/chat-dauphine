package com.dauphine.chat.chatendpoint;

import com.dauphine.chat.data.RoomRepository;
import com.dauphine.chat.domain.Room;
import com.google.gson.Gson;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by marti on 03/01/2017.
 */

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LogManager.getLogger(ChatWebSocketHandler.class);
    private final RoomRepository roomRepository;
    private List<WebSocketSession> sessions = new ArrayList<>();


    @Autowired
    public ChatWebSocketHandler(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    synchronized void addSession(final WebSocketSession webSocketSession) {
        this.sessions.add(webSocketSession);
    }


    @Override
    public void afterConnectionEstablished(final WebSocketSession webSocketSession) throws Exception {
        String[] uriParts = getUriParts(webSocketSession);
        LOGGER.log(Level.INFO, String.format("Session opened with session id %s with URI %s in room %s", webSocketSession.getId(), webSocketSession.getUri().toString(), uriParts[2]));
        webSocketSession.getAttributes().put("room", uriParts[2]);
        addSession(webSocketSession);
    }



    @Override
    public void handleTextMessage(final WebSocketSession webSocketSession, final TextMessage textMessage) {
        String room = getRoom(webSocketSession);
        try {
            for (WebSocketSession session : sessions) {
                if (session.isOpen() && room.equals(session.getAttributes().get("room"))) {
                    Map<String, String> value = getMap(textMessage);
                    session.getAttributes().put("sender", value.get("sender"));
                    session.sendMessage(new TextMessage(new Gson().toJson(value)));
                    Room r = new Room(room, value.get("sender"), value.get("message"));
                    this.roomRepository.save(r);
                    LOGGER.log(Level.INFO, String.format("new message %s from %s in room %s at %s", value.get("message"), value.get("sender"), room, value.get("date")));
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
        //LOGGER.log(Level.INFO, String.format("Session %s closed by %s from room %s at %d because of %s", webSocketSession.getId(), webSocketSession.getAttributes().get("sender"),getRoom(webSocketSession), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()), closeStatus.getReason()));
        LOGGER.log(Level.INFO, String.format("Session %s closed because of %s", webSocketSession.getId(), closeStatus.getReason()));
        sessions.remove(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    // =========================================> Private Methods <====================================================
    private String[] getUriParts(WebSocketSession webSocketSession) {
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

}
