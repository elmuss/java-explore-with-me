package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.NewCategoryDto;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.mapper.CategoryMapper;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.service.dao.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private static final String CATEGORY_NOT_FOUND_MSG = "Category with id=%d was not found";
    private static final String CATEGORY_NOT_UNIQUE_MSG = "Category not unique";
    private static final String CATEGORY_NAME_EMPTY_MSG = "Category name should not be empty";
    private static final String CATEGORY_NAME_LENGTH_MSG = "Category name should not be longer than 50 symbols";

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategory) {
        validateCategory(newCategory);
        Category category = CategoryMapper.modelFromNewCategoryDto(newCategory);
        List<Category> categories = categoryRepository.findAll();

        for (Category c : categories) {
            if (category.getName().equals(c)) {
                throw new ValidationException(CATEGORY_NOT_UNIQUE_MSG);
            }
        }
        return CategoryMapper.modelToCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(int catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MSG, catId)));

        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategoriesByParam(Integer from, Integer size) {
        return categoryRepository.findAll().stream().map(CategoryMapper::modelToCategoryDto).limit(size).toList();
    }

    @Override
    public CategoryDto getCategoryById(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MSG, catId)));

        return CategoryMapper.modelToCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(int catId, CategoryDto updateCategory) {
        validateUpdateCategory(updateCategory);
        Category oldCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MSG, catId)));
        Category newCategory = CategoryMapper.updateCategory(oldCategory, updateCategory);
        return CategoryMapper.modelToCategoryDto(categoryRepository.save(newCategory));
    }

    public void validateCategory(NewCategoryDto newCategory) {
        if (newCategory.getName() == null) {
            throw new ValidationException(CATEGORY_NAME_EMPTY_MSG);
        } else {
            if (newCategory.getName().isEmpty() || newCategory.getName().isBlank()) {
                throw new ValidationException(CATEGORY_NAME_EMPTY_MSG);
            }
            if (newCategory.getName().length() > 50) {
                throw new ValidationException(CATEGORY_NAME_LENGTH_MSG);
            }
        }
    }

    public void validateUpdateCategory(CategoryDto updateCategory) {
        if (updateCategory.getName().length() > 50) {
            throw new ValidationException(CATEGORY_NAME_LENGTH_MSG);
        }
    }
}
