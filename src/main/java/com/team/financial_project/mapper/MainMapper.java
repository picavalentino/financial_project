package com.team.financial_project.mapper;

import com.team.financial_project.dto.MainStatisticsAgeDTO;
import com.team.financial_project.dto.MainStatisticsGenderDTO;
import com.team.financial_project.dto.MainInquireDTO;
import com.team.financial_project.dto.MainStatisticsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MainMapper {
    List<MainInquireDTO> mainInqireList();

    List<MainStatisticsGenderDTO> mainCustomerList();

    List<MainStatisticsAgeDTO> getAgeStatistics();

    List<MainStatisticsSalesDTO> getSalesStatistics(@Param("userId") String userId);

}
