package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Promocode {
    @Id
    private String id;
    @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$",message = "Contains only number and alphabets with min 5 and max 10 letters")
    private String promoCode;
    @Pattern(regexp = "([1-9]|[1-9][0-9]|100)",message = "value must between 1 and 100")
    private String discount;
}
