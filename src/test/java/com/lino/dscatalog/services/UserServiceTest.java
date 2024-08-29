package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.dto.UserInsertDTO;
import com.lino.dscatalog.entities.Role;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.factory.UserFactory;
import com.lino.dscatalog.repositories.ClientRepository;
import com.lino.dscatalog.repositories.RoleRepository;
import com.lino.dscatalog.repositories.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)//usado para fazer teste do service
public class UserServiceTest {

    @InjectMocks//Essa anotação utiliza para injetar o service já que não precisa carregar o contexto
    private UserService service;
    @Mock//Essa anotação utiliza quando não precisa carregar o contexto da app pq esta utilizando o @ExtendWith
    private UserRepository repository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    private long existingId;
    private long nonExistingId;
    private PageImpl<User> page;
    private User user;
    private UserInsertDTO userInsertDTO;
    private UserDTO userDTO;
    private Role role;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        user = UserFactory.createUser();
        userDTO = UserFactory.createUserDTO();
        userInsertDTO = UserFactory.createUserInsert();
        page = new PageImpl<>(List.of(user));
        role = new Role(2L, "ROLE_ADMIN");

        //Configurando o comprtamento do repository

        //Esta simulando o comportamento do repository do find all paged
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Simulando o comportamento do find by id when id exist
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(user));

        //Simulando o comportamento do find by id when não id exist
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundExceptions.class);

        //Simulando o insert com tudo correto
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        // Simulação do roleRepository.getOne()
        Mockito.when(roleRepository.getOne(ArgumentMatchers.anyLong())).thenReturn(role);

        // Simulação do passwordEncoder.encode()
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");

    }

    @Test
    public void testFindAllPaged() {

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        //Act
        Page<UserDTO> result = service.findAllPaged(pageable);

        //Assert
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);

    }

    @Test
    public void testFindByIdWhenIdExist(){

        //Arrange esta sendo feito no beforeach

        //ACT
        UserDTO result = service.findById(existingId);

        //Assertion

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);

    }
    @Test
    public void testFindByIdWhenIdDoesNotExist(){

        //Arrange esta sendo feito no beforeach

        //Assertion
        Assertions.assertThrows(ResourceNotFoundExceptions.class, ()->{
           service.findById(nonExistingId);
        });

    }

    @Test
    public void testSaveUserCorrect(){

        //Arrange esta sendo feito no beforeach

        //ACT
        UserDTO result = service.insert(userInsertDTO);
        //Assertions
        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(User.class)); // Verifica se o save foi chamado uma vez

    }
}
