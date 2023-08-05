package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.Category;
import ru.practicum.repository.category.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;

import static ru.practicum.model.mapper.CategoryMapper.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(makeCategory(categoryDto));

        return makeCategoryDto(savedCategory);
    }

    @Transactional
    public CategoryDto updateCategories(Long categoryId, CategoryDto request) {
        Category category = getCategoryByIdIfExist(categoryId);

        category.setName(request.getName());

        return makeCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategories(Long categoryId) {
        Category category = getCategoryByIdIfExist(categoryId);
        categoryRepository.delete(category);
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAll(PageRequest.of(from, size)).getContent();

        return makeCategoryDtoList(categories);
    }

    public CategoryDto getCategoriesId(Long categoryId) {
        Category category = getCategoryByIdIfExist(categoryId);

        return makeCategoryDto(category);
    }

    public Category getCategoryByIdIfExist(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Категория с id='%s' не найдена", categoryId)));
    }
}
