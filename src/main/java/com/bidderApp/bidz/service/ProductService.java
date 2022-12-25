
package com.bidderApp.bidz.service;


import com.bidderApp.bidz.model.ResponseModel;
import com.bidderApp.bidz.model.dto.UserProductDto;
import com.bidderApp.bidz.entity.Category;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.repository.CategoryRepository;
import com.bidderApp.bidz.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    ModelMapper modelMapper = new ModelMapper();

    private UserProductDto userProductDtoConvertor(Product product) {
        return modelMapper.map(product, UserProductDto.class);
    }
    //To view all products by admin
    public Object getProductByID(String id) {
        Product productExist = productRepository.findById(id).orElse(null);
        if (productExist == null) {
            throw new NoDataFoundException("Product doesn't exist");

        } else {
            return new ResponseEntity<>(productRepository.findById(id), HttpStatus.OK);
        }
    }
    public ResponseModel<List<Product>> getProductsByCategory(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            List<Product>products=productRepository.findProductsByCategoryId(id);
            return new ResponseModel<List<Product>>(true, products);
        } else {
            throw new NoDataFoundException("category doesn't exist");
        }
    }
    public ResponseModel<List<Product>>  getProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) throw new NoDataFoundException("No products found");
        return new ResponseModel<List<Product>>(true, products);

    }
    //To view all products by user
    public ResponseModel<List<UserProductDto>> getProductsForUser() {
        List<Product> products = productRepository.findProductsForUser();

        List<UserProductDto>  productList=products.stream().map(this::userProductDtoConvertor)
                .collect(Collectors.toList());
        return new ResponseModel<List<UserProductDto>>(true, productList);

    }
    public ResponseModel<List<UserProductDto>> userGetProductsByCategory(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            List<Product> products = productRepository.findProductsForUser();
            List<UserProductDto>productList= products.stream().filter(f -> id.equals(f.getCategoryId())).map(this::userProductDtoConvertor)
                    .collect(Collectors.toList());
            return new ResponseModel<List<UserProductDto>>(true, productList);

        } else {
            throw new NoDataFoundException("category doesn't exist");
        }
    }


    public ResponseModel<UserProductDto> userGetProductByID(String id) {
        Product productExist = productRepository.findById(id).orElse(null);
        if (productExist != null) {
            if (productExist.getIsEnabled()) {
                UserProductDto productDto = userProductDtoConvertor(productExist);
                return new ResponseModel<UserProductDto>(true, productDto);
            }

        }
        throw new NoDataFoundException("Product doesn't exist");

    }
}







