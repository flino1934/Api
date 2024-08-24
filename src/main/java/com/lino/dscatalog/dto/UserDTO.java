package com.lino.dscatalog.dto;

import com.lino.dscatalog.entities.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Entre com email valído")
    private String email;

    private Set<RoleDTO> roles = new HashSet<>();//Vai estar trazendo os roles junto

    public UserDTO() {
    }

    public UserDTO(long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        entity.getRoles().forEach(role ->  this.roles.add(new RoleDTO(role)));//Vai adicionar ao  contrutor os parametros vindo da lista
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
