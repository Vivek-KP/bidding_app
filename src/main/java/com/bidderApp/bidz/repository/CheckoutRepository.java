package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.CheckoutEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CheckoutRepository extends MongoRepository<CheckoutEntity,String> {
}
