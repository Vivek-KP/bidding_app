package com.bidderApp.bidz.service;
import com.bidderApp.bidz.model.ProductImage;
import com.bidderApp.bidz.model.dto.ProductDto;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.exception.DataAlreadyExistException;
import com.bidderApp.bidz.exception.InvalidDataFoundException;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.model.Specification;
import com.bidderApp.bidz.repository.CategoryRepository;
import com.bidderApp.bidz.repository.ProductRepository;
import com.bidderApp.bidz.service.helper.SlugConverter;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductAdminService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    ModelMapper modelMapper = new ModelMapper();
    private Product convertDTOtoEntity(ProductDto productDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Product product = new Product();
        product = modelMapper.map(productDTO, Product.class);
        return product;
    }
   //  service to add product
    public ResponseEntity<Object> saveProduct(ProductDto productDto) {
        if (productRepository.findByProductName(productDto.getProductName()) != null) {
            return new ResponseEntity<>("Product already exists", HttpStatus.CONFLICT);
        }
        if (categoryRepository.findById(productDto.getCategoryId()).isEmpty()) {
            throw new InvalidDataFoundException("invalid category");
        }
        productDto.setSlug(SlugConverter.toSlug(productDto.getProductName()));
        Product product = convertDTOtoEntity(productDto);
        productRepository.save(product);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }
    //service to edit product
    public Object edit(String id, Product product) {
        Product productExist = productRepository.findById(id).orElse(null);
        if (productExist != null) {
            String productName = product.getProductName();
            String categoryId = product.getCategoryId();
            String productNameInDb = productExist.getProductName();
            if (!productExist.getIsEnabled()) {
                throw new NoDataFoundException("Product cannot be updated, enable first");
            }
            if (categoryRepository.findById(categoryId).isEmpty()) {
                throw new InvalidDataFoundException("invalid category");
            }
            if (!Objects.equals(productNameInDb, productName)) {
                if (productRepository.findByProductName(productName) != null) {
                    throw new DataAlreadyExistException("Product name already exist");
                }
            }
            productExist.update(product);
            productRepository.save(productExist); //to save the updated product
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } else {
            throw new NoDataFoundException("Product doesn't exist");
        }
    }
    // service to disable product
    public ResponseEntity<?> productStatus(String id) {
        Product productExist = productRepository.findById(id).orElse(null);
        if (productExist != null) {
            if (productExist.getIsEnabled()) {
                productExist.setIsEnabled(false);
                productRepository.save(productExist);
                return new ResponseEntity<>("Product disabled", HttpStatus.OK);
            } else {
                productExist.setIsEnabled(true);
                productRepository.save(productExist);
                return new ResponseEntity<>("Product enabled", HttpStatus.OK);
            }
        } else {
            throw new NoDataFoundException("Product doesn't exist");
        }
    }
    public ResponseEntity<?> deleteProduct(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } else {
            throw new NoDataFoundException("Product not found");
        }
    }
    public ResponseEntity<?> deleteSpecification(String id, String value) {
        Product productExist = productRepository.findById(id).orElse(null);
        if (productExist != null) {
            List<Specification> specifications = productExist.getSpecifications();
            //to check if value exist in specification and store it in the variable
            specifications = specifications.stream()
                    .filter(f -> value.equals(f.getSpecificationName())).collect(Collectors.toList());
            if (specifications.isEmpty()) {
                throw new NoDataFoundException("specification doesn't exist");
            } else {
                Query query = Query.query(Criteria.where("_id").is(id));
                Update update = new Update().pull("specifications", Query.query(Criteria.where("specificationName").is(value)));
                UpdateResult updateResult = mongoTemplate.updateFirst(query, update, "product"); // "product" is the collection name
                return new ResponseEntity<>("Deleted Specification Successfully", HttpStatus.OK);
            }
        } else {
            throw new NoDataFoundException("Product not found");
        }
    }
    public ResponseEntity<?> addPhoto(String productId, MultipartFile file) throws IOException {
        Product productExist = productRepository.findById(productId).orElse(null);
        if (productExist != null) {

                if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpg")) {
                    ProductImage productImage=new ProductImage(new ObjectId(),Base64.getEncoder().encodeToString(new Binary(BsonBinarySubType.BINARY, file.getBytes()).getData()));
                    productExist.setProductImage(productImage);
                    productRepository.save(productExist);
                } else
                    throw new InvalidDataFoundException("file is not supported");
            return new ResponseEntity<>("uploaded Successfully", HttpStatus.OK);

        } else
            throw new NoDataFoundException("product doesn't exist");
    }
}




