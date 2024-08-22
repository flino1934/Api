package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.factory.ClientFactory;
import com.lino.dscatalog.repositories.ClientRepository;
import com.lino.dscatalog.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
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
        dependentId = 2L;
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

        //Simulando o insert
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(client);

        //Simulando o get one para o update quando o id existir
        Mockito.when(repository.getOne(existingId)).thenReturn(client);

        //Simulando o get one para o update quando o id não existir devera retornar ResourceNotFoundExceptions
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //Simulando o delete quando o id existir
        Mockito.doNothing().when(repository).deleteById(existingId);

        //Simulando o delete quando o não existir deve retornar ResourceNotFoundExceptions
        Mockito.doThrow(EntityNotFoundException.class).when(repository).deleteById(nonExistingId);

        //Simulando o delete quando o id for dependente retornar DataBaseExceptions
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }

    @Test
    public void testFindAllPaged() {

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<ClientDTO> result = service.findAllPaged(pageable);

        //Assert
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);

    }

    @Test
    public void testFindByIdExistingIdShouldReturnClient() {

        //Arrange

        ClientDTO result = service.findById(existingId);

        //Assertions
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);

    }

    @Test
    public void testFindByIdWhenIdDoesNothingExistShouldReturnResourceNotFoundExceptions() {

        Assertions.assertThrows(ResourceNotFoundExceptions.class, () -> {
            service.findById(nonExistingId);
        });
        Mockito.verify(repository).findById(nonExistingId);

    }

    @Test
    public void testInsertShouldReturnClient() {

        //Arrange esta sendo feito no beforeach

        ClientDTO result = service.insert(clientDTO);

        Assertions.assertNotNull(result);

    }

    @Test
    public void testUpdateWhenIdExistShouldReturnClient() {

        //Arrange

        ClientDTO result = service.update(existingId, clientDTO);
        result.setName("Felipe ALves Lino");

        Assertions.assertEquals("Felipe ALves Lino", result.getName());
        Mockito.verify(repository).getOne(existingId);

    }

    @Test
    public void testUpdateWhenIdDoesNothingExistShouldReturnException() {

        Assertions.assertThrows(ResourceNotFoundExceptions.class, () -> {
            service.update(nonExistingId, clientDTO);
        });

    }

    @Test
    public void testDeleteWhenIdExists() {
        // Act
        service.delete(existingId);

        // Assert
        Mockito.verify(repository).deleteById(existingId);
    }

    @Test
    public void testDeleteWhenIdDoesNotExistShouldReturnResourceNotFoundExceptions() {

        Assertions.assertThrows(ResourceNotFoundExceptions.class, () -> {
            service.delete(nonExistingId);
        });
        Mockito.verify(repository).deleteById(nonExistingId);

    }

    @Test
    public void testDeleteWhenIdDependentShouldReturnDataBaseExceptions() {

        Assertions.assertThrows(DataBaseExceptions.class, () -> {
            service.delete(dependentId);
        });

        Mockito.verify(repository).deleteById(dependentId);

    }


}
