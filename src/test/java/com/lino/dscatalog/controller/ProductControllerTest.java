package com.lino.dscatalog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.factory.ProductFactory;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean//Usamos pois esta sendo carregado o contexto apenas do controller
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private String adminUsername;
    private String adminPassword;
    private PageImpl<ProductDTO> page;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Fazendo autenticação
        adminUsername = "mica@gmail.com";
        adminPassword = "123456";

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 23L;
        productDTO = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        //Simulando os comprtamentos do service

        //Simulando o find all paged
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Simulando o find by id quando o id existir
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);

        //Simulando o find by id quando o id não existir
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

        //Simulando o update quando o id existir
        Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);

        //Simulando o update quando o id não existir
        Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundExceptions.class);

        //Simulando o delete quando o id existir
        Mockito.doNothing().when(service).delete(existingId);

        //Simulando quando o id não existir e retornar ResourceNotFoundExceptions
        Mockito.doThrow(ResourceNotFoundExceptions.class).when(service).delete(nonExistingId);

        //Simulando quando o id não existir e retornar DataIntegrityViolationException
        Mockito.doThrow(DataBaseExceptions.class).when(service).delete(dependentId);

        //Simulando o insert
        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);

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
                mockMvc.perform(get("/api/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist() throws Exception {

        String accesToken = tokenUtil.obtainAccessToken(mockMvc,adminUsername,adminPassword);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/api/products/{id}", existingId)
                        .header("Authorization","Bearer " +accesToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }
    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        String accesToken = tokenUtil.obtainAccessToken(mockMvc,adminUsername,adminPassword);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(put("/api/products/{id}", nonExistingId)
                        .header("Authorization","Bearer " +accesToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void testInsertShouldReturnCreated() throws Exception {

        String accesToken = tokenUtil.obtainAccessToken(mockMvc,adminUsername,adminPassword);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(post("/api/products")
                        .header("Authorization","Bearer " +accesToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void testDeleteShouldReturnNoContentWhenIdExist() throws Exception {

        String accesToken = tokenUtil.obtainAccessToken(mockMvc,adminUsername,adminPassword);

        ResultActions result =
                mockMvc.perform(delete("/api/products/{id}", existingId)
                        .header("Authorization","Bearer " +accesToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());

    }

    @Test
    public void testDeleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        String accesToken = tokenUtil.obtainAccessToken(mockMvc,adminUsername,adminPassword);

        ResultActions result =
                mockMvc.perform(delete("/api/products/{id}", nonExistingId)
                        .header("Authorization","Bearer " +accesToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void testDeleteShouldReturnNotFoundWhenIdDoesNotExistException() throws Exception {

        String accesToken = tokenUtil.obtainAccessToken(mockMvc,adminUsername,adminPassword);

        ResultActions result =
                mockMvc.perform(delete("/api/products/{id}", dependentId)
                        .header("Authorization","Bearer " +accesToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());

    }

}
