package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.RoleDTO;
import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.dto.UserInsertDTO;
import com.lino.dscatalog.entities.Role;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.repositories.RoleRepository;
import com.lino.dscatalog.repositories.UserRepository;
import com.lino.dscatalog.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> list = repository.findAll(pageable);
        return list.map(x -> new UserDTO(x));

    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundExceptions("User not found!!"));
        return new UserDTO(entity);

    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);

        return new UserDTO(entity);

    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {

        try {
            User entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);

            return new UserDTO(entity);

        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundExceptions("Id Not found excption");
        }

    }


    private void copyDtoToEntity(UserDTO dto, User entity) {

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();

        for (RoleDTO roleDTO : dto.getRoles()) {

            Role role = roleRepository.getOne(roleDTO.getId());
            entity.getRoles().add(role);

        }

    }

    public void delete(Long id) {

        try{
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){

            throw new ResourceNotFoundExceptions("Id not found!!!");

        }catch (DataIntegrityViolationException e){

            throw new DataBaseExceptions("Databse violation!!");

        }

    }
}
