package com.lino.dscatalog.dto;

import com.lino.dscatalog.entities.Category;
import com.lino.dscatalog.entities.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "Campo obrigatório")
    @Size(min = 3, max = 20, message = "o nome deve ter entre 3 e 20 caracteres")
    private String name;
    private String description;
    @Positive(message = "O Preço deve ser um valor positivo")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "A data não pode ser futura")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {

    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);//Esta chamando o contrutor da linha 26 para preencher os dados
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));//vai percorrer a lista de categories da lista na linha 21 add a lista desta linha
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

}
