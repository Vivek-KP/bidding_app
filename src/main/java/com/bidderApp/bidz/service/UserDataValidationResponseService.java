package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.entity.UserDisable;
import com.bidderApp.bidz.exception.DataAlreadyExistException;
import com.bidderApp.bidz.exception.InvalidDataFoundException;
import com.bidderApp.bidz.exception.UnauthorizedException;
import com.bidderApp.bidz.model.AuthenticationResponse;
import com.bidderApp.bidz.model.LoginData;
import com.bidderApp.bidz.repository.BannedUserRepository;
import com.bidderApp.bidz.repository.PersonRepository;
import com.bidderApp.bidz.service.helper.SlugConverter;
import com.bidderApp.bidz.implementation.utility.JwtUtil;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserDataValidationResponseService {

    @Autowired
    private UserDataValidator userDataValidator;

    @Autowired
    private PersonRepository adminRepository;

    @Autowired
    private  NonAuthService nonAuthService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SlugConverter slugConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BannedUserRepository bannedUserRepository;


    //image is taking from the resource
    Resource resource = new ClassPathResource("userDefault.jpg");
//    InputStream input = resource.getInputStream();
    File file =  resource.getFile();
    public UserDataValidationResponseService() throws IOException {
    }

    //user registration validating function
    public ResponseEntity userRegisterValidationResponse(PersonEntity body) throws IOException {
        if (body.getName() != null && body.getUsername() != null  && body.getPassword() != null &&(body.getEmail() != null || body.getPhoneNumber() != null)) {
            if(body.getEmail()!=null){
                return userRegistrationWithPhoneOrEmail(body,userDataValidator.emailAlreadyExistCheck(body.getEmail()), "Email already exist");
            }else {
               return userRegistrationWithPhoneOrEmail(body, userDataValidator.phoneNumberAlreadyExistCheck(body.getPhoneNumber()),"Phone Number already exist");
            }
        }else
            throw new InvalidDataFoundException("All fields are mandatory");
    }



    //user registration with phone number or with email id
    @NotNull
    private ResponseEntity userRegistrationWithPhoneOrEmail(PersonEntity body, boolean checkEmailOrPhoneNumberAlreadyExist, String response) throws IOException {
        if (checkEmailOrPhoneNumberAlreadyExist || userDataValidator.userNameAlreadyExistCheck(body.getUsername())) {
            if (checkEmailOrPhoneNumberAlreadyExist) {
                throw new DataAlreadyExistException(response);
            } else
                throw new DataAlreadyExistException("Username already exist");
        }else {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            String image = Base64.getEncoder().encodeToString(new Binary(BsonBinarySubType.BINARY, bytes).getData());
            new ResponseEntity(adminRepository.save(new PersonEntity(
                    body.getId(), body.getName(), body.getUsername(),body.getSlug(), body.getEmail(), body.getPassword(),"ROLE_USER",
                    body.getPhoneNumber(), image ,body.getAddress(), body.getShippingAddress(), body.getCreditBalance(), body.getTotalCredit())),
                    HttpStatus.CREATED);
            return new ResponseEntity("Registered successfully",HttpStatus.OK);
        }
    }

    //user login and generate token function
    @NotNull
    public ResponseEntity<Object> loginFunctionAndGenerateToken(LoginData body) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getEmailOrPhoneNumber(), body.getPassword()));

        }catch (Exception e) {
            AuthenticationResponse response =
                    new AuthenticationResponse(null,null,null, "Wrong Email/Phone or Password", HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        PersonEntity personEntity = adminRepository.findByEmail(body.getEmailOrPhoneNumber());
        PersonEntity person = adminRepository.findByPhoneNumber(body.getEmailOrPhoneNumber());
        String id = null;
        String name = null;
        if (Objects.isNull(person)) {
            id = personEntity.getId();
            name = personEntity.getName();

        } else {
            id = person.getId();
            name =person.getName();
        }
        UserDisable userDisable = bannedUserRepository.findByBannedUserId(id);
        final UserDetails userDetails = nonAuthService.loadUserByUsername(body.getEmailOrPhoneNumber());
        if (userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining("")).equals("ROLE_USER") && Objects.isNull(userDisable) ) {
            final String jwt = jwtUtil.generateToken(userDetails, id);
            AuthenticationResponse response = new AuthenticationResponse(jwt,id,name, "Logged in", HttpStatus.OK.value());
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }return new ResponseEntity<Object>("Wrong Email or Password", HttpStatus.OK);
    }

    
    //user update function
    public ResponseEntity<String> userUpdateFunction(PersonEntity body, Principal principal, PersonEntity personEntity) {
        if(userDataValidator.checkActivityByAuthenticatedUser(principal, personEntity)) {
            if(Objects.isNull(adminRepository.findByUsername(body.getUsername()))) {
                try {
//                userDataValidator.updateDataAlreadyExistFunction(body, admin);
                    personEntity.setName(body.getName());
                    personEntity.setUsername(body.getUsername());
                    personEntity.setAddress(body.getAddress());
                    personEntity.setShippingAddress(body.getShippingAddress());
                    adminRepository.save(personEntity);
                    return new ResponseEntity<String>("User Updated Successfully", HttpStatus.OK);
                } catch (Exception e) {
                    throw new InvalidDataFoundException(e.getMessage());
                }
            }throw new DataAlreadyExistException("Username already exist");
        }
            throw new UnauthorizedException("Unauthorized action");


    }



}
