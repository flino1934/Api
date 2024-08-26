package com.lino.dscatalog.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
//Muitos para Muitos, um usuario pode ter varias roles assim como uma role pode ter varios usuarios
    @JoinTable(name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//Vai retornar uma lista com os perfis que o usuario tem

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

    }

    public String getPassword() {
        return password;
    }//Retorna a senha

    @Override
    public String getUsername() {//Pega o usuario, que vai ser no nosso caso o email
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {//testa se a conta não esta expirada
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {//Verifica se o usuario não esta bloqueado
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//Verifica se a credencial não expirou
        return true;
    }

    @Override
    public boolean isEnabled() {//Verifica se o usuario esta habilitado
        return true;
    }
}
