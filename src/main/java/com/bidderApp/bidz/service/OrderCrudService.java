
package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.OrderEntity;
import com.bidderApp.bidz.entity.PersonEntity;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.repository.AuctionRepository;
import com.bidderApp.bidz.repository.OrderRepository;
import com.bidderApp.bidz.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderCrudService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PersonRepository personRepository;

    //function to create an order
    public ResponseEntity<?> addOrder(OrderEntity order){
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        orderRepository.save(new OrderEntity(order.getId(),
                "Accepted",date,order.getIsAuction(),
                order.getUserId(),order.getAuctionId(),order.getProductId()));
        return new ResponseEntity<>("New order added", HttpStatus.OK);
    }

    //function to delete an order
    public ResponseEntity<?> deleteOrder(String id){
        OrderEntity currentOrder = orderRepository.findById(id).orElse(null);
        if(currentOrder == null){
            return new ResponseEntity<>("Order not found",HttpStatus.NOT_FOUND);
        }
        orderRepository.delete(currentOrder);
        return new ResponseEntity<>("Order Deleted",HttpStatus.OK);
    }

    //function to get all orders
    public ResponseEntity<?> getOrder(){
        if (orderRepository.findAll().isEmpty()){
            throw new NoDataFoundException("No orders found");
        }
        return new ResponseEntity<>(orderRepository.findAll(),HttpStatus.OK) ;
    }

    //function to get orders made by a particular user
    public ResponseEntity<?> getOrderByUserId(String id){
        PersonEntity user = personRepository.findById(id).orElse(null);
        if(user == null)
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        List<OrderEntity> orders = orderRepository.findByUserId(id);
        if(orders.isEmpty()){
            return new ResponseEntity<>("No orders found for the user",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
}

