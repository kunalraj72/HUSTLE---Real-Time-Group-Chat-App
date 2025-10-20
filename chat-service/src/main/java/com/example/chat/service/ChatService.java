package com.example.chat.service;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatService {

    private final ChatProducer producer;
    private final ChatMessageRepository repository;

    public ChatService(ChatProducer producer, ChatMessageRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    /**
     * Entry to push message: publish to Kafka and persist.
     * Persistence is done here synchronously (you can also persist in consumer).
     */
    public void sendAndPersist(ChatMessage msg) {
        if (msg.getTimestamp() == 0) msg.setTimestamp(Instant.now().toEpochMilli());
        producer.sendMessage(msg);
        // persist quickly (you can do async if you prefer)
        repository.save(msg);
    }
}
