package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "orders")
@Getter
@Setter
@AllArgsConstructor
public class OrderEntity extends AuditMetaData {
    @Id
    private String id;

    private String orderStatus;
    private Date orderDate;
    private Boolean isAuction;
    private String userId;
  //  private Double price;
  //  private  Integer creditValue;
    private String auctionId;
    private String productId;
}
