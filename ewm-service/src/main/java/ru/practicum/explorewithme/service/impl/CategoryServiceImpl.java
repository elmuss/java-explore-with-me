package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.service.dao.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private static final Integer CATEGORY_NAME_MAX_LENGTH = 50;
    private static final String CATEGORY_NOT_FOUND_MSG = "Category with id=%d was not found";
    private static final String CATEGORY_NAME_EMPTY_MSG = "Category name should not be empty";
    private static final String CATEGORY_NAME_LENGTH_MSG = "Category name should not be longer than 50 symbols";

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategory) {
        validateCategory(newCategory);
        Category category = CategoryMapper.modelFromNewCategoryDto(newCategory);

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
        return categoryRepository.findAll(PageRequest.of(from, size)).stream()
                .map(CategoryMapper::modelToCategoryDto)
                .toList();
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
            if (newCategory.getName().length() > CATEGORY_NAME_MAX_LENGTH) {
                throw new ValidationException(CATEGORY_NAME_LENGTH_MSG);
            }
        }
    }

    public void validateUpdateCategory(CategoryDto updateCategory) {
        if (updateCategory.getName().length() > CATEGORY_NAME_MAX_LENGTH) {
            throw new ValidationException(CATEGORY_NAME_LENGTH_MSG);
        }
    }
}
