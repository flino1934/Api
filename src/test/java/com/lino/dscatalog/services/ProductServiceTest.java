package com.lino.dscatalog.services;

import com.lino.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)//usado para fazer teste do service
public class ProductServiceTest {

    @InjectMocks//Essa anotação utiliza para
    private ProductService service;

    @Mock//Essa anotação utiliza quando não precisa carregar o contexto da app pq esta utilizando o @ExtendWith
    private ProductRepository repository;

}
