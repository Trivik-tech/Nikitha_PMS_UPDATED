// package com.triviktech.repositories.notification;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.triviktech.entities.notification.Notification;

// public interface NotificationRepository extends JpaRepository<Notification,Long> {
//     List<Notification> findByReceiverAndDeliveredFalse(String receiver);

// }

package com.triviktech.repositories.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triviktech.entities.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverAndDeliveredFalse(String receiver);
    List<Notification> findTop50ByReceiverAndDeliveredTrueOrderByTimestampDesc(String receiver);
}