package com.bidderApp.bidz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "photos")
public class ImageEntity {
    @Id
    private String id;

    private String title;

    private String image;


    public ImageEntity(String title) {
    }
}

