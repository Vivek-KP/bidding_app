package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationEntity,String > {

    public List<NotificationEntity> findByUserId(String userId);
}
