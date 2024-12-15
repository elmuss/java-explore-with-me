package org.example.service.dao;

import org.example.dto.category.CategoryDto;
import org.example.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategory);

    void deleteCategory(int catId);

    List<CategoryDto> getCategoriesByParam(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId);

    CategoryDto updateCategory(int catId, CategoryDto updateCategory);
}
