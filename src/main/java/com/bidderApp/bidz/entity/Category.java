
package com.bidderApp.bidz.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "category")
public class Category extends AuditMetaData {
    @Id
    String id;
    @NotNull(message = "category name mandatory")
    @NotBlank
    String categoryName;
}

