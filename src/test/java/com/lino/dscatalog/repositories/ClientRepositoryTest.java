package com.lino.dscatalog.repositories;

import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ClientFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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


}
