

package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository <Category, String> {
//to find the category name in db
  Category findByCategoryName(String categoryName);


  }

