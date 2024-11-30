package com.team.financial_project.mapper;

import com.team.financial_project.dto.InquireCommentDTO;
import com.team.financial_project.dto.InquireDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquireMapper {
    // 게시글 CRUD
    List<InquireDTO> findAllList();

    InquireDTO findById(Long inqId);

    @Select("SELECT user_name FROM tb_user WHERE user_id = #{userId}")
    String getUserName(String userId);

    List<InquireDTO> searchInquiresAll(String inqCategory, String keywordType, String keyword, String inqCreateAt);

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

    InquireDTO getInquireById(@Param("inqId") Integer inqId);

    // userId를 userName으로 바꿔서 가져옴
    InquireDTO findById(@Param("inqId")Integer inqId);

    void incrementInqCheck(Integer inqId);

    void insertInquire(InquireDTO inquireDTO);

    void updateNoticeStatus(@Param("inqId") Integer inqId, @Param("status") String status);

    void deleteInquire(Integer inqId);

    // 댓글 CRUD
    List<InquireCommentDTO> getCommentsByInqId(Integer inqId);

    // user_auth_cd 조회 쿼리
    @Select("SELECT user_auth_cd FROM tb_user WHERE user_id = #{userId}")
    String getUserAuthCdByUserId(@Param("userId") String userId);

    void updateInqReply(Integer inqId, String inqReply);

    void insertComment(InquireCommentDTO commentDTO);

    void updateInquire(InquireDTO inquireDTO);

    int countAllList();
}