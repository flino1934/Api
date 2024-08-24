package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.RoleDTO;
import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.entities.Role;
import com.lino.dscatalog.entities.User;
import com.lino.dscatalog.repositories.RoleRepository;
import com.lino.dscatalog.repositories.UserRepository;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

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
    public UserDTO insert(UserDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);

        return new UserDTO(entity);

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
}
