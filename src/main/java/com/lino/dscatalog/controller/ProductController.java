package com.lino.dscatalog.controller;

import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

}
