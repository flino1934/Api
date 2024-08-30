package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.RoleDTO;
import com.lino.dscatalog.dto.UserDTO;
import com.lino.dscatalog.dto.UserInsertDTO;
import com.lino.dscatalog.dto.UserUpdateDTO;
import com.lino.dscatalog.entities.Role;
import com.lino.dscatalog.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class UserFactory {

    // Codificador de senhas BCrypt para garantir que as senhas estão corretas
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Senha codificada para "123456"
    private static final String ENCODED_PASSWORD = passwordEncoder.encode("123456");

    public static User createUser() {
        User user = new User(null, "Michaelly Monique", "Oliveira Di Pardo", "mica@gmail.com", ENCODED_PASSWORD);
        user.getRoles().add(new Role(1L, "ROLE_OPERATOR"));
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static UserDTO createUserDTO() {
        User user = createUser();
        return new UserDTO(user);
    }

    public static UserDTO createUserDTOWithId() {
        User user = new User(2L, "Michaelly Monique", "Oliveira Di Pardo", "mica@gmail.com", ENCODED_PASSWORD);
        user.getRoles().add(new Role(1L, "ROLE_OPERATOR"));
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        return new UserDTO(user);
    }

    public static UserInsertDTO createUserInsert() {
        UserInsertDTO userInsertDTO = new UserInsertDTO();
        userInsertDTO.setFirstName("Michaelly");
        userInsertDTO.setLastName("Monique");
        userInsertDTO.setEmail("mica@gmail.com");
        userInsertDTO.setPassword("123456");  // Senha simples, será codificada pelo serviço

        Set<RoleDTO> roles = new HashSet<>();
        roles.add(new RoleDTO(1L, "ROLE_OPERATOR"));
        roles.add(new RoleDTO(2L, "ROLE_ADMIN"));
        userInsertDTO.getRoles().addAll(roles);

        return userInsertDTO;
    }

    public static UserUpdateDTO createUserUpdate() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setFirstName("Michaelly Monique");
        userUpdateDTO.setLastName("Oliveira Di Pardo");
        userUpdateDTO.setEmail("mica@gmail.com");

        Set<RoleDTO> roles = new HashSet<>();
        roles.add(new RoleDTO(1L, "ROLE_OPERATOR"));
        roles.add(new RoleDTO(2L, "ROLE_ADMIN"));
        userUpdateDTO.getRoles().addAll(roles);

        return userUpdateDTO;
    }
}