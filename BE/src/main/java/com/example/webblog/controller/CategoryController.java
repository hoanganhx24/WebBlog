package com.example.webblog.controller;

import com.example.webblog.dto.request.CreateCategoryRequest;
import com.example.webblog.dto.request.UpdateCategoryRequest;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.CategoryResponse;
import com.example.webblog.service.Category.CategoryService;
import com.example.webblog.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>>
    createCategory(@Valid @RequestBody CreateCategoryRequest req) {
        CategoryResponse categoryResponse = categoryService.createCategory(req);
        return ResponseHelper.created(categoryResponse, "Tao category thanh cong");
    }

    @DeleteMapping("/{idcategory}")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable("idcategory") String idcategory) {
        categoryService.deleteCategoryById(idcategory);
        return ResponseHelper.success("Delete category thanh cong");
    }

    @PatchMapping("/{categoryid}")
    public ResponseEntity<ApiResponse<Object>> updateCategory
            (@PathVariable("categoryid") String idcategory, @Valid @RequestBody UpdateCategoryRequest req) {
        CategoryResponse categoryResponse = categoryService.updateInfo(idcategory, req);
        return ResponseHelper.success(categoryResponse, "Update category thanh cong");
    }
}
