package com.triviktech.utilities.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.triviktech.services.notification.NotificationService;

/**
 * Listener class to handle WebSocket connection events.
 *
 * This component listens for WebSocket connection events (when a user connects)
 * and triggers the sending of any pending notifications to the connected user.
 */
@Component
public class WebSocketConnectListener {

    @Autowired
    private NotificationService notificationService;

    /**
     * Handles the WebSocket connection event.
     *
     * This method is triggered when a new WebSocket session is established.
     * It retrieves the username of the connected user and calls the notification
     * service to send any pending notifications to that user.
     *
     * @param event the WebSocket session connected event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // Wrap the event message to access STOMP headers
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // If no user is associated with the session, do nothing
        if (accessor.getUser() == null) return;

        // Get the username of the connected user
        String username = accessor.getUser().getName();

        // Send any pending notifications to the connected user
        notificationService.sendOnConnect(username);
    }
}
