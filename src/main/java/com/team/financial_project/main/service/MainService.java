package com.team.financial_project.main.service;

import com.team.financial_project.dto.MainStatisticsAgeDTO;
import com.team.financial_project.dto.MainStatisticsGenderDTO;
import com.team.financial_project.dto.MainInquireDTO;
import com.team.financial_project.dto.MainStatisticsSalesDTO;
import com.team.financial_project.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MainService {
    @Autowired
    MainMapper mainMapper;

    public List<MainInquireDTO> mainInqireList() {
        return mainMapper.mainInqireList();
    }

    public List<MainStatisticsGenderDTO> mainCustomerList() {
        return mainMapper.mainCustomerList();
    }

    public List<MainStatisticsAgeDTO> getAgeStatistics() {
        return mainMapper.getAgeStatistics();
    }

    public List<MainStatisticsSalesDTO> getSalesStatistics(String userId) {
        System.out.println("Service received userId: " + userId);
        return mainMapper.getSalesStatistics(userId);
    }

    public List<Map<String, Object>> getTodayTasks(String userId) {
        return mainMapper.getTodayTasks(userId);
    }

    public void updateTaskChecked(Long calendarSn) {
        mainMapper.updateTaskChecked(calendarSn);
    }
}
