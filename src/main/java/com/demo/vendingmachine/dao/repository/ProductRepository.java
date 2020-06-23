package com.demo.vendingmachine.dao.repository;

import com.demo.vendingmachine.dao.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
