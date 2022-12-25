package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.JwtToken;
import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.model.LoginData;
import com.bidderApp.bidz.model.dto.ChangePasswordDto;
import com.bidderApp.bidz.model.dto.UserDto;
import com.bidderApp.bidz.repository.PersonRepository;
import com.bidderApp.bidz.service.AdminCrudService;
import com.bidderApp.bidz.service.BidderUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;

@RestController
public class BidderUserController {

    @Autowired
    private BidderUserService bidderUserService;

    @Autowired
    private AdminCrudService adminCrudService;



    //signup api
    @PostMapping("/signup")
    public ResponseEntity<Object> addUser(@RequestBody PersonEntity body) throws IOException {
        return bidderUserService.userRegister(body);
    }

    //api for singup using phone number
    @PostMapping("/signup/phone")
    public ResponseEntity<Object> signUpUsingPhoneNumber(@RequestBody PersonEntity personEntity) throws IOException {
        return bidderUserService.userRegisterWithPhoneNumber(personEntity);
    }

    //api for login
    @PostMapping("/login")
    public ResponseEntity<Object>userlogin(@RequestBody LoginData body){
        return bidderUserService.userLogin(body);
    }


    //api for update
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> userUpdate(@PathVariable String id, @RequestBody PersonEntity body,Principal principal) {
        return bidderUserService.userdataUpdate(body, id,principal);
    }

    //api for delete
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> userDelete(@PathVariable String id,Principal principal){
        return  bidderUserService.delete(id,principal);
    }

    //api for getting the details of current user
    @GetMapping("/user/{id}")
    public UserDto userDetails(@PathVariable String id, Principal principal){
        return bidderUserService.currentUserDetails(id, principal);

    }


    //api for password update
    @PutMapping("/update/{id}/password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity passwordUpdate(@PathVariable String id, @RequestBody ChangePasswordDto password, Principal principal){
        return (ResponseEntity) adminCrudService.changePassword(id,password,principal);
    }


    //api for updating user image
    @PutMapping("/user/{id}/photo")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String addPhoto(@PathVariable  String id, @RequestParam("image") MultipartFile image) throws IOException {
      return   bidderUserService.addingImage(id, image);

    }

    //api for user logout
    @PostMapping ("/user/logout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> logout(@RequestBody JwtToken jwt){
       return (ResponseEntity<Object>) bidderUserService.logout(jwt);

    }

}

