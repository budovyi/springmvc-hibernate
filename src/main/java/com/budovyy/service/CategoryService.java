package com.budovyy.service;

import com.budovyy.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(Long id);

    Category reduceCategoryPriceByPercents(Category category, int percents);
}

