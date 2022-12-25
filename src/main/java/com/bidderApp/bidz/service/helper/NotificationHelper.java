package com.bidderApp.bidz.service.helper;

import com.bidderApp.bidz.entity.NotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationHelper {
    public List<NotificationEntity> findUnreadNotifications(List<NotificationEntity> notifications){
        return notifications.stream().filter(NotificationEntity::getNotificationStatus).collect(Collectors.toList());
    }

}
