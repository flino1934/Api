package com.lino.dscatalog.repositories;

import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ClientFactory;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTest {

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private long countTotalProducts;

    @Autowired
    private ClientRepository repository;

    private Client client;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        countTotalProducts = 25L;
        client = ClientFactory.createClient();
    }

    @Test
    public void testFindAllPaged() {

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<Client> result = repository.findAll(pageable);

        //Assert
        Assertions.assertNotNull(result);

    }

    @Test
    public void testFindByIDShouldReturnClientWhenIdExist() {

        //Arrange

        //Act
        Optional<Client> result = repository.findById(existingId);

        //Assertion
        Assertions.assertTrue(result.isPresent());

    }

    @Test
    public void testFindByIdWhenIdDoesNothingExist() {

        Optional<Client> result = repository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void testDeleteWhenIdExist() {

        repository.deleteById(existingId);

        Optional<Client> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void testDeleteWhenDoesNotExistId() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {

            repository.deleteById(nonExistingId);

        });

    }

}
