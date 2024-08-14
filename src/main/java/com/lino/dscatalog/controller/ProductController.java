package com.lino.dscatalog.controller;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {

        Page<ProductDTO> list = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){

        ProductDTO productDTO = service.findById(id);
        return ResponseEntity.ok().body(productDTO);

    }

}
