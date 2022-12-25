package com.bidderApp.bidz.controller;

import com.bidderApp.bidz.entity.ProductTransactionEntity;
import com.bidderApp.bidz.service.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ProductTransactionController {

    @Autowired
    ProductTransactionService productTransactionService;

    //controller to generate a transaction
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/transaction")
    public Object addTransaction(@RequestBody ProductTransactionEntity productTransaction){
        return productTransactionService.addTransaction(productTransaction);
    }

    //controller to get all transactions
    @GetMapping("/transaction")
    public Object getTransaction(){
        return productTransactionService.getTransaction();
    }

    //controller to get all transactions for a specific user
    @GetMapping("/transaction/{id}")
    public Object getTransactionById(@PathVariable(value = "id") String id){
        return productTransactionService.getTransactionsByUserId(id);
    }

    //controller to delete a particular transaction
    @DeleteMapping("/transaction/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER-ADMIN')")
    public Object deleteTransaction(@PathVariable(value = "id") String id){
        return productTransactionService.deleteTransaction(id);
    }

    //controller to generate all transactions in CSV format
    @GetMapping("/transaction/download")
    public Object getAllTransactions(HttpServletResponse servletResponse) throws IOException{
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"transactions.csv\"");
        return productTransactionService.downloadAsCSV(servletResponse.getWriter());
    }

}
