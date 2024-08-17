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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    private Product product;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        product = ProductFactory.createProduct();

        //================= Configurando o comportamento simulado do repository ==============================

        //Configurando o delete by id quando existir
        Mockito.doNothing().when(repository).deleteById(existingId);

        //Configurando o delete by id quando não existir
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        //Configurando o delete by id com violação de integridade
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }

    @Test
    public void testDeleteShouldDoNothingWhenIdExist() {

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);//Quando o JUnit rodar ele vai chamar deleteById do @BeforEach

        });

        Mockito.verify(repository).deleteById(existingId);//Verifica se o metodo deleteById foi chamado no repository

    }

    @Test
    public void deleteShouldThrowExceptionResourceNotFoundExceptionsWhenIdNonExist(){

        Assertions.assertThrows(ResourceNotFoundExceptions.class, ()->{
           service.delete(nonExistingId);//Quando o JUnit rodar ele vai chamar deleteById com id inesistente do @BeforEach
        });
        Mockito.verify(repository).deleteById(nonExistingId);//Verifica se o metodo deleteById foi chamado no repository

    }

    @Test
    public void deleteShouldThrowDataBaseExceptionsWhenDependentId(){

        Assertions.assertThrows(DataBaseExceptions.class, ()->{
           service.delete(dependentId);//Quando o JUnit rodar ele vai chamar deleteById com id dependente do @BeforEach
        });
        Mockito.verify(repository).deleteById(dependentId);//Verifica se o metodo deleteById foi chamado no repository

    }


}
