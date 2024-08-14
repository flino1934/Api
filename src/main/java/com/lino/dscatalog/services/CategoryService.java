package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.repositories.CategoryRepository;
import com.lino.dscatalog.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public Page<CategoryDTO> findAllPaged(Pageable pageable) {

        Page<Category> list = repository.findAll(pageable);
        return list.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {

        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundExceptions("Category" + id + " not found "));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        CategoryDTO categoryDTO = new CategoryDTO(entity);

        return categoryDTO;

    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {

        try {

            Category entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            CategoryDTO categoryDTO = new CategoryDTO(entity);

            return categoryDTO;

        } catch (EntityNotFoundException e) {

            throw new ResourceNotFoundExceptions("Id not found " + id);

        }
    }

    public void delete(Long id) {
        try {

            repository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {

            throw new ResourceNotFoundExceptions("Id not found!!!");

        } catch (DataIntegrityViolationException e) {

            throw new DataBaseExceptions("Database violation");

        }

    }
}
