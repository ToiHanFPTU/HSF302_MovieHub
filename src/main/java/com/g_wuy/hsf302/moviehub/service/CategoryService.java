package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    public Category addCategory(Category category);
    public void deleteCategory(Category category);
    public void updateCategory(Category category);
}
