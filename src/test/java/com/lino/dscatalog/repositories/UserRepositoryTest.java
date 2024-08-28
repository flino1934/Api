package com.lino.dscatalog.repositories;

import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.factory.UserFactory;
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

@DataJpaTest//Usado para testar a camada de repository
public class UserRepositoryTest {

    private long existingId;
    private long nonExistingId;

    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        user = UserFactory.createUser();
    }

    @Test
    public void testFindAllPaged() {

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<User> result = repository.findAll(pageable);

        //Assert
        Assertions.assertNotNull(result);

    }

    @Test
    public void testFindByIdWhenIdExist(){

        //Arrange and  Act
        Optional<User> obj = repository.findById(existingId);

        //Assert
        Assertions.assertTrue(obj.isPresent());

    }

    @Test
    public void testFindByIdWhenIdDoesnothingExist(){

        //Arrange and  Act
        Optional<User> obj = repository.findById(nonExistingId);

        //Assert
        Assertions.assertTrue(obj.isEmpty());
    }

    @Test
    public void testFindByEmailReturnUser(){

        //Arrange and act

        User find = repository.findByEmail(user.getEmail());

        //Assertions
        Assertions.assertEquals("mica@gmail.com", find.getEmail());
        Assertions.assertEquals("Michaelly Monique", find.getFirstName());
        Assertions.assertEquals("Oliveira Di Pardo",user.getLastName());

    }

    @Test
    public void testDeleteWhenIdExist() {

        repository.deleteById(existingId);

        Optional<User> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void testDeleteWhenDoesNotExistId() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {

            repository.deleteById(nonExistingId);

        });

    }

}
