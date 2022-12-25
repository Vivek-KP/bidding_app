
package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.model.AuthenticationRequest;
import com.bidderApp.bidz.model.AuthenticationResponse;
import com.bidderApp.bidz.repository.PersonRepository;
import com.bidderApp.bidz.service.NonAuthService;
import com.bidderApp.bidz.implementation.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class NonAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NonAuthService nonAuthService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PersonRepository adminRepository;

    //admin authentication controller
    @PostMapping("/authenticate")
    public Object createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        //checking email and password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
            //catch if email and password is wrong
        } catch (Exception e) {
            AuthenticationResponse response = new AuthenticationResponse(
                    null,null,null, "Wrong Email or Password", HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        final UserDetails userDetails = nonAuthService.loadUserByUsername(authenticationRequest.getEmail());
        PersonEntity personEntity = adminRepository.findByEmail(authenticationRequest.getEmail());
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));
        if (role.equals("ROLE_ADMIN")|| role.equals("ROLE_SUPER-ADMIN")) {
            final String jwt = jwtUtil.generateToken(userDetails, personEntity.getId());
            AuthenticationResponse response = new AuthenticationResponse(jwt, personEntity.getId(), personEntity.getName(), "Logged in", HttpStatus.OK.value());
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }return new ResponseEntity<Object>("Wrong Email or Password", HttpStatus.OK);
    }
}
