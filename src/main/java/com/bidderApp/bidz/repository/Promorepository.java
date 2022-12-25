package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.Promocode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Promorepository extends MongoRepository<Promocode,String> {

    public Promocode findByPromoCode(String code);
}
