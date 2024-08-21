package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.repositories.CategoryRepository;
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
public class CategoryServiceTest {

    @InjectMocks//Essa anotação utiliza para injetar o service já que não precisa carregar o contexto
    private CategoryService service;

    @Mock//Essa anotação utiliza quando não precisa carregar o contexto da app pq esta utilizando o @ExtendWith
    private CategoryRepository repository;

    private long existingId;
    private long nonExistingId;
    private long totalCounts;
    private PageImpl<Category> page;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        totalCounts = 3L;
        product = ProductFactory.createProductTestService();
        productDTO = ProductFactory.createProductDTOTestService();
        category = CategoryFactory.createCategory();
        categoryDTO = CategoryFactory.createCategoryDTO();
        page = new PageImpl<>(List.of(category));

        //Esta simulando o comportamento do repository do find all paged
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Esta simulando o comportamento do repository do find by id
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(category));

        //Esta simulando o comportamento do find by id quando o id não existir
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

        //Esta simulando o comportamnet do insert
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(category);

        //Esta simulando o comportamento do get one quando o id existir para poder fazer o update
        Mockito.when(repository.getOne(existingId)).thenReturn(category);

        //Esta simulando o comportamento get one quando o id não existir e vai retornar ResourceNotFoundExceptions
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    public void testFindAllPaged(){

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<CategoryDTO> result = service.findAllPaged(pageable);

        //Assert
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);

    }

    @Test
    public void testFindByIdWhenIdExistReturnCategory(){

        //Arrange

        //Act
        CategoryDTO result = service.findById(existingId);

        //Assertion
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void testFindByIdWhenIdDoesNotExistShouldReturnResourceNotFoundExceptions(){

        Assertions.assertThrows(ResourceNotFoundExceptions.class, ()->{

            service.findById(nonExistingId);

        });

        Mockito.verify(repository).findById(nonExistingId);

    }

    @Test
    public void testSaveCategoryShouldReturnCategory(){

        //Act
        CategoryDTO insert = service.insert(categoryDTO);

        //Assertions
        Assertions.assertNotNull(insert);
        Mockito.verify(repository).save(category);

    }

    @Test
    public void testUpdateWhenIdExistShouldReturnProduct(){

        CategoryDTO update = service.update(existingId, categoryDTO);
        update.setName("update");

        Assertions.assertEquals("update",update.getName());
        Mockito.verify(repository).getOne(existingId);

    }

    @Test
    public void testUpdateWhenIdDoesNotExistReturnResourceNotFoundExceptions(){

        Assertions.assertThrows(ResourceNotFoundExceptions.class, ()->{
            service.update(nonExistingId,categoryDTO);
        });

        Mockito.verify(repository).getOne(nonExistingId);

    }

}
