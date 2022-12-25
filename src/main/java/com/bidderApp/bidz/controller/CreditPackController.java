package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.CreditPackEntity;
import com.bidderApp.bidz.service.CreditPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CreditPackController {

    @Autowired
    CreditPackService creditPackService;

    //controller to add credit pack
    @PostMapping("/creditPack")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> addCreditPack(@RequestBody CreditPackEntity creditPack){
        return creditPackService.addCreditPack(creditPack);
    }

    //controller to view all credit packs
    @GetMapping("/creditPack")
    public ResponseEntity<?> getCreditPacks(){
        return creditPackService.getCreditPacks();
    }

    //controller to delete a credit pack
    @DeleteMapping("/creditPack/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> deleteCreditPack(@PathVariable(value = "id")String id){
        return creditPackService.deleteCreditPack(id);
    }

    @GetMapping("/creditPack/buy/{id}")
    public ResponseEntity<?> buyCreditPack(@PathVariable(value = "id") String id, Principal principal){
        return creditPackService.buyCreditPack(id,principal);
    }
}
