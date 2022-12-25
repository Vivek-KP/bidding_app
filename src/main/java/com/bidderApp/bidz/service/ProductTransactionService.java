
package com.bidderApp.bidz.service;

import com.bidderApp.bidz.entity.ProductTransactionEntity;
import com.bidderApp.bidz.exception.NoDataFoundException;
import com.bidderApp.bidz.repository.ProductTransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTransactionService {

    @Autowired
    ProductTransactionRepository productTransactionRepository;

    //function to create a new transaction
    public Object addTransaction(ProductTransactionEntity productTransaction) {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        productTransactionRepository.save(new ProductTransactionEntity(productTransaction.getId(),
                productTransaction.getCredits(),date,
                productTransaction.getIsAuction(),productTransaction.getUserId(),
                productTransaction.getAuctionId(), productTransaction.getProductId()));
        return new ResponseEntity<>("Transaction Completed", HttpStatus.OK);
    }

    //function to get all transactions
    public Object getTransaction() {
        List<ProductTransactionEntity> transactions = productTransactionRepository.findAll();
        if (transactions.isEmpty()){
            throw new NoDataFoundException("No transactions found");
        }
        return transactions;
    }

    //function to delete a transaction
    public Object deleteTransaction(String id) {
        ProductTransactionEntity transaction = productTransactionRepository.findById(id).orElse(null);
        if(transaction == null) throw new NoDataFoundException("No transaction found");
        productTransactionRepository.delete(transaction);
        return new ResponseEntity<>("Transaction deleted",HttpStatus.OK);
    }

    //get transactions of a particular user
    public Object getTransactionsByUserId(String id) {
        List<ProductTransactionEntity> transactions = productTransactionRepository.findByUserId(id);
        if(transactions == null) throw new NoDataFoundException("No transactions found");
        return transactions;
    }

    //function to get total revenue
    public Object getTotalRevenue(){
        List<ProductTransactionEntity> transactions = productTransactionRepository.findAll();
        double revenue = transactions.stream().mapToDouble(ProductTransactionEntity::getCredits).sum();
        if (Double.isNaN(revenue)) revenue = 0.0;
        return revenue;
    }

    //function to download total transactions as CSV
    public Object downloadAsCSV(Writer writer) {
        List<ProductTransactionEntity> transactions =productTransactionRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (ProductTransactionEntity transaction : transactions) {
                csvPrinter.printRecord(transaction.getId(), transaction.getTransactionDate(), transaction.getUserId(), transaction.getAuctionId(), transaction.getCredits());
            }
            return csvPrinter;
        } catch (IOException e) {
            return new ResponseEntity<>("Error generating CSV",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
