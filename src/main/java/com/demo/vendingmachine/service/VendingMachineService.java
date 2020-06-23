package com.demo.vendingmachine.service;

import com.demo.vendingmachine.dao.model.Product;
import com.demo.vendingmachine.dao.model.ReportProductsByMachine;
import com.demo.vendingmachine.dao.model.VendingMachine;
import com.demo.vendingmachine.dao.repository.VendingMachineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class VendingMachineService {
    private static final Logger LOG = LoggerFactory.getLogger(VendingMachineService.class);

    private VendingMachineRepository vendingMachineRepository;

    @Autowired
    public VendingMachineService(VendingMachineRepository vendingMachineRepository) {
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public Iterable<VendingMachine> findAllVendingMachines(){
        return this.vendingMachineRepository.findAll();
    }

    public Iterable<Product> getProductsByVendingMachineLocation(String location){
        Optional<VendingMachine> vendingMachine = this.vendingMachineRepository.findByLocation(location);

        if(vendingMachine.isPresent()){
            return vendingMachine.get().getProducts();
        }

        return Collections.EMPTY_LIST;
    }

    public Iterable<ReportProductsByMachine> findProducts(){
        Iterable<VendingMachine> vendingMachines = this.vendingMachineRepository.findAll();
        ArrayList<ReportProductsByMachine> reportProductsByMachines = new ArrayList<>();

        for(VendingMachine vendingMachine : vendingMachines){
            vendingMachine.getProducts().iterator().forEachRemaining(product -> {
                reportProductsByMachines.add(
                        ReportProductsByMachine.builder()
                        .withVendingMachineId(vendingMachine.getId())
                        .withModel(vendingMachine.getModel())
                        .withLocation(vendingMachine.getLocation())
                        .withProductCode(product.getCode())
                        .withProductName(product.getName())
                        .withProductPrice(product.getPrice())
                        .build()
                );
            });
        }

        return reportProductsByMachines;
    }

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void updateDailyOpenAttempts(){
        Iterable<VendingMachine> vendingMachines = this.vendingMachineRepository.findAll();

        for(VendingMachine vendingMachine : vendingMachines){
            vendingMachine.setOpenAttempts(0);
            saveVendingMachine(vendingMachine);
        }
    }

    public void saveVendingMachine(VendingMachine vendingMachine){
        this.vendingMachineRepository.save(vendingMachine);
    }
}