package com.lino.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServeConfig extends ResourceServerConfigurerAdapter {
    //Essa classe é responsavel por verificar se o usuario logado pode ou não acessar recursso x ou y

    @Autowired
    private JwtTokenStore tokenStore;

     private static final String[] PUBLIC = {"/oauth/token"};
     private static final String[] OPERATOR_OR_ADMIN = {"/api/products/**","/api/categories/**"};
     private static final String[] ADMIN = {"/api/users/**"};

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception { //Vai avaliar se o token é valido ou não 
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {// Vai configurar as rotas 

        http.authorizeRequests()
                .antMatchers(PUBLIC).permitAll()
                .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
                .antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR","ADMIN")
                .antMatchers(ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated();

    }
}
