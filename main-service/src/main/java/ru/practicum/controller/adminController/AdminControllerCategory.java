package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.serviceImpl.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminControllerCategory {
    private final CategoryService adminServiceCategory;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategories(@RequestBody @Valid CategoryDto categoryDto) {
        return adminServiceCategory.addCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategories(@PathVariable(name = "catId") @Positive Long categoryId,
                                        @RequestBody @Valid CategoryDto categoryDto) {
        return adminServiceCategory.updateCategories(categoryId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategories(@PathVariable(name = "catId") @Positive Long catId) {
        adminServiceCategory.deleteCategories(catId);
    }
}
