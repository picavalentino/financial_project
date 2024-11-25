package com.team.financial_project.promotion.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromoCalMapper {
    String findDsTyCd(Long dsgnSn);
}
