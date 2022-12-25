
package com.bidderApp.bidz.controller;


import com.bidderApp.bidz.entity.Category;
import com.bidderApp.bidz.model.ResponseModel;
import com.bidderApp.bidz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //crud operations for category

    @PostMapping("/category")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public Object addCategory(@RequestBody @Valid Category category){
        return categoryService.saveCategory(category);
    }

    @PutMapping ("/category/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public Object editCategory(@Valid @RequestBody Category category, @PathVariable("id")String id){
        return(categoryService.edit(id,category));
    }

    @DeleteMapping("/category/{id}")  //delete category by id
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public Object deleteCategory(@PathVariable("id")String id){

        return categoryService.delete(id);
    }

    @GetMapping("/category")   //to get categories
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN','ROLE_USER')")

    public ResponseModel<List<Category>> getAllCategories(){
        return categoryService.getAll();
    }

}
