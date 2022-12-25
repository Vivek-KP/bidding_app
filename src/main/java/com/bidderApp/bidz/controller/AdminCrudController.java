
package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.JwtToken;
import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.model.dto.AdminDto;
import com.bidderApp.bidz.model.dto.ChangePasswordDto;
import com.bidderApp.bidz.service.AdminCrudService;
import com.bidderApp.bidz.service.BidderUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class AdminCrudController {
    @Autowired
    private AdminCrudService adminCrudService;

    @Autowired
    private BidderUserService bidderUserService;

//    controller to add new admins
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody PersonEntity admin){
        return adminCrudService.addAdmin(admin);
    }

//    controller to get admin details
    @GetMapping("/admin/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> getAdminById(@PathVariable(value = "id") String id, Principal principal){
        return adminCrudService.getAdminById(id,principal);
    }

//    controller to update admin profile
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> updateAdmin(@PathVariable(value = "id") String id,
                              @RequestBody AdminDto admin, Principal principal){
        return adminCrudService.updateAdmin(id,admin,principal);
    }

//    controller to delete a particular admin
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER-ADMIN','ROLE_ADMIN')")
    public ResponseEntity<?> deleteAdmin(@PathVariable(value = "id") String id, Principal principal){
        return adminCrudService.deleteAdmin(id,principal);
    }

//    controller to get all admins
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> getAdmins(){
        return adminCrudService.getAdmins();
    }

//    controller to change admin password
    @PatchMapping("/admin/{id}/password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable(value = "id") String id,
                                 @RequestBody ChangePasswordDto passwords, Principal principal){
        return adminCrudService.changePassword(id,passwords,principal);
    }

    @PostMapping("/admin/logout")
    public ResponseEntity<?> logout(JwtToken token){
        return bidderUserService.logout(token);
    }
    @PostMapping("/admin/disable/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER-ADMIN','ROLE_ADMIN')")
    public Object userDisable( @PathVariable String id){
        return adminCrudService.disabling(id);
    }

    //api for listing all the user
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object listAllUser(){
        return bidderUserService.userList();
    }


}









