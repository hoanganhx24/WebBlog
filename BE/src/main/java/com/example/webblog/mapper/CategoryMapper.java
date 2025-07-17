package com.example.webblog.mapper;

import com.example.webblog.dto.response.CategoryResponse;
import com.example.webblog.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse toCreateCategoryResponse(Category category) {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .categoryParent(category.getParent())
                .build();
        return categoryResponse;
    }

}
