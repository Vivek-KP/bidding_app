package com.bidderApp.bidz.service.helper;

import com.bidderApp.bidz.entity.AuctionEntity;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.entity.UserAndBid;
import com.bidderApp.bidz.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionCrudHelper {

    @Autowired
    private ProductRepository productRepository;
    public Boolean checkQuantity(AuctionEntity auction, int quantity){
        String productId = auction.getProductId();
        Product product = productRepository.findQuantityById(productId);
        int totalQuantity = product.getProductQuantity();
        return quantity<=totalQuantity;
    }


}
