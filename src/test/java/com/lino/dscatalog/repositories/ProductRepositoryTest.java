package com.lino.dscatalog.repositories;

import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest//Usado para testar a camada de controller
public class ProductRepositoryTest {

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    private Product product;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        product = ProductFactory.createProduct();
    }

    @Autowired
    private ProductRepository repository;

    @Test//Esta sendo realizado o teste do delete quando o ide existir
    public void deleteShouldDeleteObjectWhenIdExists() {

        //Arrange -> vira do setUp()

        //Act
        repository.deleteById(existingId);

        //Assertions
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());

    }

    @Test//Será testado quando o id não existir
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenidDoesNotExists() {

        //Arrange -> vira do setUp()

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test//Testando o save com id null fazendo auto increment
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        //Arrange -> vira do setUp() que é o product
        product.setId(null);

        //Act
        product = repository.save(product);

        //Assert
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts +1,product.getId());

    }

    @Test//Vai verificar se há algum retorno quando o id existir
    public void findByIdReturnProductWhenidExisting(){

        //Arrange vira do beforeach que vai ser o numero do id

        //Act
        Optional<Product> obj = repository.findById(existingId);

        //Assertion
        Assertions.assertNotNull(obj.isPresent());

    }

    @Test//Vai verificar se retorna a exception correta quando o id não existir
    public void nonExistingIdShouldReturnExceptionResourceNotFoundExceptions() {

        //Arrange -> vira do @BeforEach

        //Act
        Optional<Product> obj = repository.findById(nonExistingId);

        //Assert
        Assertions.assertTrue(obj.isEmpty());


    }

}
