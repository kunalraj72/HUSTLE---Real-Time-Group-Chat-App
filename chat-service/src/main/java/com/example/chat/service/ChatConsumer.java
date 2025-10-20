package com.example.chat.service;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.websocket.ChatWebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ChatConsumer {

    private final ChatWebSocketHandler webSocketHandler;
    private final ChatMessageRepository repo;

    public ChatConsumer(ChatWebSocketHandler webSocketHandler, ChatMessageRepository repo) {
        this.webSocketHandler = webSocketHandler;
        this.repo = repo;
    }

    @KafkaListener(topics = "chat-topic", groupId = "chat-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ChatMessage msg) {
        // Persist if not already persisted (safe-guard)
        repo.save(msg);
        // broadcast to connected WS sessions (handler will decide who to send)
        webSocketHandler.broadcast(msg);
    }
}
