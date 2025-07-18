package com.example.webblog.mapper;

import com.example.webblog.dto.response.CategoryCreateResponse;
import com.example.webblog.dto.response.CategoryFilterResponse;
import com.example.webblog.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryCreateResponse toCreateCategoryResponse(Category category) {
        CategoryCreateResponse categoryCreateResponse = CategoryCreateResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .categoryParent(category.getParent())
                .build();
        return categoryCreateResponse;
    }

    public CategoryFilterResponse toCategoryFilterResponse(Category category) {
        CategoryFilterResponse categoryFilterResponse = CategoryFilterResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
        return categoryFilterResponse;
    }

}
