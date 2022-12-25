package com.bidderApp.bidz.model.dto;

import com.bidderApp.bidz.enumerator.AuctionStatus;
import com.bidderApp.bidz.model.ProductImage;
import com.bidderApp.bidz.model.Specification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @Id
    String id;
    @NotBlank(message = "product name cannot be blank")
    @NotNull(message = "product name is mandatory")
    String productName;
    String slug;
    @NotNull(message = "product description is mandatory")
    String productDescription;
    String categoryId;
    String subCategoryId;
    @NotNull(message = "product brand is mandatory")
    String productBrand;
    @Positive(message = "value must be positive")
    Double productMRP;
    @Positive(message = "value must be positive")
    Integer productQuantity;
    @Positive(message = "value must be positive")
    Double minBidPrice;
    @Size(max = 5, message = "enter upto 5 tags")
    List<String> tags;
    List<Specification> specifications;
    ProductImage productImage;
    int requiredCredit;
    Boolean isEnabled = true;

}
