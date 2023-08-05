package ru.practicum.model.mapper;

import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category makeCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto makeCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static List<CategoryDto> makeCategoryDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categories)
            categoryDtoList.add(makeCategoryDto(category));
        return categoryDtoList;
    }
}
