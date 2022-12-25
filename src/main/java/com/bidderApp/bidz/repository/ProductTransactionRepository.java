package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.ProductTransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface ProductTransactionRepository extends MongoRepository<ProductTransactionEntity,String> {
    List<ProductTransactionEntity> findByUserId(String userId);

}
