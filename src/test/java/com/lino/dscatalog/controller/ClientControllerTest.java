package com.lino.dscatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ClientFactory;
import com.lino.dscatalog.services.CategoryService;
import com.lino.dscatalog.services.ClientService;
import com.lino.dscatalog.services.exceptions.DataBaseExceptions;
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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean//Usamos pois esta sendo carregado o contexto apenas do controller
    private ClientService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<ClientDTO> page;

    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        clientDTO = ClientFactory.createClientDTOWithId();
        page = new PageImpl<>(List.of(clientDTO));

        //ira simular o service

        //Simulando o findAllPaged
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Simulando o findById quando o id existir
        Mockito.when(service.findById(existingId)).thenReturn(clientDTO);

        //Simulando o findById quando o id não existir
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

        //Simulando o insert
        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(clientDTO);

        //Simulando o update quando o id não existir
        Mockito.when(service.update(ArgumentMatchers.eq(existingId),ArgumentMatchers.any())).thenReturn(clientDTO);

        //Simulando o update quando id não existir
        Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId),ArgumentMatchers.any())).thenThrow(ResourceNotFoundExceptions.class);

        //Vai simular o delee quando o id existir
        Mockito.doNothing().when(service).delete(existingId);

        //Vai simular o delee quando o id não existir
        Mockito.doThrow(ResourceNotFoundExceptions.class).when(service).delete(nonExistingId);

        //Vai simular o delee quando o id for dependente
        Mockito.doThrow(DataBaseExceptions.class).when(service).delete(dependentId);

    }

    @Test
    public void testFindAllPagedShouldReturnPaged() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/clients")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void testFindByidWhenIdExistShouldReturnClient() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/clients/{id}",existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.cpf").exists());

    }

    @Test
    public void testFindByidWhenIdDoesNotExistShouldReturnNotFound() throws Exception {

        ResultActions result =
                mockMvc.perform(get("/api/clients/{id}",nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void testInsertShoulReturnCategory() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result =
                mockMvc.perform(post("/api/clients")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.cpf").exists());

    }

    @Test
    public void testUpdateWhenIdExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result =
                mockMvc.perform(put("/api/clients/{id}",existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());


    }
    @Test
    public void testUpdateWhenDoesNotIdExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result =
                mockMvc.perform(put("/api/clients/{id}",nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }
    @Test
    public void testDeleteWhenIdExist() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/api/clients/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());

    }
    @Test
    public void testDeleteWhenIdDoesnotExist() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/api/clients/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }
    @Test
    public void testDeleteWhenIdDependent() throws Exception{

        ResultActions result =
                mockMvc.perform(delete("/api/clients/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());

    }


}
