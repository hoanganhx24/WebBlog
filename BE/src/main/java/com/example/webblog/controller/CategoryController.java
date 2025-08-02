package com.example.webblog.controller;

import com.example.webblog.dto.request.CategoryCreateRequest;
import com.example.webblog.dto.request.CategoryUpdateRequest;
import com.example.webblog.dto.response.*;
import com.example.webblog.service.Category.CategoryService;
import com.example.webblog.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>>
    createCategory(@Valid @RequestBody CategoryCreateRequest req) {
        CategoryResponse categoryResponse = categoryService.createCategory(req);
        return ResponseHelper.created(categoryResponse, "Tao category thanh cong");
    }

    @DeleteMapping("/{idcategory}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable("idcategory") String idcategory) {
        categoryService.deleteCategoryById(idcategory);
        return ResponseHelper.success("Delete category thanh cong");
    }

    @PatchMapping("/{categoryid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory
            (@PathVariable("categoryid") String idcategory, @Valid @RequestBody CategoryUpdateRequest req) {
        CategoryResponse categoryResponse = categoryService.updateInfo(idcategory, req);
        return ResponseHelper.success(categoryResponse, "Update category thanh cong");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int pageSize

    ) {
        return ResponseHelper.ofPage(categoryService.getCategories(name, page, pageSize));
    }
}
