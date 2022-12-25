package com.bidderApp.bidz.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BidRequest {

    private String name;
    private String bid;
    private String auctionId;



}
