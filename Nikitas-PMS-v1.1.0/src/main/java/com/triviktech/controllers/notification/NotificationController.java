package com.triviktech.controllers.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.triviktech.entities.notification.Notification;

import java.util.List;

/**
 * REST controller interface for handling notification-related requests
 * in the Performance Management System (PMS).
 *
 * <p>This controller provides an endpoint to fetch the most recent
 * notifications for the currently authenticated user.</p>
 *
 * <p><strong>Base Path:</strong> /api/v1/pms</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@link RequestMapping} - Defines the base API path for notifications.</li>
 *   <li>{@link GetMapping} - Maps HTTP GET requests to fetch recent notifications.</li>
 *   <li>{@link AuthenticationPrincipal} - Injects the details of the currently authenticated user.</li>
 * </ul>
 *
 * @author YourName
 * @since 1.0
 */
@RequestMapping("/api/v1/pms")
public interface NotificationController {

    /**
     * Fetches the most recent notifications for the currently authenticated user.
     *
     * @param data the authenticated user details (automatically injected by Spring Security)
     * @return a {@link ResponseEntity} containing a list of {@link Notification} objects
     */
    @GetMapping("/recent")
    ResponseEntity<List<Notification>> getRecentMessages(@AuthenticationPrincipal UserDetails data);
}

