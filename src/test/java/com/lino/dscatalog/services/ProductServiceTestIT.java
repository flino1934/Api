package com.lino.dscatalog.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;
import com.lino.dscatalog.factory.CategoryFactory;
import com.lino.dscatalog.factory.ProductFactory;
import com.lino.dscatalog.repositories.ProductRepository;
import com.lino.dscatalog.services.ProductService;
import com.lino.dscatalog.services.exceptions.ResourceNotFoundExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ProductServiceTestIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private long countTotalProducts;
    private PageImpl<ProductDTO> page;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {

        //Estara fazendo o Arrange
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 23L;
        countTotalProducts = 25L;
        product = ProductFactory.createProduct();
        productDTO = ProductFactory.createProductDTO();
        category = CategoryFactory.createCategory();
        categoryDTO = CategoryFactory.createCategoryDTO();
        page = new PageImpl<>(List.of(productDTO));

    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExist() {

        //Arrange essta sendo feito no beforeach

        //Act
        service.delete(existingId);

        //Assertions
        Assertions.assertEquals(countTotalProducts - 1, repository.count());


    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionsWhenIdDoesNotExist() {

        //Arrange essta sendo feito no beforeach

        //Assertions
        Assertions.assertThrows(ResourceNotFoundExceptions.class, () -> {
            service.delete(nonExistingId);//Quando o JUnit rodar ele vai chamar deleteById com id inesistente do @BeforEach
        });

    }

    @Test
    public void findAllPagedShouldReturnPageWhenPageZeroSizeTen() {

        //Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);

        //Act
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        //Assertion
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0,result.getNumber());
        Assertions.assertEquals(countTotalProducts,result.getTotalElements());

    }
    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {

        //Arrange
        PageRequest pageRequest = PageRequest.of(50, 10);

        //Act
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        //Assertion
        Assertions.assertTrue(result.isEmpty());

    }
    @Test
    public void findAllPagedShouldReturnOrdenedPageWhenSortByName() {

        //Arrange
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        //Act
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        //Assertion
        Assertions.assertEquals("Macbook Pro",result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer",result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa",result.getContent().get(2).getName());

    }

    @Test
    public void findByIDWhenIdExistReturnProduct(){

    }


}
