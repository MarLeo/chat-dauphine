package com.dauphine.chat.chatendpoint;

import com.dauphine.chat.domain.ChatMessage;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 03/01/2017.
 */

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LogManager.getLogger(ChatWebSocketHandler.class);
    private List<WebSocketSession> sessions = new ArrayList<>();

    synchronized void addSession(final WebSocketSession webSocketSession) {
        this.sessions.add(webSocketSession);
    }


    @Override
    public void afterConnectionEstablished(final WebSocketSession webSocketSession /*,@PathParam("room") final String room*/) throws Exception {
        // LOGGER.log(Level.INFO, String.format("Session opened with sessions if %s and bound to room %s", webSocketSession.getId(), room));
        LOGGER.log(Level.INFO, String.format("Session opened with session id %s ", webSocketSession.getId()));
        addSession(webSocketSession);
        //webSocketSession.getAttributes().put("room", room);

    }



    @Override
    public void handleMessage(final WebSocketSession webSocketSession,  WebSocketMessage<?> webSocketMessage) throws Exception {
        /* String room = (String) webSocketSession.getAttributes().get("room"); */
        try {
            for (WebSocketSession session : sessions) {
                if(session.isOpen() /*&& room.equals(session.getAttributes().get("room"))*/) {
                    session.sendMessage(webSocketMessage);
                    LOGGER.log(Level.INFO, String.format("new message %s", webSocketMessage));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "handleMessage failed", e);
        }

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.log(Level.ERROR, String.format("error occured at sender %s", webSocketSession.getId()), throwable);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        LOGGER.log(Level.INFO, String.format("Session %s closed because of %s", webSocketSession.getId(), closeStatus.getReason()));
        sessions.remove(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
