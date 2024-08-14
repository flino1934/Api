package com.lino.dscatalog.services;

import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
}
