
package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Document(collection = "auctions")
@Getter
@Setter
@AllArgsConstructor
public class AuctionEntity extends AuditMetaData{
    @Id
    private String id;
    @NotNull(message = "Auction quantity required ")
    private int auctionQuantity;
    @NotNull(message = "Starting bid required ")
    private long startingBid;
    private long currentBid;
    private String currentBidder;
    @NotBlank(message = " ProductID required ")
    private String productId;
    private long auctionStartDate;
    private long auctionEndTime;
}

