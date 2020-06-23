package com.demo.vendingmachine.service;

import com.demo.vendingmachine.dao.model.Product;
import com.demo.vendingmachine.dao.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {


    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> findAllProducts(){
        return this.productRepository.findAll();
    }
}
