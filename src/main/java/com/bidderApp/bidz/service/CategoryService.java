

package com.bidderApp.bidz.service;


import com.bidderApp.bidz.entity.Category;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.exception.DataAlreadyExistException;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.model.ResponseModel;
import com.bidderApp.bidz.repository.CategoryRepository;
import com.bidderApp.bidz.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    //service for adding new category by an admin
    public ResponseEntity<?> saveCategory(Category category) {

        String categoryName = category.getCategoryName();//from request body
        if (categoryRepository.findByCategoryName(categoryName) != null) {
            throw new DataAlreadyExistException("Category name  already exist");

        }

        categoryRepository.save(category);
        return new ResponseEntity<>("Category created", HttpStatus.CREATED);
    }
    // Service for editing category
    public ResponseEntity<?> edit(String id, Category category) {

        String categoryName = category.getCategoryName();//categoryName in request body
        Category categoryExist = (categoryRepository.findById(id).orElse(null));
        if (categoryExist != null) {
            String categoryNameInDb = categoryExist.getCategoryName();
            if ((categoryRepository.findByCategoryName(categoryName) == null || categoryNameInDb.equals(categoryName))) {
                categoryExist.setCategoryName(category.getCategoryName());
                categoryRepository.save(categoryExist); //to save the updated user
                return new ResponseEntity<>("Category updated", HttpStatus.OK);

            } else {
                throw new DataAlreadyExistException("Category name  already exist");
            }
        } else {
            throw new NoDataFoundException("Category not found");
        }
    }

    //service for deleting category
    public ResponseEntity<?> delete(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            //to delete category id in product
            List<Product> products = productRepository.findProductsByCategoryId(id);
            for (Product p : products) {
                p.setCategoryId(null);

                productRepository.save(p);
            }
            categoryRepository.delete(category);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } else {

            throw new NoDataFoundException("Category not found");
        }
    }

    //service for listing category
    public ResponseModel<List<Category>> getAll() {
        if (categoryRepository.findAll().isEmpty()) {
            throw new NoDataFoundException("No categories  found");
        }

        List<Category> category=categoryRepository.findAll();
        return new ResponseModel<List<Category>>(true,category);
    }
}

