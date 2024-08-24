package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.entities.User;
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

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> list = repository.findAll(pageable);
        return list.map(x -> new UserDTO(x));

    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(()-> new ResourceNotFoundExceptions("User not found!!"));
        return new UserDTO(entity);

    }
}
