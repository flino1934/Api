package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.repositories.CategoryRepository;
import com.lino.dscatalog.repositories.ProductRepository;
import com.lino.dscatalog.services.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {

        Page<Product> list = repository.findAll(pageable);
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {

        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundExceptions("Category" + id + " not found "));

        return new ProductDTO(entity, entity.getCategories());
    }
}
