package com.team.financial_project.schedule.service;

import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.schedule.dto.ScheduleDTO;
import com.team.financial_project.schedule.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;


    public List<ScheduleDTO> getEventsByUserId(String userId) {
        return scheduleMapper.selectEventsByUserId(userId);
    }
    public boolean addEvent(ScheduleDTO scheduleDTO) {
        return scheduleMapper.insertSchedule(scheduleDTO) > 0;
    }

    public boolean deleteEvent(int calendarId) {
        return scheduleMapper.deleteEvent(calendarId) > 0;
    }

    public boolean saveSchedule(ScheduleDTO scheduleDTO) {
        try {
            System.out.println("Saving Schedule: " + scheduleDTO);
            scheduleMapper.insertSchedule(scheduleDTO); // Mapper 호출
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
            return false;
        }

    }


    public void updateTaskStatus(int calendarSn, boolean taskCheckedVal) {
        scheduleMapper.updateTaskCheckedStatus(calendarSn, taskCheckedVal);
    }

    public boolean deleteSchedule(int calendarSn) {
        int result = scheduleMapper.deleteSchedule(calendarSn);
        return result > 0; // 삭제 성공 여부
    }
}
