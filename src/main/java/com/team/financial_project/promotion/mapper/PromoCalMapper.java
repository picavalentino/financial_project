package com.team.financial_project.promotion.mapper;

import com.team.financial_project.promotion.dto.ProductInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromoCalMapper {
    String findDsTyCd(Long dsgnSn);

    List<ProductInfoDto> getAcmlProductList();

    List<ProductInfoDto> getDpstProductList();

    List<ProductInfoDto> getLoanProductList();
}
