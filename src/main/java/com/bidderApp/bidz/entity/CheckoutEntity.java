package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutEntity {
    @Id
    private String id;
    private String productName;
    private String image;
    private double total;
    private double subtotal;
    private int quantity;
}
