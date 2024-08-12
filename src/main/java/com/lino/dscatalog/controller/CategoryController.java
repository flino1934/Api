package com.lino.dscatalog.controller;

import com.lino.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<Category> findAll() {

        Category category = new Category();
        category.setName("TV");
        category.setId(1L);

        return ResponseEntity.ok().body(category);

    }
}
