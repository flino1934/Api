package com.lino.dscatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.services.CategoryService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean//Usamos pois esta sendo carregado o contexto apenas do controller
    private CategoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<CategoryDTO> page;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 23L;
        categoryDTO = CategoryFactory.createCategoryDTORepository();
        page = new PageImpl<>(List.of(categoryDTO));

        //Simulando os comprtamentos do service

        //Vai simular o comportamento do find all paged
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Vai simular o find by id quando o id existir
        Mockito.when(service.findById(existingId)).thenReturn(categoryDTO);
        //Vai simular o find by id quando o não id existir e deve retornar ResourceNotFoundExceptions
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);
    }

    @Test
    public void testFindAllPagedShouldReturnPaged() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/categories")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    public void testFindByidWhenIdExistShouldReturnCategory() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/categories/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());

    }
    @Test
    public void testFindByidWhenIdDoesNotExistShouldReturnThrowResourceNotFoundExceptions() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/categories/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }


}
