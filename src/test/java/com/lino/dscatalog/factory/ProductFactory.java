package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.ProductDTO;
import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createProduct() {
        Product product = new Product(1L, "Harry Potter and the Philosopher's Stone", 50.0, "Harry Potter, an eleven-year-old orphan, discovers that he is a wizard and is invited to study at Hogwarts. Even as he escapes a dreary life and enters a world of magic, he finds trouble awaiting him.", "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg", Instant.parse("2021-01-01T10:30:00.00000Z"));
        product.getCategories().add(new Category(2L,"Livros"));
        return product;

    }
    public static Product createProductTestService() {
        Product product = new Product(null, "Harry Potter and the Philosopher's Stone", 50.0, "Harry Potter, an eleven-year-old orphan, discovers that he is a wizard and is invited to study at Hogwarts. Even as he escapes a dreary life and enters a world of magic, he finds trouble awaiting him.", "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg", Instant.parse("2021-01-01T10:30:00.00000Z"));
        product.getCategories().add(new Category(2L,"Livros"));
        return product;

    }

    public static ProductDTO createProductDTO(){

        Product product = createProduct();

        return new ProductDTO(product, product.getCategories());

    }
    public static ProductDTO createProductDTOTestService(){

        Product product = createProductTestService();

        return new ProductDTO(product, product.getCategories());

    }

}
