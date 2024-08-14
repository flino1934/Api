package com.lino.dscatalog.services;

import com.lino.dscatalog.dto.ClientDTO;
import com.lino.dscatalog.entities.Client;
import com.lino.dscatalog.repositories.ClientRepository;
import com.lino.dscatalog.services.services.exceptions.DataBaseExceptions;
import com.lino.dscatalog.services.services.exceptions.ResourceNotFoundExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(Pageable pageable) {

        Page<Client> list = repository.findAll(pageable);
        return list.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {

        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundExceptions("Client not found!!"));
        ClientDTO dto = new ClientDTO(entity);

        return dto;

    }

    @Transactional
    public ClientDTO insert(ClientDTO dto) {

        Client client = new Client();
        client.setName(dto.getName());
        client.setCpf(dto.getCpf());
        client.setChildren(dto.getChildren());
        client.setBirthDate(dto.getBirthDate());

        repository.save(client);

        return new ClientDTO(client);

    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {

        try {
            Client entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity.setChildren(dto.getChildren());

            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {

            throw new ResourceNotFoundExceptions("Id not found " + id);

        }

    }

    @Transactional
    public void delete(Long id) {

        try {

            Client clientDTO = repository.getOne(id);
            repository.delete(clientDTO);

        } catch (EntityNotFoundException e) {

            throw new ResourceNotFoundExceptions("Id not found " + id);

        }catch (DataIntegrityViolationException e){

            throw new DataBaseExceptions("Database Violation");

        }
    }
}
