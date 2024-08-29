package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.dto.UserInsertDTO;
import com.lino.dscatalog.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

public class UserFactory {

    public static User createUser() {
        return new User(null, "Michaelly Monique", "Oliveira Di Pardo", "mica@gmail.com", "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG");

    }

    public static UserDTO createUserDTO() {
        User user = createUser();
        return new UserDTO(user);
    }

    // Método adicional para criar um UserDTO com ID, se necessário para o teste
    public static UserDTO createUserDTOWithId() {
        User user = new User(2L, "Michaelly Monique", "Oliveira Di Pardo", "mica@gmail.com", "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG");

        return new UserDTO(user);
    }
    public static UserInsertDTO createUserInsert() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserInsertDTO userInsertDTO = new UserInsertDTO();
        userInsertDTO.setFirstName("Michaelly");
        userInsertDTO.setLastName("Monique");
        userInsertDTO.setEmail("mica@gmail.com");
        userInsertDTO.setPassword(passwordEncoder.encode("123456")); // Senha codificada

        // Adicionar roles ao userInsertDTO, se necessário
        return userInsertDTO;
    }
}
