package com.example.chat.websocket;

import com.example.chat.model.ChatMessage;
import com.example.chat.service.ChatService;
import com.example.chat.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight WebSocket handler that connects users, authenticates with JWT,
 * and relays messages through Kafka via ChatService.
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtService jwtService;
    private final ChatService chatService;

    // active user sessions
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(JwtService jwtService, ChatService chatService) {
        this.jwtService = jwtService;
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Example: ws://localhost:8081/ws/chat?token=<JWT>
        String token = getQueryParam(session, "token");
        if (token == null || !jwtService.validateToken(token)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing token"));
            return;
        }

        String username = jwtService.getUsername(token);
        userSessions.put(username, session);

        System.out.println("✅ User connected: " + username);
        session.sendMessage(new TextMessage("Welcome " + username + "! WebSocket connection established."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Expected JSON message body: { "roomId": "general", "receiver": "raj", "content": "Hi!" }
        try {
            ChatMessage msg = mapper.readValue(message.getPayload(), ChatMessage.class);

            // Basic validation
            if (msg.getSender() == null) {
                msg.setSender(getUsernameFromSession(session));
            }
            if (msg.getTimestamp() == 0) {
                msg.setTimestamp(System.currentTimeMillis());
            }

            // 1️⃣ Send to Kafka (and persist via ChatService)
            chatService.sendAndPersist(msg);

            // 2️⃣ Acknowledge sender
            session.sendMessage(new TextMessage("✔️ Message sent to Kafka topic."));

        } catch (Exception e) {
            session.sendMessage(new TextMessage("❌ Error: " + e.getMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String user = removeUser(session);
        if (user != null) {
            System.out.println("❌ User disconnected: " + user);
        }
    }

    /**
     * Broadcasts a message to all connected WebSocket sessions.
     * Called by ChatConsumer when Kafka delivers a message.
     */
    public void broadcast(ChatMessage msg) {
        try {
            String payload = mapper.writeValueAsString(msg);
            for (WebSocketSession session : userSessions.values()) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(payload));
                }
            }
        } catch (Exception ignored) {}
    }

    private String removeUser(WebSocketSession session) {
        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                userSessions.remove(entry.getKey());
                return entry.getKey();
            }
        }
        return null;
    }

    private String getQueryParam(WebSocketSession session, String key) {
        if (session.getUri() == null) return null;
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return null;
    }

    private String getUsernameFromSession(WebSocketSession session) {
        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        return "unknown";
    }
}
