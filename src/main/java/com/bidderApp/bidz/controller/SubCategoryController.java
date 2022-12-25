
package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.Category;
import com.bidderApp.bidz.entity.SubCategory;
import com.bidderApp.bidz.service.CategoryService;
import com.bidderApp.bidz.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    //To add a subCategory
    @PostMapping("/subcategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object addSubCategory(@RequestBody SubCategory subCategory){

        return subCategoryService.saveSubCategory(subCategory);



    }
    //To update subCategory
    @PutMapping("/subcategory/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object editCategory(@RequestBody SubCategory subCategory, @PathVariable("id")String id){
        return(subCategoryService.edit(id,subCategory));
    }


    //to delete subCategory by id
    @DeleteMapping("/subcategory/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object deleteCategory(@PathVariable("id")String id){
        return subCategoryService.delete(id);
    }


    //to view all sub categories
    @GetMapping("/subcategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")

    public List<SubCategory> viewAllSubCategories(){
        return subCategoryService.viewAll();
    }


    //to view subCategories by category
    @GetMapping("/subcategory/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")

    public Object viewSubCategoryByCat(@PathVariable("id")String id){

        return subCategoryService.viewSubCategoryByCategory(id);
    }


}

