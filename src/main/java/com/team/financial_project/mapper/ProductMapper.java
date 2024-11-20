package com.team.financial_project.mapper;

import com.team.financial_project.product.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductDTO> findAll();

    ProductDTO findById(Long prodSn);
}
