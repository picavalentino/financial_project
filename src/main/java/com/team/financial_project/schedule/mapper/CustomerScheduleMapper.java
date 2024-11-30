package com.team.financial_project.schedule.mapper;

import com.team.financial_project.schedule.dto.CustomerBirthdayDTO;
import com.team.financial_project.schedule.dto.CustomerMtrDtDTO;
import com.team.financial_project.schedule.dto.CustomerScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerScheduleMapper {
    List<CustomerScheduleDTO> getCalendarDetailsByUserId(@Param("userId") String userId);
    // 생일 정보 조회
    List<CustomerBirthdayDTO> findCustomerBirthdays(@Param("userId") String userId);

    // 만기일 정보 조회
    List<CustomerMtrDtDTO> findCustomerMaturityDates(@Param("userId") String userId);
}


