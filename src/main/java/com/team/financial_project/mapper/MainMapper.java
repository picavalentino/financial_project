package com.team.financial_project.mapper;

import com.team.financial_project.dto.MainStatisticsAgeDTO;
import com.team.financial_project.dto.MainStatisticsGenderDTO;
import com.team.financial_project.dto.MainInquireDTO;
import com.team.financial_project.dto.MainStatisticsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MainMapper {
    List<MainInquireDTO> mainInqireList();

    List<MainStatisticsGenderDTO> mainCustomerList();

    List<MainStatisticsAgeDTO> getAgeStatistics();

    List<MainStatisticsSalesDTO> getSalesStatistics(@Param("userId") String userId);

    // 사용자 정보 조회
    @Select("SELECT user_imgpath, user_name, user_jbps_ty_cd FROM tb_user WHERE user_id = #{userId}")
    Map<String, String> findUserById(@Param("userId") String userId);

    // 직위 이름 조회
    @Select("SELECT code_nm FROM tb_code WHERE code_cl = #{codeCl} AND code_no = #{codeNo}")
    String findCodeNameByCodeClAndCodeNo(@Param("codeCl") String codeCl, @Param("codeNo") String codeNo);
}
