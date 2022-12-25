package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.UserAndBid;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserAndBidRepository extends MongoRepository<UserAndBid, String > {
    List<UserAndBid> findBidsByUserId(String userId);
    List<UserAndBid> findBidsByUserIdAndAuctionId(String userId,String auctionId);

    List<UserAndBid> findBidsByAuctionId(String auctionId);

    UserAndBid findUserByAuctionIdAndBid(int bid, String auctionId);
}
