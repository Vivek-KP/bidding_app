package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.Promocode;
import com.bidderApp.bidz.model.dto.PromoCodeDto;
import com.bidderApp.bidz.service.UserCheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCheckoutController {

    @Autowired
    private UserCheckoutService userCheckoutService;



    //api for checkout
    @PostMapping("/checkout/{auctionId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Object checkout(@PathVariable String auctionId){
        return userCheckoutService.checkoutFunction(auctionId);
    }


    //api for checkout after applying promo code
    @PostMapping("/checkout/promo/{checkoutId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Object promoCodeAtCheckout(@PathVariable String checkoutId, @RequestBody PromoCodeDto promoCodeDto){
        return userCheckoutService.promoCodeApply(checkoutId, promoCodeDto);

    }

    //api for adding promo code
    @PostMapping("/promo/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object addPromo(@RequestBody Promocode promocode){
        return userCheckoutService.promoAddFunction(promocode);
    }


}
