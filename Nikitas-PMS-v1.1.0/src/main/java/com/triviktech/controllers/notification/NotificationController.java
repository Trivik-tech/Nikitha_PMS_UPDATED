package com.triviktech.controllers.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.triviktech.entities.notification.Notification;

import java.util.List;

@RequestMapping("/api/v1/pms")
public interface NotificationController {

    @GetMapping("/recent")
    ResponseEntity<List<Notification>> getRecentMessages(@AuthenticationPrincipal UserDetails data);
}
