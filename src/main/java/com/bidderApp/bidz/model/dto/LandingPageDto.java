package com.bidderApp.bidz.model.dto;

import com.bidderApp.bidz.model.ProductImage;
import com.bidderApp.bidz.model.Specification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LandingPageDto {
    String id;
    long auctionStartDate;
    long auctionEndTime;
    String productId;
    String productName;
    ProductImage productImage;
    Double productMRP;
    String currentBidder;
    long currentBid;
    long startingBid;
    String productDescription;
    List<Specification> specifications;
}
