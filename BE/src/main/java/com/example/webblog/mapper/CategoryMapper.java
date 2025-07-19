package com.example.webblog.mapper;

import com.example.webblog.dto.response.CategoryCreateResponse;
import com.example.webblog.dto.response.CategoryFilterResponse;
import com.example.webblog.dto.response.CategoryResponse;
import com.example.webblog.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "categoryParent", source = "parent")
    CategoryCreateResponse toCreateCategoryResponse(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    CategoryFilterResponse toCategoryFilterResponse(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    CategoryResponse toCategoryResponse(Category category);
}
