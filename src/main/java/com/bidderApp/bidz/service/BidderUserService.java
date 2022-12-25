package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.JwtToken;
import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.exception.*;
import com.bidderApp.bidz.model.LoginData;
import com.bidderApp.bidz.model.dto.AdminDto;
import com.bidderApp.bidz.model.dto.UserDto;
import com.bidderApp.bidz.repository.JWTRepository;
import com.bidderApp.bidz.repository.PersonRepository;
import com.bidderApp.bidz.implementation.utility.JwtUtil;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class BidderUserService {

    @Autowired
    private PersonRepository adminRepository;

    @Autowired
    private JWTRepository jwtRepository;

    @Autowired
    private UserDataValidator userDataValidator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDataValidationResponseService userDataValidationResponseService;

    @Autowired
    private  NonAuthService nonAuthService;
    @Autowired
    private JwtUtil jwtUtil;


    //displaying only needed data of user
    ModelMapper modelMapper = new ModelMapper();

    private AdminDto getAdminDtoConverter(PersonEntity person) {
        return modelMapper.map(person, AdminDto.class);
    }


    //User signup with username,name,email and password
    public  ResponseEntity<Object> userRegister(PersonEntity body) throws IOException {
        return userDataValidationResponseService.userRegisterValidationResponse(body);
    }

//    private ResponseEntity<Object> userEnteringData(PersonEntity body) throws IOException {
//        return userDataValidationResponseService.userRegisterValidationResponse(body);
//    }


    //user signup using phone number
    public ResponseEntity<Object> userRegisterWithPhoneNumber(PersonEntity personEntity) throws IOException {
        return userDataValidationResponseService.userRegisterValidationResponse(personEntity);
    }



//    Login with phoneNumber/email and password
    public ResponseEntity<Object> userLogin(LoginData body) {
        if (!body.getEmailOrPhoneNumber().isEmpty() && !body.getPassword().isEmpty()) {
            return userDataValidationResponseService.loginFunctionAndGenerateToken(body);
        }  throw new InvalidDataFoundException("All fields are mandatory");

    }


    //user update
    public ResponseEntity<String> userdataUpdate(PersonEntity body, String id,Principal principal){
        PersonEntity admin = adminRepository.findById(id).orElse(null);
        if (admin!=null) {
            return userDataValidationResponseService.userUpdateFunction(body, principal, admin);
        }
            throw new NoDataFoundException("User Not Found");
    }



    //deleting a user
    public ResponseEntity<String> delete(String id,Principal principal) {
        PersonEntity validUser= adminRepository.findById(id).orElse(null);
        if (validUser!=null) {
            if (userDataValidator.checkActivityByAuthenticatedUser(principal, validUser)) {
                adminRepository.deleteById(id);
                return new ResponseEntity<String>("User Deleted Successfully", HttpStatus.OK);
            }throw new UnauthorizedException("Unauthorized action");

        } throw new NoDataFoundException("User Not Found");
    }


    //function for adding image
    public String addingImage(String id, MultipartFile image) throws IOException {
        PersonEntity person = adminRepository.findById(id).orElse(null);
        if (Objects.nonNull(person)) {
            if (userDataValidator.imageValidation(image)) {
                person.setImage(Base64.getEncoder().encodeToString(new Binary(BsonBinarySubType.BINARY, image.getBytes()).getData()));
                adminRepository.save(person);
                return "Image added successfully";
            }
            return "Unsupported file.Supported files are:jpeg,jpg,png";
        }return "User Not found";
    }



    //logout function
    public ResponseEntity<Object> logout(JwtToken jwt) {
      jwtRepository.save(new JwtToken(null,jwt.getJwtToken()));
      return new ResponseEntity<>("successfully logged out",HttpStatus.OK) ;
    }


  //  currently logged user details
    public UserDto currentUserDetails(String id, Principal principal) {
        PersonEntity validUser = adminRepository.findById(id).orElse(null);
        if(validUser!=null) {
            return modelMapper.map(validUser, UserDto.class);
        }throw  new NoDataFoundException("User not found");
    }


    //listing all the user
    @NotNull
    public Object userList() {
        List<PersonEntity> admins = adminRepository.findUsers();
        List<AdminDto>userList =  admins.stream().map(this::getAdminDtoConverter)
                .collect(Collectors.toList());
        if(Objects.nonNull(userList)){
            return userList;
        }return "No user found";
    }
}








