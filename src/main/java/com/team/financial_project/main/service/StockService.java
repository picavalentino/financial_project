package com.team.financial_project.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public String getKospiData() {

        String serviceKey = "2ub9G6Mcp0vPeMxueHx0SjuMkNYIEa/243mpXVSxUhLSU15JRQuzBtAi9XbKucskCUYqNQNOZZAmPndgPkK3xA==";

        String url = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex"
                + "?serviceKey=" + serviceKey // 여기서 다시 인코딩
                + "&resultType=json"
                + "&numOfRows=30"
                + "&idxNm=코스피";
        log.info("Generated URL: {}", url);
        // HTTP 요청
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        log.info("Response Headers: {}", response.getHeaders());
        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());

        log.info("Response : {}", response);

        return response.getBody();
    }

    public List<Map<String, String>> parseKospiData(String responseBody) {
        List<Map<String, String>> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            for (JsonNode item : itemsNode) {
                Map<String, String> data = new HashMap<>();
                data.put("basDt", item.path("basDt").asText()); // 기준 날짜
                data.put("clpr", item.path("clpr").asText());   // 체결가
                data.put("vs", item.path("vs").asText());       // 전일비
                data.put("fltRt", item.path("fltRt").asText()); // 등락률
                result.add(data);
            }
        } catch (Exception e) {
            log.error("Error parsing KOSPI data", e);
        }

        return result;
    }

    //코스닥
    public String getKosdaqData() {

        String serviceKey = "2ub9G6Mcp0vPeMxueHx0SjuMkNYIEa/243mpXVSxUhLSU15JRQuzBtAi9XbKucskCUYqNQNOZZAmPndgPkK3xA==";

        String url = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex"
                + "?serviceKey=" + serviceKey // 여기서 다시 인코딩
                + "&resultType=json"
                + "&numOfRows=30"
                + "&idxNm=코스닥";
        log.info("Generated URL: {}", url);
        // HTTP 요청
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        log.info("Response : {}", response);

        return response.getBody();
    }

    public List<Map<String, String>> parseKosdaqData(String responseBody) {
        List<Map<String, String>> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            for (JsonNode item : itemsNode) {
                Map<String, String> data = new HashMap<>();
                data.put("basDt", item.path("basDt").asText()); // 기준 날짜
                data.put("clpr", item.path("clpr").asText());   // 체결가
                data.put("vs", item.path("vs").asText());       // 전일비
                data.put("fltRt", item.path("fltRt").asText()); // 등락률
                result.add(data);
            }
        } catch (Exception e) {
            log.error("Error parsing KOSDAQ data", e);
        }

        return result;
    }


}


