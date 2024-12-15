package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.NewCategoryDto;
import org.example.model.Category;

import java.util.Optional;

@UtilityClass
public class CategoryMapper {
    public static Category modelFromNewCategoryDto(NewCategoryDto newCategory) {
        return Category.builder()
                .name(newCategory.getName())
                .build();
    }

    public static CategoryDto modelToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category updateCategory(Category category, CategoryDto updateCategory) {
        category.setName(Optional.ofNullable(updateCategory.getName())
                .filter(name -> !name.isBlank()).orElse(category.getName()));

        return category;
    }

}
