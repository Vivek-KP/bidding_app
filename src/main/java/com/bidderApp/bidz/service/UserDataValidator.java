package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.exception.DataAlreadyExistException;
import com.bidderApp.bidz.exception.InvalidDataFoundException;
import com.bidderApp.bidz.repository.PersonRepository;
//import com.bidderApp.bidz.repository.BidderUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Objects;

@Service
public class UserDataValidator {

    @Autowired
    private PersonRepository adminRepository;



    //email already exist checking function
    public Boolean emailAlreadyExistCheck(String email){
        PersonEntity checkEmailAlreadyExist = adminRepository.findByEmail(email);
        return checkEmailAlreadyExist != null;
    }

    //username already exist checking function
    public Boolean userNameAlreadyExistCheck(String userName){
        PersonEntity checkUserNameAlreadyExist = adminRepository.findByUsername(userName);
        return checkUserNameAlreadyExist != null;
    }


    //phone number already exist checking function
    public Boolean phoneNumberAlreadyExistCheck(String phoneNumber){
        PersonEntity checkPhoneNumberAlreadyExist = adminRepository.findByPhoneNumber(phoneNumber);
        return checkPhoneNumberAlreadyExist != null;
    }

    //checking user data already exist function
//    public boolean updateValidationCheck(PersonEntity alreadyExistData,String checkingData,String existingCheckingData) {
//        return checkingData != null && alreadyExistData == null || Objects.equals(checkingData, existingCheckingData);
//    }


    //function for checking activities are done by current user
    public boolean checkActivityByAuthenticatedUser(Principal principal,PersonEntity personEntity){
        return principal.getName().equals(personEntity.getEmail()) || principal.getName().equals(personEntity.getPhoneNumber());
    }


    //function for image validation
    public Boolean imageValidation(MultipartFile image) {
        String filename = image.getOriginalFilename();
        int index = filename.indexOf(".");
        String fileExtension = filename.substring(index+1);
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png");
    }

//    public void updateDataAlreadyExistFunction(PersonEntity body, PersonEntity admin) {
//        if (updateValidationCheck(adminRepository.findByUsername(body.getUsername()),
//                body.getUsername(), admin.getUsername())) {
//            admin.setUsername(body.getUsername());
//        } else throw new DataAlreadyExistException("Username Already Exist");
//        if(Objects.isNull(admin.getEmail()) ){
//            if(Objects.isNull(body.getEmail())){
//                throw new InvalidDataFoundException("Enter a valid email");
//            }else {
//                if(emailAlreadyExistCheck(body.getEmail())){
//                    throw new DataAlreadyExistException("Email number Already Exist");
//                }else
//                    admin.setEmail(body.getEmail());
//            }
//        }else if(Objects.isNull(admin.getPhoneNumber())){
//            if(Objects.isNull(body.getPhoneNumber())){
//                throw new InvalidDataFoundException("Enter a valid phone number");
//            }else {
//                if(phoneNumberAlreadyExistCheck(body.getPhoneNumber())) {
//                    throw new DataAlreadyExistException("phonenumber Already Exist");
//                } else admin.setPhoneNumber(body.getPhoneNumber());
//            }
//        }
//    }


}
