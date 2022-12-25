
package com.bidderApp.bidz.entity;

import com.bidderApp.bidz.model.ProductImage;
import com.bidderApp.bidz.model.Specification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document(collection = "product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AuditMetaData {
    @Id
    String id;

    String productName;
    String productDescription;
    String slug;
    String categoryId;
//    String subCategoryId;
    String productBrand;
    Double productMRP;
    Integer productQuantity;
    Double minBidPrice;
    List<String> tags;
    List<Specification> specifications;
    int requiredCredit;
    ProductImage productImage;
    Boolean isEnabled = true;

    public void update(Product product){
      this.setProductName(product.getProductName());
      this.setProductBrand(product.getProductBrand());
      this.setProductMRP(product.getProductMRP());
      this.setProductDescription(product.getProductDescription());
      this.setProductQuantity(product.getProductQuantity());
      this.setCategoryId(product.getCategoryId());
      this.setMinBidPrice(product.getMinBidPrice());
      this.setRequiredCredit(product.getRequiredCredit());
      this.setTags(product.getTags());
      this.setSpecifications(product.getSpecifications());
  }
}




