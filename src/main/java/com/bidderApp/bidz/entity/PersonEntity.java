package com.bidderApp.bidz.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "personEntity")

public class PersonEntity extends AuditMetaData {

    @Id
    private String id;
    @Pattern(regexp = "^([a-zA-Z])+([a-zA-Z ]){3,}$",message = ("Name should only contain Alphabets"))
    private String name;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",message = ("Minimum eight characters at least one letter one number and one special character:"))
    private String username;

    private String slug;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.]+\\.[a-z]{2,3}$",message = ("Enter valid email"))
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$",message = ("Password should contain Minimum eight characters at least one uppercase letter one lowercase letter one number and one special character"))
    private String password;

    private String role;
    @Pattern(regexp = "^[0-9]{10}$",message = ("Enter valid phone-number"))
    private String phoneNumber;
    private String image;
    private String address;
    private String shippingAddress;
    private int creditBalance=0;
    private  int totalCredit=0;


}
