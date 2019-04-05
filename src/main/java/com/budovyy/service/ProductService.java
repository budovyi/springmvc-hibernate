package com.budovyy.service;

import com.budovyy.model.Product;

public interface ProductService {

    Product reducePriceByPercents(Product product, int percents);

}
