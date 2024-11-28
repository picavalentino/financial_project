package com.team.financial_project.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExchangeService {
    private static final String API_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";
    private static final String API_KEY = "FPVOMrDhR7LFek9F9ZRCcqA55baes1F6";

    public Map<String, Map<String, Object>> getExchangeRates(String today, String yesterday) {
        RestTemplate restTemplate = new RestTemplate();

        // 오늘 환율 정보 요청
        String todayUrl = API_URL + "?authkey=" + API_KEY + "&searchdate=" + today + "&data=AP01";
        List<Map<String, Object>> todayRates = null;
        try {
            todayRates = restTemplate.getForObject(todayUrl, List.class);
        } catch (Exception e) {
            todayRates = null; // 에러 발생 시 null 처리
        }

        // 어제 환율 정보 요청
        String yesterdayUrl = API_URL + "?authkey=" + API_KEY + "&searchdate=" + yesterday + "&data=AP01";
        List<Map<String, Object>> yesterdayRates = null;
        try {
            yesterdayRates = restTemplate.getForObject(yesterdayUrl, List.class);
        } catch (Exception e) {
            yesterdayRates = null; // 에러 발생 시 null 처리
        }

        // 결과 데이터 매핑
        Map<String, Map<String, Object>> exchangeData = new HashMap<>();
        String[] currencies = {"USD", "JPY(100)", "EUR"};

        for (String currency : currencies) {
            Map<String, Object> todayRate = todayRates != null
                    ? todayRates.stream()
                    .filter(rate -> currency.equals(rate.get("cur_unit")))
                    .findFirst()
                    .orElse(null)
                    : null;

            Map<String, Object> yesterdayRate = yesterdayRates != null
                    ? yesterdayRates.stream()
                    .filter(rate -> currency.equals(rate.get("cur_unit")))
                    .findFirst()
                    .orElse(null)
                    : null;

            BigDecimal todayValue = BigDecimal.ZERO;
            BigDecimal yesterdayValue = BigDecimal.ZERO;
            BigDecimal change = BigDecimal.ZERO;
            BigDecimal changePercentage = BigDecimal.ZERO;

            if (todayRate != null && yesterdayRate != null) {
                todayValue = new BigDecimal(todayRate.get("deal_bas_r").toString().replace(",", ""));
                yesterdayValue = new BigDecimal(yesterdayRate.get("deal_bas_r").toString().replace(",", ""));
                change = todayValue.subtract(yesterdayValue);

                if (yesterdayValue.compareTo(BigDecimal.ZERO) != 0) {
                    changePercentage = change.divide(yesterdayValue, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP);
                }
            }

            // null 또는 비정상 값일 경우 기본값(0) 설정
            Map<String, Object> data = new HashMap<>();
            data.put("deal_bas_r", todayValue);
            data.put("change", change);
            data.put("change_percentage", changePercentage);

            exchangeData.put(currency, data);
        }

        return exchangeData;
    }
}
