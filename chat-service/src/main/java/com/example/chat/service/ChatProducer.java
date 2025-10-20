package com.example.chat.service;

import com.example.chat.model.ChatMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatProducer {

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public ChatProducer(KafkaTemplate<String, ChatMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ChatMessage message) {
        // key can be roomId to preserve ordering per room
        String key = message.getRoomId() != null ? message.getRoomId() : "global";
        kafkaTemplate.send("chat-topic", key, message);
    }
}
