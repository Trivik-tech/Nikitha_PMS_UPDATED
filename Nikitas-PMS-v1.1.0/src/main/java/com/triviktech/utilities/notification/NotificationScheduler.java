package com.triviktech.utilities.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.triviktech.controllers.hr.HRControllerImpl;
import com.triviktech.controllers.manager.ManagerControllerImpl;

@Service
public class NotificationScheduler {

    @Autowired
    private HRControllerImpl hrControllerImpl;

    @Autowired
    // private ManagerControllerImpl managerControllerImpl;

    @Scheduled(cron = "0 */2 * * * ?", zone = "Asia/Kolkata")
    public void sendDailyNotifications() {
        // hrControllerImpl.notifyAllEmployeesAndManagers();
        // managerControllerImpl.autoNotifyEmployees();
    }
}
