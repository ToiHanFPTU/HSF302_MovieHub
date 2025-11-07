package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Category;
import com.g_wuy.hsf302.moviehub.repository.CategoryRepository;
import com.g_wuy.hsf302.moviehub.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private  CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


}
