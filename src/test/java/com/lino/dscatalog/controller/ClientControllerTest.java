package com.lino.dscatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ClientFactory;
import com.lino.dscatalog.services.CategoryService;
import com.lino.dscatalog.services.ClientService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        dependentId = 3L;
        clientDTO = ClientFactory.createClientDTOWithId();
        page = new PageImpl<>(List.of(clientDTO));

        //ira simular o service

        //Simulando o findAllPaged
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //Simulando o findById quando o id existir
        Mockito.when(service.findById(existingId)).thenReturn(clientDTO);

        //Simulando o findById quando o id não existir
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

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

}
