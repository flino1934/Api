package com.lino.dscatalog.repositories;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTest {

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private long countTotalProducts;

    @Autowired
    private CategoryRepository repository;

    private Category category;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        countTotalProducts = 25L;
        category = CategoryFactory.createCategory();
    }

    @Test
    public void testFindAllPaged() {

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<Category> result = repository.findAll(pageable);

        //Assert
        Assertions.assertNotNull(result);

    }

    @Test
    public void testFindByIdWhenIdExistShouldReturnCategory() {

        //Arrange

        //Act
        Optional<Category> result = repository.findById(existingId);

        //Assert
        Assertions.assertNotNull(result.isPresent());
    }

    @Test
    public void testFindByIdWhenIdDoesNotExistShouldReturnResourceNotFoundExceptions() {

        //Act
        Optional<Category> result = repository.findById(nonExistingId);

        //Assert
        Assertions.assertTrue(result.isEmpty());


    }

    @Test
    public void testDeleteWhenIdExist() {

        repository.deleteById(existingId);

        Optional<Category> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void testDeleteWhenIdDoesNotExistShoulReturnThrowEmptyResultDataAccessException() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });

    }

}
