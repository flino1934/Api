package com.lino.dscatalog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.services.ProductService;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@WebMvcTest(ProductController.class)//Essa anotação é para fazer teste apenas na camada web
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean//Usamos pois esta sendo carregado o contexto apenas do controller
    private ProductService service;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private long countTotalProducts;
    private PageImpl<ProductDTO> page;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO();
        category = CategoryFactory.createCategory();
        categoryDTO = CategoryFactory.createCategoryDTO();
        page = new PageImpl<>(List.of(productDTO));

        //Simulando os comprtamentos do service

        //Simulando o find all paged
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Simulando o find by id quando o id existir
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);

        //Simulando o find by id quando o id não existir
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnProductWhenIdExist() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/products/{id}",existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/products/{id}",nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

}
