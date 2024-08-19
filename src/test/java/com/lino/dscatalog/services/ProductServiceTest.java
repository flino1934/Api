package com.lino.dscatalog.services;

import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.repositories.ProductRepository;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)//usado para fazer teste do service
public class ProductServiceTest {

    @InjectMocks//Essa anotação utiliza para
    private ProductService service;
    @Mock//Essa anotação utiliza quando não precisa carregar o contexto da app pq esta utilizando o @ExtendWith
    private ProductRepository repository;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private long countTotalProducts;
    private PageImpl<Product> page;

    private Product product;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        product = ProductFactory.createProduct();
        page = new PageImpl<>(List.of(product));

        //================= Configurando o comportamento simulado do repository ==============================

        //Configurando o delete by id quando existir
        Mockito.doNothing().when(repository).deleteById(existingId);

        //Configurando o delete by id quando não existir
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        //Configurando o delete by id com violação de integridade
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        //Configurando o find all paged
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Configurando o findById quando o id existir
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        //Configurando o findById quando o id não existir
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Configurando save
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

    }

    @Test
    public void testDeleteShouldDoNothingWhenIdExist() {

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);//Quando o JUnit rodar ele vai chamar deleteById do @BeforEach

        });

        Mockito.verify(repository).deleteById(existingId);//Verifica se o metodo deleteById foi chamado no repository

    }

    @Test
    public void deleteShouldThrowExceptionResourceNotFoundExceptionsWhenIdNonExist() {

        Assertions.assertThrows(ResourceNotFoundExceptions.class, () -> {
            service.delete(nonExistingId);//Quando o JUnit rodar ele vai chamar deleteById com id inesistente do @BeforEach
        });
        Mockito.verify(repository).deleteById(nonExistingId);//Verifica se o metodo deleteById foi chamado no repository

    }

    @Test
    public void deleteShouldThrowDataBaseExceptionsWhenDependentId() {

        Assertions.assertThrows(DataBaseExceptions.class, () -> {
            service.delete(dependentId);//Quando o JUnit rodar ele vai chamar deleteById com id dependente do @BeforEach
        });
        Mockito.verify(repository).deleteById(dependentId);//Verifica se o metodo deleteById foi chamado no repository

    }


}
