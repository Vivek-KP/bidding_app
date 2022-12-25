
package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {


    Product findByProductName(String productName);


    List<Product> findProductsByCategoryId(String categoryId);


    Product findQuantityById(String productId);


    @Aggregation(pipeline = {
            "{'$match':{'isEnabled':true}}",
    })
    List<Product> findProductsForUser();


}




//    @Aggregation(pipeline = {
//            "{'$match':{'id':?0},}",
//            "{ '$set': { isEnabled: { {'$not': '$isEnabled' }"
//})

