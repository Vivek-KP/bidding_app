
package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credit-pack")
@Getter
@Setter
@AllArgsConstructor
public class CreditPackEntity extends AuditMetaData {
    @Id
    private String id;

    private int creditValue;
    private Long creditCost;
    private int packDiscount;
    private String adminId;
}
