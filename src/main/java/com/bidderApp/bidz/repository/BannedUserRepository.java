package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.UserDisable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannedUserRepository extends MongoRepository<UserDisable, String> {

    public UserDisable findByBannedUserId(String id);
}
