package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.entities.User;

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
}