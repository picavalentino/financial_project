package com.team.financial_project.schedule.service;

import com.team.financial_project.schedule.dto.CustomerScheduleDTO;
import com.team.financial_project.schedule.dto.ScheduleDTO;
import com.team.financial_project.schedule.mapper.CustomerScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CustomerScheduleService {

    @Autowired
    CustomerScheduleMapper customerScheduleMapper;

    public List<CustomerScheduleDTO> getEventsByUserId(String userId) {
        List<CustomerScheduleDTO> scheduleList = customerScheduleMapper.getCalendarDetailsByUserId(userId);

        if (scheduleList != null && !scheduleList.isEmpty()) {
            for (CustomerScheduleDTO schedule : scheduleList) {
                schedule.setCustomerBirthdayDTO(customerScheduleMapper.findCustomerBirthdays(userId));
                schedule.setCustomerMtrDtDTO(customerScheduleMapper.findCustomerMaturityDates(userId));
            }
        }
        return scheduleList;
    }


    public void saveSchedule(CustomerScheduleDTO scheduleDTO) {
        customerScheduleMapper.insertSchedule(scheduleDTO); // 데이터베이스 저장 호출
    }
    public void deleteTask(Long calendarSn) {
        customerScheduleMapper.deleteTask(calendarSn);
    }

    public void updateCheckboxState(Long calendarSn, Boolean taskCheckedVal) {
        System.out.println("Updating Checkbox: calendarSn=" + calendarSn + ", checked=" + taskCheckedVal);
        customerScheduleMapper.updateCheckboxState(calendarSn, taskCheckedVal);
    }
}
