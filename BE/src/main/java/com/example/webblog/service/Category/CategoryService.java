package com.example.webblog.service.Category;

import com.example.webblog.dto.request.CategoryCreateRequest;
import com.example.webblog.dto.request.CategoryFilterRequest;
import com.example.webblog.dto.request.CategoryUpdateRequest;
import com.example.webblog.dto.response.CategoryCreateResponse;
import com.example.webblog.dto.response.CategoryFilterResponse;
import com.example.webblog.dto.response.PageResponse;

public interface CategoryService {
    CategoryCreateResponse createCategory(CategoryCreateRequest request);
    void deleteCategoryById(String id);
    CategoryCreateResponse updateInfo(String id, CategoryUpdateRequest request);
    PageResponse<CategoryFilterResponse> getCategories(CategoryFilterRequest request, int page, int pageSize);
}
