package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.model.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductImageRepository extends MongoRepository<ProductImage, String> {
}
