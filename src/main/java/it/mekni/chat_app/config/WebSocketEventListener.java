package it.mekni.chat_app.config;

import it.mekni.chat_app.chat.ChatMessage;
import it.mekni.chat_app.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations mesageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("user disconected :{}", username);
            var chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .sender(username)
                    .build();
            mesageTemplate.convertAndSend("/topic/public",chatMessage);
        }

    }
}
