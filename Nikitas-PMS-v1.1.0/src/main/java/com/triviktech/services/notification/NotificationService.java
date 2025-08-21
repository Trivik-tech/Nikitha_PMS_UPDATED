package com.triviktech.services.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import com.triviktech.entities.notification.Notification;
import com.triviktech.repositories.notification.NotificationRepository;

/**
 * Service class for handling notifications between users.
 *
 * <p>
 * This service provides functionality to:
 * </p>
 * <ul>
 *     <li>Save notifications to the database.</li>
 *     <li>Send messages to online users via WebSocket using SimpMessagingTemplate.</li>
 *     <li>Store undelivered notifications for offline users and deliver them when they connect.</li>
 *     <li>Fetch pending (undelivered) notifications and mark them as delivered.</li>
 * </ul>
 *
 * <p>
 * Dependencies:
 * <ul>
 *     <li>{@link NotificationRepository} for database operations.</li>
 *     <li>{@link SimpMessagingTemplate} for sending WebSocket messages.</li>
 *     <li>{@link SimpUserRegistry} for checking online users.</li>
 * </ul>
 * </p>
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    /**
     * Saves a notification message to the database.
     *
     * @param sender      The username of the sender.
     * @param receiver    The username of the receiver.
     * @param content     The message content.
     * @param destination The WebSocket destination/topic for the message.
     * @param delivered   Whether the message was successfully delivered.
     */
    public void saveMessage(String sender, String receiver, String content, String destination, boolean delivered) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setContent(content);
        notification.setTimestamp(LocalDateTime.now());
        notification.setDelivered(delivered);
        notification.setDestination(destination);
        repo.save(notification);
    }

    /**
     * Sends a message to a user via WebSocket if online. 
     * If the user is offline, the message is saved for later delivery.
     *
     * @param sender      The username of the sender.
     * @param receiver    The username of the receiver.
     * @param content     The message content.
     * @param destination The WebSocket destination/topic for the message.
     */
    public void sendMessageWithRecent(String sender, String receiver, String content, String destination) {
        boolean isOnline = simpUserRegistry.getUser(receiver) != null;

        if (isOnline) {
            messagingTemplate.convertAndSendToUser(receiver, destination, content);
            saveMessage(sender, receiver, content, destination, true);
        } else {
            saveMessage(sender, receiver, content, destination, false);
        }
    }

    /**
     * Retrieves all pending (undelivered) notifications for a given user.
     * Marks them as delivered and saves the changes in the database.
     *
     * @param username The username of the receiver.
     * @return List of notifications that were pending for the user.
     */
    public List<Notification> getPendingNotification(String username) {
        List<Notification> list = repo.findByReceiverAndDeliveredFalse(username);
        list.forEach(n -> n.setDelivered(true));
        return repo.saveAll(list);
    }

    /**
     * Sends all undelivered notifications to a user when they connect.
     * Messages are sent via WebSocket to the respective destinations.
     *
     * @param username The username of the user who just connected.
     */
    public void sendOnConnect(String username) {
        List<Notification> undelivered = getPendingNotification(username);

        undelivered.forEach(n -> {
            messagingTemplate.convertAndSendToUser(username, n.getDestination(), n.getContent());
        });
    }
}
