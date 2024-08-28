package com.lino.dscatalog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.services.CategoryService;
import com.lino.dscatalog.services.ProductService;
import com.lino.dscatalog.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import com.lino.dscatalog.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean//Usamos pois esta sendo carregado o contexto apenas do controller
    private CategoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String operatorUsername;
    private String operatorPassword;
    private String adminUsername;
    private String adminPassword;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<CategoryDTO> page;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Fazendo autenticação
        operatorUsername = "f.lino1934@hotmail.com";
        operatorPassword  = "123456";
        adminUsername = "mica@gmail.com";
        adminPassword = "123456";

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        categoryDTO = CategoryFactory.createCategoryDTORepository();
        page = new PageImpl<>(List.of(categoryDTO));

        //Simulando os comprtamentos do service

        //Vai simular o comportamento do find all paged
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Vai simular o find by id quando o id existir
        Mockito.when(service.findById(existingId)).thenReturn(categoryDTO);

        //Vai simular o find by id quando o não id existir e deve retornar ResourceNotFoundExceptions
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

        //Vai simular o insert
        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(categoryDTO);

        //Vai simular o delete quando o id existir
        Mockito.doNothing().when(service).delete(existingId);

        //Vai simular o delete quando o id não existir e vai lançar ResourceNotFoundExceptions
        Mockito.doThrow(ResourceNotFoundExceptions.class).when(service).delete(nonExistingId);

        //Vai simular o delete quando o id for dependete e vai lançar DataBaseExceptions
        Mockito.doThrow(DataBaseExceptions.class).when(service).delete(dependentId);

        //Vai simular o get one de quandop o id eixistir para fazer o update
        Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(categoryDTO);

        //Vai simular o get one de quandop o id não eixistir e lanãr uma exception
        Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundExceptions.class);

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

    @Test
    public void testInsertShoulReturnCategory() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(categoryDTO);

        ResultActions result =
                mockMvc.perform(post("/api/categories")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

    }

    @Test
    public void testDeleteWhenIdExist() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/api/categories/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());

    }

    @Test
    public void testDeleteWhenIdDoesNotExistShouldReturnResourceNotFoundExceptions() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/api/categories/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }
    @Test
    public void testDeleteWhenIdDependentShouldReturnException() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/api/categories/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateWhenIdExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(categoryDTO);

        ResultActions result =
                mockMvc.perform(put("/api/categories/{id}",existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.name").exists());

    }

    @Test
    public void testUpdateWhenIdDoesNothingExistShoulReturnThrowResourceNotFoundExceptions() throws Exception{


        String jsonBody = objectMapper.writeValueAsString(categoryDTO);

        ResultActions result =
                mockMvc.perform(put("/api/categories/{id}",nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }


}
