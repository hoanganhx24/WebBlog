package com.example.webblog.service.Category;

import com.example.webblog.dto.request.CategoryCreateRequest;
import com.example.webblog.dto.request.CategoryUpdateRequest;
import com.example.webblog.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest request);
    void deleteCategoryById(String id);
    CategoryResponse updateInfo(String id, CategoryUpdateRequest request);
    Page<CategoryResponse> getCategories(String keyword, int page, int pageSize);
}
