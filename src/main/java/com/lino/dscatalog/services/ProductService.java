package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.repositories.CategoryRepository;
import com.lino.dscatalog.repositories.ProductRepository;
import com.lino.dscatalog.services.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
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

    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);

        return new ProductDTO(entity);

    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try {

            Product entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            return new ProductDTO(entity);

        } catch (EntityNotFoundException e) {

            throw new ResourceNotFoundExceptions("Id Not found");

        }
    }
    @Transactional
    public void delete(Long id) {

        try{

            repository.deleteById(id);

        }catch (
    EmptyResultDataAccessException e) {

        throw new ResourceNotFoundExceptions("Id not found!!!");

    } catch (
    DataIntegrityViolationException e) {

        throw new DataBaseExceptions("Database violation");

    }

    }


    private void copyDtoToEntity(ProductDTO dto, Product entity) {

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(Instant.now());

        entity.getCategories().clear();//Limpando as categorias que possam esta armazenadas na entidade

        for (CategoryDTO categoryDTO : dto.getCategories()) {//Esta percorrendo todas a categorias de CategoryDto

            Category category = categoryRepository.getOne(categoryDTO.getId());//Vai carregar a categoria especifica
            entity.getCategories().add(category);

        }

    }


}
