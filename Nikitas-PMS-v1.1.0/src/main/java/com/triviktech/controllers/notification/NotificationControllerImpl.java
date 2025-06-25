package com.triviktech.controllers.notification;

import com.triviktech.controllers.notification.NotificationController;
import com.triviktech.entities.notification.Notification;
import com.triviktech.repositories.notification.NotificationRepository;
import com.triviktech.services.notification.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationControllerImpl(NotificationService notificationService,
            NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public ResponseEntity<List<Notification>> getRecentMessages() {
        // In real implementation, fetch authenticated username from SecurityContext
         String username = "EMP1234";
        System.out.println("✅ Authenticated username: " + username);

        List<Notification> undelivered = notificationService.getPendingNotification(username);
        int remaining = 50 - undelivered.size();

        List<Notification> recent = notificationRepository
                .findTop50ByReceiverAndDeliveredTrueOrderByTimestampDesc(username)
                .stream()
                .limit(Math.max(remaining, 0))
                .toList();

        List<Notification> all = new ArrayList<>();
        all.addAll(undelivered);
        all.addAll(recent);

        return ResponseEntity.ok(all);
    }
}
