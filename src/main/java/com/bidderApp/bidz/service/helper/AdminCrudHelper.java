package com.bidderApp.bidz.service.helper;

import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.model.dto.AdminDto;
import com.bidderApp.bidz.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCrudHelper {

    @Autowired
    private PersonRepository adminRepository;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    ModelMapper modelMapper = new ModelMapper();

    public Boolean checkIfSuperAdmin(){
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_SUPER-ADMIN"));
    }

    public Boolean checkUsername(String username){
        return adminRepository.findByUsername(username) == null;
    }
    public Boolean checkEmail(String email){
        return adminRepository.findByEmail(email) == null;
    }

    public List<PersonEntity> returnAdmins(List<PersonEntity> admins){
        return admins.stream().filter(f -> "ROLE_ADMIN".equals(f.getRole())).collect(Collectors.toList());
    }

    public AdminDto getAdminDtoConverter(PersonEntity person) {
        return modelMapper.map(person, AdminDto.class);
    }

}
