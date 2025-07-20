package com.example.webblog.service.Category;

import com.example.webblog.dto.request.CategoryCreateRequest;
import com.example.webblog.dto.request.CategoryFilterRequest;
import com.example.webblog.dto.request.CategoryUpdateRequest;
import com.example.webblog.dto.response.CategoryCreateResponse;
import com.example.webblog.dto.response.CategoryFilterResponse;
import com.example.webblog.dto.response.PageResponse;
import com.example.webblog.entity.Category;
import com.example.webblog.mapper.CategoryMapper;
import com.example.webblog.repository.CategoryRepository;
import com.example.webblog.repository.Specification.CategorySpecification;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    private final Slugify slugify = new Slugify();

    @Override
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
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
    public CategoryCreateResponse updateInfo(String id, CategoryUpdateRequest request) {
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

    @Override
    public PageResponse<CategoryFilterResponse> getCategories(CategoryFilterRequest request, int page, int pageSize) {
        Sort sort = Sort.unsorted();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<Category> spec = CategorySpecification.build(request);

        Page<Category>  pageResult = categoryRepository.findAll(spec, pageable);
        List<CategoryFilterResponse> content = pageResult.getContent()
                .stream()
                .map(category -> categoryMapper.toCategoryFilterResponse(category))
                .toList();
        for (CategoryFilterResponse categoryFilterResponse : content) {
            log.info("categoryFilterResponse: {}", categoryFilterResponse.toString());
        }
        return PageResponse.<CategoryFilterResponse>builder()
                .content(content)
                .page(page)
                .size(pageSize)
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();
    }
}
