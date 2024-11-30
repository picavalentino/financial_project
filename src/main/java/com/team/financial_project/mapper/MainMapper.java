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

    List<Map<String, Object>> getTodayTasks(@Param("userId") String userId);

    void updateTaskChecked(@Param("calendarSn") Long calendarSn);

}
