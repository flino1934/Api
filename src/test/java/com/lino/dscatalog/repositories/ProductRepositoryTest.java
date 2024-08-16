package com.lino.dscatalog.repositories;

import com.lino.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest//Usado para testar a camada de controller
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test//Esta sendo realizado o teste do delete quando o ide existir
    public void deleteShouldDeleteObjectWhenIdExists() {

        //Arrange
        long existingId = 1L;

        //Act
        repository.deleteById(existingId);

        //Assertions
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());

    }

    @Test//Será testado quando o id não existir
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenidDoesNotExists(){

        long nonExistingID = 1000L;

        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            repository.deleteById(nonExistingID);
        });
    }
}
