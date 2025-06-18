// package com.triviktech.utilities.notification;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.event.EventListener;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.messaging.SessionConnectedEvent;

// import com.triviktech.entities.notification.Notification;
// import com.triviktech.services.notification.NotificationService;

// @Component
// public class WebSocketConnectListener {

//     @Autowired
//     private SimpMessagingTemplate messagingTemplate;

//     @Autowired
//     private NotificationService messageService;

//     @EventListener
//     public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//         StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

//         if (accessor.getUser() == null) {
//             System.out.println("❌ [WebSocket] Connected session missing Principal.");
//             return;
//         }

//         String username = accessor.getUser().getName();
//         System.out.println("🔔 [WebSocket] User connected: " + username);

//         List<Notification> pendingMessages = messageService.getPendingNotification(username);
//         System.out.println("📬 Pending messages for " + username + ": " + pendingMessages.size());

//         for (Notification msg : pendingMessages) {
//             System.out.println("➡️ Sending stored message to " + username + ": " + msg.getContent());
//             messagingTemplate.convertAndSendToUser(username, msg.getDestination(), msg.getContent());
//         }
//     }
// }



package com.triviktech.utilities.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.triviktech.services.notification.NotificationService;

@Component
public class WebSocketConnectListener {

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() == null) return;

        String username = accessor.getUser().getName();
        notificationService.sendOnConnect(username);
    }
}