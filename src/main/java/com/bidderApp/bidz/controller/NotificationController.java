package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    //controller to get notifications for a specific user
    @GetMapping("/notifications/{id}")
    public Object getNotifications(@PathVariable(value = "id") String id){
        return notificationService.getNotifications(id);
    }

    //controller to get only unread notifications of a specific user
    @GetMapping("/notifications/{id}/unread")
    public Object getUnreadNotifications(@PathVariable(value = "id") String id){
        return notificationService.getUnreadNotifications(id);
    }
}
