package com.team.financial_project.main.controller;

import com.team.financial_project.dto.MainStatisticsAgeDTO;
import com.team.financial_project.dto.MainStatisticsGenderDTO;
import com.team.financial_project.dto.MainStatisticsSalesDTO;
import com.team.financial_project.main.service.MainService;
import com.team.financial_project.main.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController // JSON 반환을 위한 RestController 추가
@Controller
public class StatisticsController {
    @Autowired
    private MainService mainService;

    @Autowired
    private StockService stockService;

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

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
        String userId = getAuthenticatedUserId(); // 인증된 사용자 ID 가져오기
        return mainService.getSalesStatistics(userId);
    }

    //코스피 지수
    @GetMapping("/api/kospi-statistics") // 데이터 반환 엔드포인트
    public List<Map<String, String>> getKospiData() {
        String kospiRawData = stockService.getKospiData();
        return stockService.parseKospiData(kospiRawData);
    }

    //코스닥 지수
    @GetMapping("/api/kosdaq-statistics") // 데이터 반환 엔드포인트
    public List<Map<String, String>> getKosdaqData() {
        String kosdaqRawData = stockService.getKosdaqData();
        return stockService.parseKosdaqData(kosdaqRawData);
    }
}
