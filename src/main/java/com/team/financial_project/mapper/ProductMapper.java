package com.team.financial_project.mapper;

import com.team.financial_project.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<ProductDTO> findAll();

    ProductDTO findById(Long prodSn);

    void updateProduct(ProductDTO dto);

    void deleteProduct(BigDecimal prodSn);

    void insertProduct(ProductDTO dto);

    int findCdSizeByName(@Param("keyword") String keyword);

    List<ProductDTO> searchProducts(Map<String, Object> searchParams);
}
