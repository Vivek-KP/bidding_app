package com.bidderApp.bidz.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "bids")
public class UserAndBid {
    @Id
    private String id;
    private String userId;
    private String auctionId;
    private int bid;
}
