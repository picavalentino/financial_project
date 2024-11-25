package com.team.financial_project.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StockService {
    private static final String API_URL = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex";
    private static final String API_KEY = "6BnHP4eBbX5qrTTDAdmIkuNZupmDr7m9lNMhjNyIP3Opuswbm8xVjaKyu5hw3GTXZQ6BD0emIyPkbIPdMM2e6A%3D%3D";

    /*
    public Map<String, Object> getKospiData() {
        String url = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex?serviceKey=6BnHP4eBbX5qrTTDAdmIkuNZupmDr7m9lNMhjNyIP3Opuswbm8xVjaKyu5hw3GTXZQ6BD0emIyPkbIPdMM2e6A%3D%3D&resultType=json&numOfRows=10&idxNm=%EC%BD%94%EC%8A%A4%ED%94%BC";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        log.info("API Response: {}", response); // JSON 원본 로그 출력
        return response;
    }*/

    public String getKospiData() {
        //String baseURL = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex";
        //String serviceKey = "2ub9G6Mcp0vPeMxueHx0SjuMkNYIEa/243mpXVSxUhLSU15JRQuzBtAi9XbKucskCUYqNQNOZZAmPndgPkK3xA==";

        // URL 인코딩
        //String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        //String encodedIdxNm = URLEncoder.encode("코스피", StandardCharsets.UTF_8);

        // 완성된 URL
        //String url = baseURL + "?serviceKey=" + encodedServiceKey + "&resultType=json" + "&numOfRows=10" + "&idxNm=" + encodedIdxNm;

        //log.info("Generated URL: {}", url);

        String serviceKey = "2ub9G6Mcp0vPeMxueHx0SjuMkNYIEa/243mpXVSxUhLSU15JRQuzBtAi9XbKucskCUYqNQNOZZAmPndgPkK3xA==";

        String url = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex"
                + "?serviceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) // 여기서 다시 인코딩
                + "&resultType=json"
                + "&numOfRows=10"
                + "&idxNm=%EC%BD%94%EC%8A%A4%ED%94%BC";
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


}

