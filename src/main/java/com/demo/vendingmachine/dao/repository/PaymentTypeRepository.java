package com.demo.vendingmachine.dao.repository;

import com.demo.vendingmachine.dao.model.PaymentType;
import org.springframework.data.repository.CrudRepository;

public interface PaymentTypeRepository extends CrudRepository<PaymentType, Integer> {
}
