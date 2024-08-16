package com.lino.dscatalog.repositories;

import com.lino.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Optional;

@DataJpaTest//Usado para testar a camada de controller
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test//Esta sendo realizado o teste do delete quando o ide existir
    public void deleteShoulddeleteObjectWhenIdExists() {

        //Arrange
        long existingId = 1L;

        //Act
        repository.deleteById(existingId);

        //Assertions
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());

    }
}
