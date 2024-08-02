package com.example.pinokkio.config;

import com.example.pinokkio.api.room.WebSocketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketService webSocketService;

    public WebSocketConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CustomWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }

    private class CustomWebSocketHandler extends TextWebSocketHandler {
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("New WebSocket connection: " + session.getId());
            webSocketService.addSession(session);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            webSocketService.handleMessage(session, message);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            System.out.println("WebSocket connection closed: " + session.getId() + ", status: " + status);
            webSocketService.removeSession(session);
        }
    }
}