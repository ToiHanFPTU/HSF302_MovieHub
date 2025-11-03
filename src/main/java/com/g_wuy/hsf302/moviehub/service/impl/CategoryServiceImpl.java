package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Category;
import com.g_wuy.hsf302.moviehub.repository.CategoryRepository;
import com.g_wuy.hsf302.moviehub.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Category category) {
        Category existCate = categoryRepository.findById(category.getId()).orElse(null);
        if (existCate != null) {
            categoryRepository.delete(existCate);
        }
    }

    @Override
    public void updateCategory(Category category) {
        Category existCate = categoryRepository.findById(category.getId()).orElse(null);
        if (existCate != null) {
            existCate.setCategoryName(category.getCategoryName());
            categoryRepository.save(existCate);
        }
    }
}
