package com.demo.vendingmachine.controller;

import com.demo.vendingmachine.dao.model.*;
import com.demo.vendingmachine.service.TransactionService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping(path="/transaction")
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private TransactionService transactionService;
    private Gson gson;

    @Autowired
    public TransactionController(TransactionService transactionService, Gson gson) {
        this.transactionService = transactionService;
        this.gson = gson;
    }

    @GetMapping(value = "/all")
    public @ResponseBody ResponseEntity<Iterable<Transaction>> findAll(){
        Iterable<Transaction> transactions = this.transactionService.findAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);

    }

    @GetMapping(value = "/total-sales")
    public @ResponseBody ResponseEntity<String> findTotalSales() {
        Double totalSales = this.transactionService.findTotalSales();
        String totalSalesResponse = "{\"msg\": \"$" + totalSales + "\"}";
        return new ResponseEntity<>(totalSalesResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/total-sales/{id}")
    public @ResponseBody ResponseEntity<String> findTotalSalesByVendingMachine(@PathVariable Integer id) {
        Optional<ReportTotalSalesByMachine> totalSales = this.transactionService.findTotalSalesByVendingMachine(id);

        if(totalSales.isPresent()) {
            return new ResponseEntity<>(this.gson.toJson(totalSales.get(), ReportTotalSalesByMachine.class), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = "/from", consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<Iterable<ReportTransaction>> findTransactionsFromDate(@RequestBody RequestTransaction requestTransaction){

        Iterable<ReportTransaction> reportTransactions = this.transactionService.findReportTransactionsFromDate(requestTransaction.getDateFrom());
        return new ResponseEntity<>(reportTransactions, HttpStatus.OK);

    }

    @GetMapping(value = "/from/today")
    public @ResponseBody ResponseEntity<Iterable<ReportTransaction>> findTransactionsFromToday(){
        Iterable<ReportTransaction> reportTransactions = this.transactionService.findReportTransactionsFromDate(LocalDateTime.now().with(LocalTime.of(0, 0, 0)));
        return new ResponseEntity<>(reportTransactions, HttpStatus.OK);
    }

    @PostMapping(value = "/product-sell", consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<String> sellProduct(@RequestBody RequestSell requestSell){

        Optional<ReportTransaction> reportTransaction = this.transactionService.sellProduct(requestSell);

        if(reportTransaction.isPresent()){
            return new ResponseEntity<>(this.gson.toJson(reportTransaction.get(), ReportTransaction.class), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\": \"No se pudo completar la compra o el producto no esta disponible en esta maquina\"}", HttpStatus.OK);
        }

    }

}
