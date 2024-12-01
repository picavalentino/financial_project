package com.team.financial_project.main.controller;

import com.team.financial_project.dto.MainInquireDTO;
import com.team.financial_project.main.service.ExchangeService;
import com.team.financial_project.main.service.MainService;
import com.team.financial_project.main.service.NewsService;
import com.team.financial_project.main.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MainController {
    @Autowired
    MainService mainService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private StockService stockService;

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

    // main
    @GetMapping("/main")
    public String main(Model model) {
        //로그인
        String userId = getAuthenticatedUserId(); // 인증된 사용자 ID 가져오기

        //공지사항 데이터 가져오기
        List<MainInquireDTO> mainInquireDTOList = mainService.mainInqireList();
        model.addAttribute("noticeList", mainInquireDTOList);

        //오늘 할 일
        List<Map<String, Object>> todayTasks = mainService.getTodayTasks(userId); // 오늘의 할 일 가져오기
        log.info("오늘할일:"+todayTasks.toString());
        model.addAttribute("todayTasks", todayTasks);

        // 경제 뉴스 데이터 가져오기
        //네이버 경제 뉴스 카테고리 페이지
        String categoryUrl = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101";
        List<Map<String, String>> newsList = newsService.fetchTop3News(categoryUrl);
        model.addAttribute("newsList", newsList);

        // 환율 데이터 가져오기
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Map<String, Map<String, Object>> exchangeRates = exchangeService.getExchangeRates(today, yesterday);
        model.addAttribute("exchangeRates", exchangeRates);

        // KOSPI 데이터 가져오기
//        String kosdaqRawData = stockService.getKosdaqData();
//        List<Map<String, String>> kosdaqData = stockService.parseKosdaqData(kosdaqRawData);
//        log.info("파싱 데이터: "+kosdaqData.toString());
//        // 데이터를 모델에 추가
//        model.addAttribute("kospiData", kospiData);


        return "financial/main";
    }

    @PostMapping("/updateTask")
    @ResponseBody
    public String updateTask(@RequestParam("calendarSn") Long calendarSn) {
        mainService.updateTaskChecked(calendarSn);
        return "success";
    }

    @GetMapping("/api/today-tasks")
    @ResponseBody
    public List<Map<String, Object>> getTodayTasks() {
        String userId = getAuthenticatedUserId(); // 현재 로그인한 사용자 ID
        return mainService.getTodayTasks(userId);
    }

}
