package com.example.webblog.service.Category;

import com.example.webblog.dto.request.CategoryCreateRequest;
import com.example.webblog.dto.request.CategoryUpdateRequest;
import com.example.webblog.dto.response.CategoryResponse;
import com.example.webblog.entity.Category;
import com.example.webblog.mapper.CategoryMapper;
import com.example.webblog.repository.CategoryRepository;
import com.example.webblog.repository.Specification.CategorySpecification;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final Slugify slugify = new Slugify();

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        String slug = slugify.slugify(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new EntityExistsException("Category with name " + slug + " already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .build();
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (Objects.isNull(category)) {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse updateInfo(String id, CategoryUpdateRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(()  -> new EntityNotFoundException("Category not found with id: " + id));
        String slug = slugify.slugify(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new EntityExistsException("Category with name " + slug + " already exists");
        }
        category.setName(request.getName());
        category.setSlug(slug);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public Page<CategoryResponse> getCategories(String keyword, int page, int pageSize) {
        Sort sort = Sort.unsorted();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<Category> spec = CategorySpecification.build(keyword);

        Page<Category>  pageResult = categoryRepository.findAll(spec, pageable);
        List<CategoryResponse> content = categoryMapper.toCategoryResponseList(pageResult.getContent());
        return new PageImpl<>(content, pageable, pageResult.getTotalElements());
    }
}
