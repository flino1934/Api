package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.repositories.CategoryRepository;
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

import java.util.List;

@ExtendWith(SpringExtension.class)//usado para fazer teste do service
public class CategoryServiceTest {

    @InjectMocks//Essa anotação utiliza para injetar o service já que não precisa carregar o contexto
    private CategoryService service;

    @Mock//Essa anotação utiliza quando não precisa carregar o contexto da app pq esta utilizando o @ExtendWith
    private CategoryRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
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
        dependentId = 22L;
        product = ProductFactory.createProductTestService();
        productDTO = ProductFactory.createProductDTOTestService();
        category = CategoryFactory.createCategory();
        categoryDTO = CategoryFactory.createCategoryDTO();
        page = new PageImpl<>(List.of(category));

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

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



}
