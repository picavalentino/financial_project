package com.team.financial_project.mapper;

import com.team.financial_project.dto.InquireDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InquireMapper {
    void insertInquire(InquireDTO inquireDTO);

    @Select("SELECT user_name FROM tb_user WHERE user_id = #{userId}")
    String getUserName(String userId);

    List<InquireDTO> searchInquires(@Param("category") String category,
                                    @Param("keywordType") String keywordType,
                                    @Param("keyword") String keyword,
                                    @Param("createAt") String createAt,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);

    int countSearchInquires(@Param("category") String category,
                            @Param("keywordType") String keywordType,
                            @Param("keyword") String keyword,
                            @Param("createAt") String createAt);

    InquireDTO getInquireById(@Param("inqId") Long inqId);
}
