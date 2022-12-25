package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.CreditPackEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreditPackRepository extends MongoRepository<CreditPackEntity,String > {

}
