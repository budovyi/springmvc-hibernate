package com.budovyy.service;

import com.budovyy.dao.CategoryDao;
import com.budovyy.model.Category;
import com.budovyy.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductService productService;

    @Override
    public List<Category> getAll() {
        List<Category> result = categoryDao.getAll();
        System.out.println();
        return result;
    }

    @Override
    public Category getById(Long id) {
        return categoryDao.getById(id);
    }

    @Override
    public Category reduceCategoryPriceByPercents(Category category, int percents) {
        List<Product> products = category.getProducts().stream()
                .map(p -> productService.reducePriceByPercents(p, percents))
                .collect(Collectors.toList());
        category.setProducts(products);
        return categoryDao.save(category);
    }
}
