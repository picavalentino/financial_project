package com.team.financial_project.main.controller;

import com.team.financial_project.dto.MainInquireDTO;
import com.team.financial_project.main.service.ExchangeService;
import com.team.financial_project.main.service.MainService;
import com.team.financial_project.main.service.NewsService;
import com.team.financial_project.main.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    // test용
    @GetMapping("/test")
    public String test() {
        return "/financial/test";
    }

    // main
    @GetMapping("/main")
    public String main(Model model) {
        //로그인 했다 치고
        String userId = "20240706002";

        //공지사항 데이터 가져오기
        List<MainInquireDTO> mainInquireDTOList = mainService.mainInqireList();
        model.addAttribute("noticeList", mainInquireDTOList);

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
//         Map<String, Object> kospiData = stockService.getKospiData();
//        String kospiData = stockService.getKospiData();

        return "/financial/main";
    }

}
