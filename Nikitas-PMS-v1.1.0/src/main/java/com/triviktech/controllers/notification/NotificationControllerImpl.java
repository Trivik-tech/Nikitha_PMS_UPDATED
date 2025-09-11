package com.triviktech.controllers.notification;

import com.triviktech.entities.notification.Notification;
import com.triviktech.repositories.notification.NotificationRepository;
import com.triviktech.services.notification.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link NotificationController} responsible for managing
 * notification-related operations in the Performance Management System (PMS).
 *
 * <p>This class provides the logic to fetch both pending (undelivered) and
 * recently delivered notifications for the currently authenticated user.
 * It ensures that up to 50 notifications are returned, combining undelivered
 * messages with the latest delivered ones.</p>
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *   <li>Retrieve undelivered (pending) notifications from {@link NotificationService}.</li>
 *   <li>Fetch the latest delivered notifications from {@link NotificationRepository}.</li>
 *   <li>Merge both sets of notifications and return them as a single list.</li>
 * </ul>
 *
 * <p><strong>Annotations:</strong></p>
 * <ul>
 *   <li>{@link RestController} - Marks this class as a Spring REST controller.</li>
 *   <li>{@link Autowired} - Injects required service and repository dependencies.</li>
 * </ul>
 */

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
    public ResponseEntity<List<Notification>> getRecentMessages(@AuthenticationPrincipal UserDetails manager) {
         String username = manager.getUsername();


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
