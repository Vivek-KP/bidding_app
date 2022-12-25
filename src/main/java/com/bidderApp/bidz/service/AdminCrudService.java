
package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.entity.UserDisable;
import com.bidderApp.bidz.exception.DataAlreadyExistException;

import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.model.dto.AdminDto;
import com.bidderApp.bidz.model.dto.ChangePasswordDto;

import com.bidderApp.bidz.repository.BannedUserRepository;
import com.bidderApp.bidz.repository.PersonRepository;

import com.bidderApp.bidz.service.helper.AdminCrudHelper;
import com.bidderApp.bidz.service.helper.SlugConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

import java.util.Objects;

import java.util.stream.Collectors;

@Service
public class AdminCrudService {
    @Autowired
    private PersonRepository adminRepository;
    @Autowired
    private BannedUserRepository bannedUserRepository;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    AdminCrudHelper helper;

    //function to add a new admin
    public ResponseEntity<?> addAdmin(PersonEntity admin){
        String username = admin.getUsername();
        String email = admin.getEmail();
        admin.setRole("ROLE_ADMIN");
        admin.setSlug(SlugConverter.toSlug(admin.getName()));
        if(!(helper.checkEmail(email) && helper.checkUsername(username))){
            return new ResponseEntity<>("Admin already exists", HttpStatus.CONFLICT);
        }
        adminRepository.save(admin);
        return new ResponseEntity<>("New Admin added",HttpStatus.CREATED);
    }

    //get a specific admin by id
    public ResponseEntity<?> getAdminById(String id, Principal principal){
        PersonEntity repoAdmin = adminRepository.findById(id).orElse(null);
        if(repoAdmin == null || Objects.equals(repoAdmin.getRole(), "ROLE_USER")){
            return new ResponseEntity<>("Profile not found",HttpStatus.NOT_FOUND);
        }
        if(!repoAdmin.getEmail().equals(principal.getName()) && !helper.checkIfSuperAdmin() )
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(modelMapper.map(repoAdmin, AdminDto.class),HttpStatus.OK);
    }

    //update an admin profile
    public ResponseEntity<?> updateAdmin(String id, AdminDto admin,Principal principal){
        PersonEntity repoAdmin = adminRepository.findById(id).orElse(null);
        String currentEmail = admin.getEmail();
        String currentUsername = admin.getUsername();
        if(repoAdmin!=null ){
            if(!repoAdmin.getEmail().equals(principal.getName()) && !helper.checkIfSuperAdmin())
                return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
            String repoEmail = repoAdmin.getEmail();
            String repoUsername = repoAdmin.getUsername();
            if((helper.checkUsername(currentUsername) || repoUsername.equals(currentUsername)) &&
                    (helper.checkEmail(currentEmail) || repoEmail.equals(currentEmail)))
            {
                repoAdmin.setUsername(admin.getUsername());
                repoAdmin.setEmail(admin.getEmail());
                repoAdmin.setName(admin.getName());
                repoAdmin.setRole("ROLE_ADMIN");
                repoAdmin.setSlug(SlugConverter.toSlug(repoAdmin.getName()));
                adminRepository.save(repoAdmin);
                return new ResponseEntity<>("Profile updated",HttpStatus.OK);
            }
            else throw new DataAlreadyExistException("Email or username exists");
        }
        else throw new NoDataFoundException("Admin not found");
    }

    //delete an admin
    public ResponseEntity<?> deleteAdmin(String id,Principal principal){
        PersonEntity repoAdmin = adminRepository.findById(id).orElse(null);
        if(repoAdmin == null){
            return new ResponseEntity<>("admin not found",HttpStatus.NOT_FOUND);
        }
        if(!repoAdmin.getEmail().equals(principal.getName()) && !helper.checkIfSuperAdmin())
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        adminRepository.delete(repoAdmin);
        return new ResponseEntity<>("Admin Deleted",HttpStatus.FOUND);
    }

    //get all admins
    public ResponseEntity<?> getAdmins(){
        List<PersonEntity> admins = helper.returnAdmins(adminRepository.findAll());
        admins = admins.stream().filter(f -> "ROLE_ADMIN".equals(f.getRole())).collect(Collectors.toList());
        return new ResponseEntity<>(admins.stream().map(helper::getAdminDtoConverter)
                .collect(Collectors.toList()),HttpStatus.OK) ;
        }
    //change password of admin
    public ResponseEntity<?> changePassword(String id, ChangePasswordDto passwords, Principal principal) {
        PersonEntity repoAdmin = adminRepository.findById(id).orElse(null);
        if(repoAdmin == null)
            return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
        if(!repoAdmin.getEmail().equals(principal.getName()))
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        if(!repoAdmin.getPassword().equals(passwords.getOldPassword())){
            return new ResponseEntity<>("Incorrect Old Password",HttpStatus.CONFLICT);
        }
        repoAdmin.setPassword(passwords.getNewPassword());
        adminRepository.save(repoAdmin);
        return new ResponseEntity<>("Password successfully changed",HttpStatus.OK);
    }

    @NotNull
    public ResponseEntity<?> disabling(String id) {
        PersonEntity disableUser = adminRepository.findById(id).orElse(null);
        if(disableUser == null)
            return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
        if ( disableUser.getRole().equals("ROLE_SUPER-ADMIN")){
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }else {
            UserDisable userDisable = bannedUserRepository.findByBannedUserId(id);

            return new ResponseEntity<>(enableOrDisableFunction(id, userDisable),HttpStatus.OK);
        }
    }

    @NotNull
    private String enableOrDisableFunction(String id, UserDisable userDisable) {
        if(Objects.isNull(userDisable)) {
            bannedUserRepository.save(new UserDisable(null, id));
            return "user has been disabled";
        }else{
            String disabledUserId = userDisable.getId();
            bannedUserRepository.deleteById(disabledUserId);
            return "user has been enabled";
        }
    }

}
