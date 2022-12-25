
package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.AuctionEntity;
import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.entity.Product;
import com.bidderApp.bidz.entity.UserAndBid;
import com.bidderApp.bidz.exception.InvalidDataFoundException;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.model.BidDetails;
import com.bidderApp.bidz.model.BidRequest;
import com.bidderApp.bidz.model.dto.LandingPageDto;
import com.bidderApp.bidz.repository.AuctionRepository;
import com.bidderApp.bidz.repository.PersonRepository;
import com.bidderApp.bidz.repository.ProductRepository;

import com.bidderApp.bidz.repository.UserAndBidRepository;
import com.bidderApp.bidz.service.helper.AuctionCrudHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class AuctionCrudService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuctionCrudHelper helper;


    @Autowired
    private UserAndBidRepository userAndBidRepository;

    //function to create an auction
    public ResponseEntity<?> addAuction(AuctionEntity auction){
        int quantity = auction.getAuctionQuantity();
        Product product = productRepository.findById(auction.getProductId()).orElse(null);
        if(product == null){
            return new ResponseEntity<>("product not found",HttpStatus.CONFLICT);
        }
        if(!helper.checkQuantity(auction,quantity)){
            return new ResponseEntity<>("Not enough quantity",HttpStatus.CONFLICT);
        }
//<<<<<<< HEAD
//        auctionRepository.save(new AuctionEntity(
//                auction.getId(),"UPCOMING",auction.getAuctionQuantity(),
//                auction.getStartingBid(),0,0,null,
//                auction.getProductId(),auction.getAuctionStartDate()));
//=======
        long date = auction.getAuctionStartDate();
        long currentDate = System.currentTimeMillis();
        if(date<currentDate)
            return new ResponseEntity<>(
                    "Auction Start Date cannot be before current date", HttpStatus.BAD_REQUEST);
        auction.setCurrentBid(0);
        auction.setCurrentBidder("nil");
        product.setProductQuantity(product.getProductQuantity()- auction.getAuctionQuantity());
        productRepository.save(product);
        auctionRepository.save(auction);
//>>>>>>> 9c395ed8a7f1138e68b8e1fbe347c9097109ba69
        return new ResponseEntity<>("New Auction added", HttpStatus.OK);
    }

    //function to delete an auction
    public ResponseEntity<?> deleteAuction(String id){
        long date = System.currentTimeMillis();

        AuctionEntity currentAuction = auctionRepository.findById(id).orElse(null);
        if(currentAuction ==  null){
            return new ResponseEntity<>("Auction not found",HttpStatus.NOT_FOUND);
        }
        if (date>currentAuction.getAuctionStartDate())
            return new ResponseEntity<>("Ongoing auction cannot be deleted", HttpStatus.CONFLICT);

        auctionRepository.delete(currentAuction);
        return new ResponseEntity<>("Auction Deleted",HttpStatus.OK);
    }

    //function to get all auctions
    public ResponseEntity<?> getAuction(){
        if(auctionRepository.findAll().isEmpty()){
            throw new NoDataFoundException("No auctions found");
        }
        return new ResponseEntity<>(auctionRepository.findAll(),HttpStatus.OK) ;
    }

    public ResponseEntity<?> placeBid(String id,UserAndBid userAndBid, Principal principal){
        String email = principal.getName();
        PersonEntity currentUser = personRepository.findByEmail(email);
        String bidUserId = currentUser.getId();
        userAndBid.setUserId(bidUserId);
        userAndBid.setAuctionId(id);

        int bid = userAndBid.getBid();
        if(!personRepository.existsById(bidUserId) || !auctionRepository.existsById(id)){
            return new ResponseEntity<>("Invalid User id or Auction id",HttpStatus.BAD_REQUEST);
        }
        AuctionEntity bidAuction = auctionRepository.findById(id).orElse(null);

        if(bidAuction == null)
            return new ResponseEntity<>("Auction Not found", HttpStatus.NOT_FOUND);
        if(bid<bidAuction.getCurrentBid() || bid<bidAuction.getStartingBid()){
            return new ResponseEntity<>("Placed bid cannot be lower than current bid/starting bid",HttpStatus.BAD_REQUEST);
        }
        List<UserAndBid> previousBids = userAndBidRepository.findBidsByUserIdAndAuctionId(bidUserId,id);
        previousBids.forEach(previousBid->userAndBidRepository.delete(previousBid));
        userAndBidRepository.save(userAndBid);

        if(bidAuction.getCurrentBid()<bid){
            bidAuction.setCurrentBid(bid);
            bidAuction.setCurrentBidder(bidUserId);
        }
        auctionRepository.save(bidAuction);
        return new ResponseEntity<>("Bid Placed", HttpStatus.OK);
    }

    Comparator<UserAndBid> cmp = new Comparator<UserAndBid>() {
        @Override
        public int compare(UserAndBid o1, UserAndBid o2) {
            return Double.compare(o1.getBid(), o2.getBid());
        }
    };

    public ResponseEntity<?> getAuctionWinner(String auctionId){
        List<UserAndBid> auctionBids = userAndBidRepository.findBidsByAuctionId(auctionId);
        UserAndBid top = Collections.max(auctionBids,cmp);
        String highestBidder = top.getUserId();
        PersonEntity winner = personRepository.findById(highestBidder).orElse(null);
        if(winner == null)
            return new ResponseEntity<>("No winner", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>("Winner is:"+winner.getName(),HttpStatus.OK);
    }

    //function to get auctions in which a particular user participated
    public ResponseEntity<?> getAuctionByUser(String id){
        List<UserAndBid> userAndBidList = userAndBidRepository.findBidsByUserId(id);
        List<String> auctionIds = userAndBidList.stream().map(UserAndBid::getAuctionId).collect(Collectors.toList());
        Iterable<AuctionEntity> auction =  auctionRepository.findAllById(auctionIds);
        return new ResponseEntity<>(auction,HttpStatus.OK);
    }


    //function to get a single auction
    public ResponseEntity<?> getAuctionById(String id){
        AuctionEntity currentAuction = auctionRepository.findById(id).orElse(null);
        if (currentAuction ==  null){
            return new ResponseEntity<>("No auctions found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AuctionEntity>(currentAuction,HttpStatus.OK);
    }



    //function to get auctions by their status
    public ResponseEntity<?> getAuctionByStatus(String auctionStatus) {
        List<AuctionEntity> auctions = auctionRepository.findAll();
        List<AuctionEntity> liveAuctions = new ArrayList<>();
        List<AuctionEntity> upcomingAuctions = new ArrayList<>();
        List<AuctionEntity> completedAuctions = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        auctions.forEach(auction -> {
            if(auction.getAuctionStartDate() > currentTime )
                upcomingAuctions.add(auction);
            if(auction.getAuctionEndTime() <= currentTime )
                completedAuctions.add(auction);
            if(currentTime>auction.getAuctionStartDate() && currentTime<auction.getAuctionEndTime() )
                liveAuctions.add(auction);
        });
        if(auctionStatus.equals("LIVE")){
            auctions = liveAuctions;
        }

        if(auctionStatus.equals("UPCOMING")){
            auctions = upcomingAuctions;
        }

        if(auctionStatus.equals("COMPLETED")){
            auctions = completedAuctions;
        }
        if (auctions.isEmpty()){
            return new ResponseEntity<String>("No auctions found", HttpStatus.NOT_FOUND);
        }
        List<LandingPageDto> landingPageDTOs = new ArrayList<>();
        auctions.forEach(auction->{
            Product product = productRepository.findById(auction.getProductId()).get();
            LandingPageDto landingPageDto = new LandingPageDto();
            landingPageDto.setId(auction.getId());
            landingPageDto.setAuctionStartDate(auction.getAuctionStartDate());
            landingPageDto.setProductId(product.getId());
            landingPageDto.setProductName(product.getProductName());
            landingPageDto.setProductImage(product.getProductImage());
            landingPageDto.setProductMRP(product.getProductMRP());
            landingPageDto.setAuctionEndTime(auction.getAuctionEndTime());
            landingPageDto.setCurrentBid(auction.getCurrentBid());
            landingPageDto.setCurrentBidder(auction.getCurrentBidder());
            landingPageDto.setStartingBid(auction.getStartingBid());
            landingPageDto.setProductDescription(product.getProductDescription());
            landingPageDto.setSpecifications(product.getSpecifications());
            landingPageDTOs.add(landingPageDto);

        });
        return new ResponseEntity<>(landingPageDTOs, HttpStatus.OK);
    }

    public ResponseEntity<?> getLiveAuction(String id){

        AuctionEntity auction = auctionRepository.findById(id).orElse(null);
        if(auction == null){
            return new ResponseEntity<>("Auction not found", HttpStatus.NOT_FOUND);
        }
        String productId = auction.getProductId();
        Product product = productRepository.findById(productId).orElse(null);

        if(product == null){
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        LandingPageDto landingPageDto = new LandingPageDto(
                auction.getId(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndTime(),
                product.getId(),
                product.getProductName(),
                product.getProductImage(),
                product.getProductMRP(),
                auction.getCurrentBidder(),
                auction.getCurrentBid(),
                auction.getStartingBid(),
                product.getProductDescription(),
                product.getSpecifications());
        return new ResponseEntity<>(landingPageDto,HttpStatus.OK);
    }

    //socket function
    public BidDetails bidDetails(String room,BidRequest request){
        return new BidDetails("New Bidder: " + HtmlUtils.htmlEscape(request.getName())+
                " New Bid:"+ HtmlUtils.htmlEscape(request.getBid())+
                " AuctionId"+ HtmlUtils.htmlEscape(request.getAuctionId()));
    }
}


