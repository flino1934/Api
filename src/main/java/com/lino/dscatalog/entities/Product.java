package com.lino.dscatalog.entities;

import com.lino.dscatalog.dto.ProductDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_category")
public class Product implements Serializable {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imgUrl;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant date;

    public Product(Long id, String name, Double price, String description, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public Product(ProductDTO productDTO) {
        this.id = productDTO.getId();
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.description = productDTO.getDescription();
        this.imgUrl = productDTO.getImgUrl();
    }

    public Product() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
