package com.team.financial_project.schedule.mapper;

import com.team.financial_project.schedule.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<ScheduleDTO> selectEventsByUserId(String userId);

    int deleteEvent(int calendarId);

    int insertSchedule(ScheduleDTO scheduleDTO);


    int updateTaskCheckedStatus(ScheduleDTO scheduleDTO);
}
