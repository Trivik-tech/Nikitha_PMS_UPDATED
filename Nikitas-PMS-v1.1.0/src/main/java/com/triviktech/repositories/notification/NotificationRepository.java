package com.triviktech.repositories.notification;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.triviktech.entities.notification.Notification;

/**
 * Repository interface for managing {@link Notification} entities.
 * <p>
 * This extends {@link JpaRepository} to provide basic CRUD operations and 
 * defines custom query methods for fetching notifications based on 
 * delivery status and receiver.
 * </p>
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Fetches all undelivered notifications for a specific receiver.
     *
     * @param receiver the ID or identifier of the receiver
     * @return a list of {@link Notification} objects where delivered = false
     */
    List<Notification> findByReceiverAndDeliveredFalse(String receiver);

    /**
     * Fetches the latest 50 delivered notifications for a specific receiver,
     * ordered by the most recent timestamp in descending order.
     *
     * @param receiver the ID or identifier of the receiver
     * @return a list of up to 50 {@link Notification} objects where delivered = true
     */
    List<Notification> findTop50ByReceiverAndDeliveredTrueOrderByTimestampDesc(String receiver);
}
