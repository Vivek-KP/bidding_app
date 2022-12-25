
package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderEntity,String> {
    public List<OrderEntity> findByUserId(String userId);
}
