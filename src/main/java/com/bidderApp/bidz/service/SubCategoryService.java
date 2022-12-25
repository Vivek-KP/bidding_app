

package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.Category;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.entity.SubCategory;
import com.bidderApp.bidz.exception.DataAlreadyExistException;
import com.bidderApp.bidz.exception.InvalidDataFoundException;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.repository.CategoryRepository;
import com.bidderApp.bidz.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public ResponseEntity saveSubCategory(SubCategory subCategory) {

        String subCategoryName = subCategory.getSubCategoryName();
        Category categoryExist=  categoryRepository.findById(subCategory.getCategoryID()).orElse(null);
        if (categoryExist == null) {
            throw new NoDataFoundException("category doesn't exist");
        }
        if (subCategoryRepository.findBySubCategoryName(subCategoryName) != null) {
            throw new DataAlreadyExistException("SubCategory name already exist");
        }


        subCategoryRepository.save(subCategory);
        return new ResponseEntity<>("SubCategory added", HttpStatus.CREATED);

    }

    // Service for editing category
    public ResponseEntity edit(String id, SubCategory subCategory) {
        String subCategoryName = subCategory.getSubCategoryName();//categoryName in request body
        SubCategory subCategoryExist = (subCategoryRepository.findById(id).orElse(null));
        if (subCategoryExist != null) {
            String subCategoryNameInDb = subCategoryExist.getSubCategoryName();
            Category categoryExist=  categoryRepository.findById(subCategory.getCategoryID()).orElse(null);
            if (categoryExist == null) {
                throw new NoDataFoundException("category doesn't exist");
            }
            if ((subCategoryRepository.findBySubCategoryName(subCategoryName) == null || subCategoryNameInDb.equals(subCategoryName)) ) {
                subCategoryExist.setSubCategoryName(subCategory.getSubCategoryName());
                subCategoryRepository.save(subCategoryExist); //to save the updated user
                return new ResponseEntity<>("SubCategory updated", HttpStatus.OK);

            } else {
                throw new DataAlreadyExistException("SubCategory name  already exist");
            }
        } else {
            throw new NoDataFoundException("SubCategory not found");
        }
    }
    //service for deleting category
    public ResponseEntity delete(String id) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElse(null);
        if (subCategory != null) {
            subCategoryRepository.delete(subCategory);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } else {
            throw new NoDataFoundException("SubCategory not found");
        }
    }
    //service for listing category
    public List<SubCategory> viewAll() {
        return subCategoryRepository.findAll();
    }
    public List<SubCategory> viewSubCategoryByCategory(String id) {
        Category categoryExist=  categoryRepository.findById(id).orElse(null);
        if (categoryExist == null) {
            throw new NoDataFoundException("category doesn't exist");
        }
        return (subCategoryRepository.findSubcategories(id));
    }
}







