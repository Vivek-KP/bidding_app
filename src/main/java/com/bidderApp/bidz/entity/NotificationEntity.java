package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Notifications")
@Getter
@Setter
@AllArgsConstructor
public class NotificationEntity {
    @Id
    private String id;
    private String notificationType;
    private String notificationContent;
    private Date notificationDate;
    private Boolean notificationStatus;
    private String userId;
}
