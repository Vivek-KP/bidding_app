package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.CreditPackEntity;
import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.repository.PersonRepository;
import com.bidderApp.bidz.repository.CreditPackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CreditPackService {

    @Autowired
    CreditPackRepository creditPackRepository;

    @Autowired
    PersonRepository userRepository;

    //function to add a new credit pack
    public ResponseEntity<?> addCreditPack(CreditPackEntity creditPack) {
        creditPackRepository.save(new CreditPackEntity(creditPack.getId(),
                creditPack.getCreditValue(), creditPack.getCreditCost(),
                creditPack.getPackDiscount(),creditPack.getAdminId()));
        return new ResponseEntity<>("New Credit Pack added", HttpStatus.OK);
    }

    //function to get all credit packs
    public ResponseEntity<?> getCreditPacks() {
        List<CreditPackEntity> creditPack = creditPackRepository.findAll();
        if(creditPack.isEmpty()) throw new NoDataFoundException("No credit packs found");
        return new ResponseEntity<>(creditPack,HttpStatus.OK) ;
    }

    //function to delete a particular credit pack
    public ResponseEntity<?> deleteCreditPack(String id) {
        CreditPackEntity creditPack = creditPackRepository.findById(id).orElse(null);
        if(creditPack == null)
            return new ResponseEntity<>("Credit pack not found",HttpStatus.NOT_FOUND);
        creditPackRepository.delete(creditPack);
        return new ResponseEntity<>("Credit pack deleted", HttpStatus.OK);
    }

    //function for user to buy a new credit pack(payment not implemented)
    public ResponseEntity<?> buyCreditPack(String id, Principal principal){
        CreditPackEntity creditPack = creditPackRepository.findById(id).orElse(null);
        if (creditPack ==  null) return new ResponseEntity<>("Credit pack not found",HttpStatus.NOT_FOUND);
        String email = principal.getName();
        PersonEntity user = userRepository.findByEmail(email);
        int newBalance = user.getCreditBalance() + creditPack.getCreditValue();
        user.setCreditBalance(newBalance);
        userRepository.save(user);
        return new ResponseEntity<>("New Balance:"+newBalance,HttpStatus.OK);
    }
}
