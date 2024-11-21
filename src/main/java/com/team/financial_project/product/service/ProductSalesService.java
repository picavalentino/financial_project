package com.team.financial_project.product.service;

import com.team.financial_project.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductSalesService {
    private final ProductMapper productMapper;

    public ProductSalesService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }
}
