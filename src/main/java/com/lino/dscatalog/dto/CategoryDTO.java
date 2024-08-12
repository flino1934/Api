package com.lino.dscatalog.dto;

import com.lino.dscatalog.entities.Category;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private Long id;
    private String name;


    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category category) {
        this.name = category.getName();
        this.id = category.getId();
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
}
