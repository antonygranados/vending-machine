package com.demo.vendingmachine.dao.repository;

import com.demo.vendingmachine.dao.model.VendingMachine;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VendingMachineRepository extends CrudRepository<VendingMachine, Integer> {

    Optional<VendingMachine> findByLocation(String location);
}
