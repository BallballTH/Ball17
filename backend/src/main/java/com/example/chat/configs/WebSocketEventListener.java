package com.example.chat.configs;

import com.example.chat.models.ChatMessage;
import com.example.chat.models.MessageType;
import com.example.chat.services.UserCountService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
@AllArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    // Handle user connect events
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username  != null) {
            // Remove the user from the active users set
            UserCountService.decrementUserCount();

            // Broadcast the updated user count
            messagingTemplate.convertAndSend("/topic/userCount", UserCountService.getUserCount());

            ChatMessage chatMessage = ChatMessage.buildChatmessage(username + "has left the chat." , username , MessageType.LEAVE);
            messagingTemplate.convertAndSend("/topic/messages" , chatMessage);
        }
    }
}
