package com.lino.dscatalog.controller.services;

import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {

        List<Category> cat = categoryRepository.findAll();

        return cat;

    }

}
