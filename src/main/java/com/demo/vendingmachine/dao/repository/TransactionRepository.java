package com.demo.vendingmachine.dao.repository;

import com.demo.vendingmachine.dao.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM transaction\n" +
            "WHERE transaction_date > :dateFrom", nativeQuery = true)
    Iterable<Transaction> findTransactionsFromDate(@Param("dateFrom") LocalDateTime dateFrom);
}
