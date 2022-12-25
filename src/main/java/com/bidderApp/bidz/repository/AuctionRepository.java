package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.AuctionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuctionRepository extends MongoRepository<AuctionEntity,String> {

}
