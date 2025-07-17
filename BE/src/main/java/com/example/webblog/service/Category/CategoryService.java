package com.example.webblog.service.Category;

import com.example.webblog.dto.request.CreateCategoryRequest;
import com.example.webblog.dto.request.UpdateCategoryRequest;
import com.example.webblog.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    void deleteCategoryById(String id);
    CategoryResponse updateInfo(String id, UpdateCategoryRequest request);
}
