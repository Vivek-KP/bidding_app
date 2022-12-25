
package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.Category;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.entity.SubCategory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubCategoryRepository extends MongoRepository<SubCategory, String> {
    Category findBySubCategoryName(String subCategoryName);


    @Aggregation(pipeline = {
            "{'$match':{'categoryID':?0}}",
    })
    List<SubCategory> findSubcategories(String categoryID);
}
