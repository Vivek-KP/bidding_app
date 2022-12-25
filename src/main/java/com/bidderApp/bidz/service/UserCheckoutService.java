package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.AuctionEntity;
import com.bidderApp.bidz.entity.CheckoutEntity;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.entity.Promocode;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.model.dto.PromoCodeDto;
import com.bidderApp.bidz.repository.AuctionRepository;
import com.bidderApp.bidz.repository.CheckoutRepository;
import com.bidderApp.bidz.repository.ProductRepository;
import com.bidderApp.bidz.repository.Promorepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserCheckoutService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private Promorepository promorepository;

    @NotNull
    public Object checkoutFunction(String auctionId) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionId).orElse(null);
        Product product = productRepository.findById(auctionEntity.getProductId()).orElse(null);
        if(Objects.nonNull(auctionEntity) && Objects.nonNull(product)) {
            int quantity = auctionEntity.getAuctionQuantity();
            double totalAmount = product.getProductMRP();
            double subtotal = totalAmount * auctionEntity.getAuctionQuantity();
            String image = product.getProductImage().getImage();
            return checkoutRepository.save(new CheckoutEntity(null, product.getProductName(), image, totalAmount, subtotal, quantity));
        }else{
            return new NoDataFoundException("Auction or product could not be found");
        }
    }

    @NotNull
    public Object promoCodeApply(String checkoutId, PromoCodeDto promoCodeDto) {
        CheckoutEntity checkout = checkoutRepository.findById(checkoutId).orElse(null);
        Promocode promocode = promorepository.findByPromoCode(promoCodeDto.getPromoCode());
        if(Objects.isNull(checkout)){
            return new NoDataFoundException("Invalid checkout");
        } else if (Objects.isNull(promocode)) {
            return new NoDataFoundException("Invalid promo-code");
        }else {
            double discount = Double.parseDouble(promocode.getDiscount());
            double totalAmount = (checkout.getTotal() * (1 - (discount / 100)));
            double subTotal = totalAmount * checkout.getQuantity();
            checkout.setTotal(totalAmount);
            checkout.setSubtotal(subTotal);
            return checkoutRepository.save(checkout);
        }
    }

    @NotNull
    public Promocode promoAddFunction(Promocode promocode) {
        return promorepository.save
                (new Promocode(promocode.getId(), promocode.getPromoCode(), promocode.getDiscount()));
    }


}
