package com.team.financial_project.product.service;

import com.team.financial_project.mapper.ProductMapper;
import com.team.financial_project.product.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    public List<ProductDTO> findAll() {
        List<ProductDTO> list = productMapper.findAll();
        log.info("product list: " + list);
        return list;
    }

    public ProductDTO findById(Long prodSn) {
        return productMapper.findById(prodSn);
    }

    public void updateProduct(ProductDTO dto) {
        productMapper.updateProduct(dto);
    }

    public void deleteProduct(BigDecimal prodSn) {
        productMapper.deleteProduct(prodSn);
    }
}
