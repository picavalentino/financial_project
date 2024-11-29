package com.team.financial_project.schedule.service;

import com.team.financial_project.schedule.dto.CustomerScheduleDTO;
import com.team.financial_project.schedule.dto.ScheduleDTO;
import com.team.financial_project.schedule.mapper.CustomerScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
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
}
