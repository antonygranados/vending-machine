package com.demo.vendingmachine.controller;

import com.demo.vendingmachine.dao.model.Product;
import com.demo.vendingmachine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/product")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Iterable<Product>> findAll(){
        Iterable<Product> products = this.productService.findAllProducts();
        return new ResponseEntity<Iterable<Product>>(products, HttpStatus.OK);
    }
}
