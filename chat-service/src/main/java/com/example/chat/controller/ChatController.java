package com.example.chat.controller;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageRepository repository;

    public ChatController(ChatService chatService, ChatMessageRepository repository) {
        this.chatService = chatService;
        this.repository = repository;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    /**
     * Optional REST endpoint to post a chat message (useful for testing or bots).
     */
    @PostMapping("/messages")
    public ChatMessage postMessage(@RequestBody ChatMessage msg) {
        chatService.sendAndPersist(msg);
        return msg;
    }

    /**
     * Simple history endpoint (by room). You can expand to user-to-user queries.
     */
    @GetMapping("/history/{roomId}")
    public List<ChatMessage> history(@PathVariable String roomId) {
        return repository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
