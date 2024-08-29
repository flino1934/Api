package com.lino.dscatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.factory.UserFactory;
import com.lino.dscatalog.services.ProductService;
import com.lino.dscatalog.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

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
    private PageImpl<UserDTO> page;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Fazendo autenticação
        adminUsername = "mica@gmail.com";
        adminPassword = "123456";

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 23L;
        userDTO = UserFactory.createUserDTO();
        page = new PageImpl<>(List.of(userDTO));

    }

}
