
package com.bidderApp.bidz.repository;

import com.bidderApp.bidz.entity.PersonEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepository extends MongoRepository<PersonEntity,String> {

    public PersonEntity findByUsername(String username);

    public PersonEntity findByEmail(String email);


    @Aggregation(pipeline = {"{'$match':{'role':'ROLE_USER'}}",})
    List<PersonEntity> findUsers();

//    @Query(value="{ name : ?0}", fields="{ name : 0 }")
//    List<PersonEntity> findByPath();


    @Aggregation(pipeline = {"{'$match':{'role':'ROLE_ADMIN'}'$or'{'role':'ROLE_SUPER-ADMIN'}}",})
    List<PersonEntity> findAdmins();

    public PersonEntity findByPhoneNumber(String phoneNumber);
}
