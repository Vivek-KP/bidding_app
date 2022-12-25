
package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Arrays;

import java.util.List;

@Service
public class NonAuthService implements UserDetailsService {

    @Autowired
    private PersonRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        PersonEntity existingAdminWithEmail = adminRepository.findByEmail(email);
        PersonEntity existingAdminWithPhone = adminRepository.findByPhoneNumber(email);


        List<SimpleGrantedAuthority> roles = null;

        if (existingAdminWithEmail!= null && existingAdminWithEmail.getEmail()!=null) {
            roles = List.of(new SimpleGrantedAuthority(existingAdminWithEmail.getRole()));
            return new User(existingAdminWithEmail.getEmail(), existingAdminWithEmail.getPassword(), roles);
        }
        else if (existingAdminWithPhone!= null && existingAdminWithPhone.getPhoneNumber()!=null){
            roles = List.of(new SimpleGrantedAuthority(existingAdminWithPhone.getRole()));
            return new User(existingAdminWithPhone.getPhoneNumber(), existingAdminWithPhone.getPassword(), roles);
        }
        return null;

    }


}
