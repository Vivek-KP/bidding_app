
package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.AuctionEntity;
import com.bidderApp.bidz.entity.UserAndBid;
import com.bidderApp.bidz.model.BidRequest;
import com.bidderApp.bidz.service.AuctionCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping
public class AuctionCrudController {

    @Autowired
    AuctionCrudService auctionCrudService;

    //controller to add a new auction
    @PostMapping("/auction")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> addAuction(@Valid @RequestBody AuctionEntity auction){
        return auctionCrudService.addAuction(auction);
    }

    //controller to get all auctions
    @GetMapping("/auction")
    public ResponseEntity<?> getAuction(){
        return auctionCrudService.getAuction();
    }

    //controller to delete an auction
    @DeleteMapping("/auction/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER-ADMIN')")
    public ResponseEntity<?> deleteAuction(@PathVariable(value = "id") String id){
        return auctionCrudService.deleteAuction(id);
    }

//    controller to get live,upcoming and completed auctions
    @GetMapping("/home/{auctionStatus}")
    public ResponseEntity<?> getAuctionsByStatus(@PathVariable(value = "auctionStatus")String auctionStatus){
        return auctionCrudService.getAuctionByStatus(auctionStatus);
    }

    @GetMapping("/user/auction/{id}")
    public ResponseEntity<?> getLiveAuction(@PathVariable(value = "id") String id){
        return auctionCrudService.getLiveAuction(id);
    }

    //controller to get a specific auction
    @GetMapping("/auction/{id}")
    public ResponseEntity<?> getAuctionById(@PathVariable(value = "id")String id){
        return auctionCrudService.getAuctionById(id);
    }


    //controller to get auctions in which a user participated
    @GetMapping("/user/{id}/auction")
    public ResponseEntity<?> getAuctionByUser(@PathVariable(value = "id") String id){
        return auctionCrudService.getAuctionByUser(id);
    }

    @PostMapping("/auction/{id}/bid")
    public ResponseEntity<?> placeBid(@PathVariable(value = "id")String id,@RequestBody UserAndBid userAndBid, Principal principal){
        return auctionCrudService.placeBid(id,userAndBid,principal);
    }

    @GetMapping("/auction/{id}/winner")
    public ResponseEntity<?> getAuctionWinner(@PathVariable(value = "id") String id){
        return auctionCrudService.getAuctionWinner(id);
    }


    @MessageMapping("/bid/{room}")
    @SendTo("/topic/greetings")
    public ResponseEntity<?> bidDetails(@DestinationVariable(value = "room") String room, BidRequest request) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new ResponseEntity<>(auctionCrudService.bidDetails(room,request), HttpStatus.OK) ;
    }


}

