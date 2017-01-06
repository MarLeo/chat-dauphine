package com.dauphine.chat.config;


import com.dauphine.chat.chatendpoint.ChatWebSocketHandler;
import com.dauphine.chat.data.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by marti on 03/01/2017.
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    @Autowired
    private final RoomRepository roomRepository;
    @Autowired protected ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(roomRepository), "/chat/{room}");
    }
}
