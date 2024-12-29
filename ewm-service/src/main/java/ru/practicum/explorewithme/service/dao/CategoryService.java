package ru.practicum.explorewithme.service.dao;

import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategory);

    void deleteCategory(int catId);

    List<CategoryDto> getCategoriesByParam(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId);

    CategoryDto updateCategory(int catId, CategoryDto updateCategory);
}
