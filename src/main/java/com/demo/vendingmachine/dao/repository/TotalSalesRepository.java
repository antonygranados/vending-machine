package com.demo.vendingmachine.dao.repository;

import com.demo.vendingmachine.dao.model.ReportTotalSalesByMachine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TotalSalesRepository extends CrudRepository<ReportTotalSalesByMachine, Integer> {

    @Query(value = "SELECT vending_machine_id, vm.model, SUM(total) AS total_sales FROM transaction\n" +
            "INNER JOIN vending_machine vm\n" +
            "ON vm.id = vending_machine_id\n" +
            "WHERE vending_machine_id = :vendingMachineId", nativeQuery = true)
    Optional<ReportTotalSalesByMachine> findTotalSalesByVendingMachine(@Param("vendingMachineId") Integer vendingMachineId);
}
