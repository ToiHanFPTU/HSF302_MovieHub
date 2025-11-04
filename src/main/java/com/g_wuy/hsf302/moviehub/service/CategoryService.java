package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    List<Category> getCategoriesByIds(List<Integer> ids);
    Category saveCategory(Category category);
    void deleteCategory(Integer id);
}
