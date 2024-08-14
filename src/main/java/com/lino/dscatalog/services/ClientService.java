package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.repositories.ClientRepository;
import com.lino.dscatalog.services.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional
    public Page<ClientDTO> findAllPaged(Pageable pageable) {

        Page<Client> list = repository.findAll(pageable);
        return list.map(x -> new ClientDTO(x));
    }

    public ClientDTO findById(Long id) {

        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundExceptions("Client not found!!"));
        ClientDTO dto = new ClientDTO(entity);

        return dto;

    }
}
