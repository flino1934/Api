package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ClientFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.repositories.CategoryRepository;
import com.lino.dscatalog.repositories.ClientRepository;
import com.lino.dscatalog.repositories.ProductRepository;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)//usado para fazer teste do service
public class ClientServiceTest {

    @InjectMocks//Essa anotação utiliza para injetar o service já que não precisa carregar o contexto
    private ClientService service;
    @Mock//Essa anotação utiliza quando não precisa carregar o contexto da app pq esta utilizando o @ExtendWith
    private ClientRepository repository;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Client> page;
    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 22L;
        client = ClientFactory.createClient();
        clientDTO = ClientFactory.createClientDTO();
        page = new PageImpl<>(List.of(client));

        //Configurando o comprtamento do repository

        //Esta simulando o comportamento do repository do find all paged
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Simulando o findById quando o id existir
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(client));

        //Simulando o find by id quando o id não existir
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);


    }

    @Test
    public void testFindAllPaged(){

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<ClientDTO> result = service.findAllPaged(pageable);

        //Assert
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);

    }

    @Test
    public void testFindByIdExistingIdShouldReturnClient(){

        //Arrange

        ClientDTO result = service.findById(existingId);

        //Assertions
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);

    }

    @Test
    public void testFindByIdWhenIdDoesNothingExistShouldReturnResourceNotFoundExceptions(){

        Assertions.assertThrows(ResourceNotFoundExceptions.class,()->{
           service.findById(nonExistingId);
        });

    }



}
