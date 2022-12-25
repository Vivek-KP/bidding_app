package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.NotificationEntity;
import com.bidderApp.bidz.repository.NotificationRepository;
import com.bidderApp.bidz.service.helper.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationHelper notificationHelper;

    //function to get all notifications for a user
    public Object getNotifications(String id) {
        List<NotificationEntity> notifications = notificationRepository.findByUserId(id);
        if(notifications.isEmpty()){
            return new ResponseEntity<>("No notifications", HttpStatus.OK);
        }
        return notifications;
    }

    //function to get unread notifications
    public Object getUnreadNotifications(String id) {
        List<NotificationEntity> unreadNotifications =
                notificationHelper.findUnreadNotifications(notificationRepository.findByUserId(id));
        if(unreadNotifications.isEmpty()){
            return new ResponseEntity<>("No unread notifications",HttpStatus.OK);
        }
        return unreadNotifications;
    }
}
