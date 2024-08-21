package com.lino.dscatalog.factory;

import com.lino.dscatalog.dto.CategoryDTO;
import com.lino.dscatalog.entities.Category;

public class CategoryFactory {

    public static Category createCategory() {
        Category category = new Category(null, "Livros");
        return category;

    }

    public static CategoryDTO createCategoryDTO() {

        Category category = createCategory();

        return new CategoryDTO(category);

    }

}
