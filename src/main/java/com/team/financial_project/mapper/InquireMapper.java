package com.team.financial_project.mapper;

import com.team.financial_project.dto.InquireDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InquireMapper {
    List<InquireDTO> findAllList();
    void insertInquire(InquireDTO inquireDTO);

    InquireDTO findById(Long inqId);
    @Select("SELECT user_name FROM tb_user WHERE user_id = #{userId}")
    String getUserName(String userId);
}
