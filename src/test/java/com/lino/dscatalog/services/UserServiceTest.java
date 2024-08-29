package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.*;
import com.lino.dscatalog.entities.Role;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.factory.UserFactory;
import com.lino.dscatalog.repositories.ClientRepository;
import com.lino.dscatalog.repositories.RoleRepository;
import com.lino.dscatalog.repositories.UserRepository;
import com.lino.dscatalog.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private long dependeteId;
    private PageImpl<User> page;
    private User user;
    private UserInsertDTO userInsertDTO;
    private UserUpdateDTO userUpdateDTO;
    private UserDTO userDTO;
    private Role role;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependeteId = 3L;
        user = UserFactory.createUser();
        userDTO = UserFactory.createUserDTO();
        userInsertDTO = UserFactory.createUserInsert();
        userUpdateDTO = UserFactory.createUserUpdate();
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

        //Simulação do get one do User quando o id existir
        Mockito.when(repository.getOne(existingId)).thenReturn(user);

        //Simulação do get one do User quando o id não existir
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EmptyResultDataAccessException.class);

        //Simulação do delete quando o id existir
        Mockito.doNothing().when(repository).deleteById(existingId);

        //Simulação delete quando id não existir
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        //Simulação delete quando id for dependente
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependeteId);

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

    @Test
    public void testUpdateWhenIdExist(){

        //Arrange esta sendo feito no beforeach

        //ACT
        UserDTO result = service.update(existingId, userUpdateDTO);
        result.setLastName("Lino");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Lino",result.getLastName());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(User.class)); // Verifica se o save foi chamado uma vez

    }
    @Test
    public void testUpdateWhenIdDoesNotExist(){

        //Arrange esta sendo feito no beforeach

        //ACT
       Assertions.assertThrows(ResourceNotFoundExceptions.class,()->{
          service.update(nonExistingId,userUpdateDTO);
       });

    }

    @Test
    public void testDeleteByIdWhenIdExist(){

        //Arrange esta sendo feito no beforeach

        //Act
        service.delete(existingId);
        Mockito.verify(repository).deleteById(existingId);


    }
    @Test
    public void testDeleteByIdWhenIdDoesNotExistShouldReturnResourceNotFoundExceptions(){

        //Arrange esta sendo feito no beforeach

        //Act
        Assertions.assertThrows(ResourceNotFoundExceptions.class,()->{
           service.delete(nonExistingId);
        });
        Mockito.verify(repository).deleteById(nonExistingId);

    }
    @Test
    public void testDeleteByIdWhenDependentIdShouldReturnDataBaseExceptions(){

        //Arrange esta sendo feito no beforeach

        //Act
        Assertions.assertThrows(DataBaseExceptions.class,()->{
           service.delete(dependeteId);
        });
        Mockito.verify(repository).deleteById(dependeteId);

    }
}
