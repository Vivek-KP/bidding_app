package com.bidderApp.bidz.model.dto;

import com.bidderApp.bidz.enumerator.AuctionStatus;
import com.bidderApp.bidz.model.ProductImage;
import com.bidderApp.bidz.model.Specification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

import javax.validation.constraints.Size;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProductDto {

    String id;
    String productName;
    String productDescription;
    String categoryId;
    String subCategoryId;
    String productBrand;
    Double productMRP;
    Double minBidPrice;
    List<String> tags;
    List <Specification>specifications;
    ProductImage productImage;
    int requiredCredit;
    String slug;


}
