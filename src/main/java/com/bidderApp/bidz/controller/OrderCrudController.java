
package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.AuctionEntity;
import com.bidderApp.bidz.entity.OrderEntity;
import com.bidderApp.bidz.service.OrderCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderCrudController {
    @Autowired
    OrderCrudService orderCrudService;

    //controller to generate an order
    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderEntity order) {
        return orderCrudService.addOrder(order);
    }

    //controller to get all orders
    @GetMapping("/order")
    public ResponseEntity<?> getOrder() {
        return orderCrudService.getOrder();
    }

    //controller to remove an order
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(value = "id") String id) {
        return orderCrudService.deleteOrder(id);
    }

    //controller to get a specific order
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable(value = "id") String id) {
        return orderCrudService.getOrderByUserId(id);
    }
}
