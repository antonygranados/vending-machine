package com.demo.vendingmachine.controller;

import com.demo.vendingmachine.dao.model.ReportProductsByMachine;
import com.demo.vendingmachine.dao.model.VendingMachine;
import com.demo.vendingmachine.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/vending")
public class VendingMachineController {
    private VendingMachineService vendingMachineService;

    @Autowired
    public VendingMachineController(VendingMachineService vendingMachineService) {
        this.vendingMachineService = vendingMachineService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Iterable<VendingMachine>> findAll(){
        Iterable<VendingMachine> vendingMachines = this.vendingMachineService.findAllVendingMachines();
        return new ResponseEntity<>(vendingMachines, HttpStatus.OK);
    }

    @GetMapping(value = "/products")
    public ResponseEntity<Iterable<ReportProductsByMachine>> findVendingProducts(){
        Iterable<ReportProductsByMachine> reportProductsByMachine = this.vendingMachineService.findProducts();
        return new ResponseEntity<>(reportProductsByMachine, HttpStatus.OK);
    }

}
