
package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.model.ResponseModel;
import com.bidderApp.bidz.model.dto.ProductDto;
import com.bidderApp.bidz.model.dto.UserProductDto;
import com.bidderApp.bidz.service.ProductAdminService;
import com.bidderApp.bidz.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductAdminService productAdminService;


    @PostMapping("/admin/product")  //to save product
    @PreAuthorize("hasAnyRole('ROLE_SUPER-ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody ProductDto productDto) {
        return productAdminService.saveProduct(productDto);
    }

    @PutMapping("/admin/product/changeStatus/{id}")  //to enable or disable product
    @PreAuthorize("hasAnyRole('ROLE_SUPER-ADMIN','ROLE_ADMIN')")

    public Object productDisable(@PathVariable("id") String id) {
        return productAdminService.productStatus(id);
    }


    @PutMapping("/admin/product/{id}")  // update product
    @PreAuthorize("hasAnyRole('ROLE_SUPER-ADMIN','ROLE_ADMIN')")

    public Object productUpdate(@PathVariable("id") String id, @Valid @RequestBody Product product) {
        return productAdminService.edit(id, product);
    }

    @GetMapping("/admin/product/{id}") //get a product by id
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public Object getProductByID(@PathVariable("id") String id) {
        return productService.getProductByID(id);
    }

    @GetMapping("/admin/product/category/{id}")     //get products by a category id
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")

    public ResponseModel<List<Product>> getProductsByCategory(@PathVariable("id") String id) {
        return productService.getProductsByCategory(id);
    }


    @GetMapping("/admin/product")  //get all products
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public Object getProducts() {

        return productService.getProducts();
    }

    @DeleteMapping("/admin/product/{id}") //delete product by id
    @PreAuthorize("hasAnyRole('ROLE_SUPER-ADMIN','ROLE_ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        return productAdminService.deleteProduct(id);
    }

    @PutMapping("/admin/product/{id}/{value}")  // delete specification
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> deleteSpecification(@PathVariable("id") String id, @PathVariable("value") String value) {
        return productAdminService.deleteSpecification(id, value);
    }

    @PutMapping("/product/{id}/upload") //to upload image
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")

    public ResponseEntity addPhoto(@PathVariable("id") String productId,
                                   @RequestPart("image") MultipartFile image, Model model)
            throws IOException {
        return productAdminService.addPhoto(productId, image);

    }

    @GetMapping("/product")
    //  @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseModel
            <List<UserProductDto>> getProductsForUsers() {
        return productService.getProductsForUser();

    }

    @GetMapping("/product/{id}") //get a product by id
   // @PreAuthorize("hasRole('ROLE_USER')")
    public Object userGetProductByID(@PathVariable("id") String id) {
        return productService.userGetProductByID(id);
    }

    @GetMapping("/product/category/{id}")     //get products by a category id
  //  @PreAuthorize("hasRole('ROLE_USER')")

    public Object userGetProductsByCategory(@PathVariable("id") String id) {
        return productService.userGetProductsByCategory(id);
    }

}



