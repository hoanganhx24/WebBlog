package com.example.webblog.service.Category;

import com.example.webblog.dto.request.CreateCategoryRequest;
import com.example.webblog.dto.request.UpdateCategoryRequest;
import com.example.webblog.dto.response.CategoryResponse;
import com.example.webblog.entity.Category;
import com.example.webblog.mapper.CategoryMapper;
import com.example.webblog.repository.CategoryRepository;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    private final Slugify slugify = new Slugify();

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        String slug = slugify.slugify(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new EntityExistsException("Category with name " + slug + " already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .build();
        if(request.getParentId() != null){
            var categoryParent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getParentId()));
            category.setParent(categoryParent);
        }
        return categoryMapper.toCreateCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategoryById(String id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(()  -> new EntityNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse updateInfo(String id, UpdateCategoryRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(()  -> new EntityNotFoundException("Category not found with id: " + id));
        String slug = slugify.slugify(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new EntityExistsException("Category with name " + slug + " already exists");
        }
        category.setName(request.getName());
        category.setSlug(slug);
        return categoryMapper.toCreateCategoryResponse(categoryRepository.save(category));
    }
}
