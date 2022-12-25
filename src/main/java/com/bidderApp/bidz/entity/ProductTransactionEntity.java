package com.bidderApp.bidz.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;

@Document(collection = "transactions")
@Getter
@Setter
@AllArgsConstructor
public class ProductTransactionEntity extends AuditMetaData {
    @Id
    private String id;
    private Double credits;
    private Date transactionDate;
    private Boolean isAuction;
    private String userId;
    private String auctionId;
    private String productId;

}
