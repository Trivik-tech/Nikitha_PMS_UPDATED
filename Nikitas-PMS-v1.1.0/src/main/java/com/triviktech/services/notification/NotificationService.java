// package com.triviktech.services.notification;

// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.messaging.simp.user.SimpUser;
// import org.springframework.messaging.simp.user.SimpUserRegistry;
// import org.springframework.stereotype.Service;

// import com.triviktech.entities.notification.Notification;
// import com.triviktech.repositories.notification.NotificationRepository;

// @Service
// public class NotificationService {

//     @Autowired
//     private NotificationRepository repo;

//     @Autowired
//     private SimpUserRegistry simpUserRegistry;

//     @Autowired
//     private SimpMessagingTemplate messagingTemplate;

    
//     public void saveMessage(String sender, String receiver, String content, String destination, boolean delivered) {
//         Notification notification = new Notification();
//         notification.setSender(sender);
//         notification.setReceiver(receiver);
//         notification.setContent(content);
//         notification.setTimestamp(LocalDateTime.now());
//         notification.setDelivered(delivered);
//         notification.setDestination(destination);
//         repo.save(notification);
//     }


//     public List<Notification> getPendingNotification(String username) {
//         List<Notification> list = repo.findByReceiverAndDeliveredFalse(username);
//         list.forEach(n -> n.setDelivered(true));
//         repo.saveAll(list);
//         return list;
//     }

   
     
// //     // public void sendMessage(String sender, String receiver, String content, String destination) {
// //     //     for (SimpUser user : simpUserRegistry.getUsers()) {
// //     //     System.out.println("🟢 Online user: " + user.getName());
// //     // }
// //     //     boolean isOnline = simpUserRegistry.getUser(receiver) != null;

// //     //     if (isOnline) {
// //     //         messagingTemplate.convertAndSendToUser(receiver, destination, content);
// //     //         saveMessage(sender, receiver, content, destination, true);
// //     //     } else {
// //     //         saveMessage(sender, receiver, content, destination, false);
// //     //     }
// //     // }
//  }


package com.triviktech.services.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import com.triviktech.entities.notification.Notification;
import com.triviktech.repositories.notification.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

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

    public void sendMessageWithRecent(String sender, String receiver, String content, String destination) {
        boolean isOnline = simpUserRegistry.getUser(receiver) != null;

        if (isOnline) {
            messagingTemplate.convertAndSendToUser(receiver, destination, content);
            saveMessage(sender, receiver, content, destination, true);
        } else {
            saveMessage(sender, receiver, content, destination, false);
        }
    }

    public List<Notification> getPendingNotification(String username) {
        List<Notification> list = repo.findByReceiverAndDeliveredFalse(username);
        list.forEach(n -> n.setDelivered(true));
        return repo.saveAll(list);
    }

    public void sendOnConnect(String username) {
        List<Notification> undelivered = getPendingNotification(username);
        undelivered.forEach(n ->
            messagingTemplate.convertAndSendToUser(username, n.getDestination(), n.getContent())
        );
    }
}