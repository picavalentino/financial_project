package com.team.financial_project.main.controller;

import com.team.financial_project.dto.MainStatisticsAgeDTO;
import com.team.financial_project.dto.MainStatisticsGenderDTO;
import com.team.financial_project.dto.MainStatisticsSalesDTO;
import com.team.financial_project.main.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // JSON 반환을 위한 RestController 추가
@Controller
public class StatisticsController {
    @Autowired
    private MainService mainService;

    //고객 성별 분석
    @GetMapping("/api/gender-statistics") // 데이터 반환 엔드포인트
    public List<MainStatisticsGenderDTO> getCustomerStatistics() {
        return mainService.mainCustomerList();
    }

    //고객 연령 분석
    @GetMapping("/api/age-statistics") // 데이터 반환 엔드포인트
    public List<MainStatisticsAgeDTO> getAgeStatistics() {
        return mainService.getAgeStatistics();
    }

    //나의 월별 매출 건 수 분석
    @GetMapping("/api/sales-statistics") // 데이터 반환 엔드포인트
    public List<MainStatisticsSalesDTO> getSalesStatistics() {
        //로그인 했다 치고
        String userId="20240706002";
        return mainService.getSalesStatistics(userId);
    }
}
