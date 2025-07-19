package com.example.webblog.controller;

import com.example.webblog.dto.request.CategoryCreateRequest;
import com.example.webblog.dto.request.CategoryFilterRequest;
import com.example.webblog.dto.request.CategoryUpdateRequest;
import com.example.webblog.dto.response.*;
import com.example.webblog.service.Category.CategoryService;
import com.example.webblog.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryCreateResponse>>
    createCategory(@Valid @RequestBody CategoryCreateRequest req) {
        CategoryCreateResponse categoryCreateResponse = categoryService.createCategory(req);
        return ResponseHelper.created(categoryCreateResponse, "Tao category thanh cong");
    }

    @DeleteMapping("/{idcategory}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable("idcategory") String idcategory) {
        categoryService.deleteCategoryById(idcategory);
        return ResponseHelper.success("Delete category thanh cong");
    }

    @PatchMapping("/{categoryid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> updateCategory
            (@PathVariable("categoryid") String idcategory, @Valid @RequestBody CategoryUpdateRequest req) {
        CategoryCreateResponse categoryCreateResponse = categoryService.updateInfo(idcategory, req);
        return ResponseHelper.success(categoryCreateResponse, "Update category thanh cong");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryFilterResponse>>> getAllCategories(@ModelAttribute CategoryFilterRequest request) {
        return ResponseHelper.success(categoryService.getCategories(request), "Lay thanh cong danh sach category");
    }
}
