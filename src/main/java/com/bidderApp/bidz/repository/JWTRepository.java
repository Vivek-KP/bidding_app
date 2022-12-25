package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JWTRepository extends MongoRepository<JwtToken,String> {
    public JwtToken findByJwtToken(String token);
}
