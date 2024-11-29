package com.team.financial_project.schedule.mapper;

import com.team.financial_project.schedule.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<ScheduleDTO> selectEventsByUserId(String userId);

    int deleteEvent(int calendarId);

    int insertSchedule(ScheduleDTO scheduleDTO);


    void updateTaskCheckedStatus(@Param("calendarSn") int calendarSn, @Param("taskCheckedVal") boolean taskCheckedVal);

    int deleteSchedule(int calendarSn);
}
