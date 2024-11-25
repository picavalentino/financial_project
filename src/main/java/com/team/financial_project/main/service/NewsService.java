package com.team.financial_project.main.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NewsService {
    public List<Map<String, String>> fetchTop3News(String categoryUrl) {
        try {
            // 네이버 뉴스 HTML 가져오기
            Document doc = Jsoup.connect(categoryUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            // ul 태그의 id가 '_SECTION_HEADLINE_LIST_'로 시작하는 요소 찾기
            Element list = doc.selectFirst("ul[id^=_SECTION_HEADLINE_LIST_]");
            if (list == null) {
                throw new IOException("뉴스 목록을 찾을 수 없습니다.");
            }

            // 상단 3개의 <li> 추출
            Elements items = new Elements(list.select("li.sa_item._SECTION_HEADLINE").subList(0, Math.min(3, list.select("li.sa_item._SECTION_HEADLINE").size())));

            List<Map<String, String>> newsList = new ArrayList<>();

            for (Element item : items) {
                Map<String, String> news = new HashMap<>();

                // link 추출
                Element linkElement = item.selectFirst("div.sa_thumb_inner a");
                String link = linkElement != null ? linkElement.attr("href") : "";

                // img-link 추출
                Element imgElement = linkElement != null ? linkElement.selectFirst("img") : null;
                String imgLink = imgElement != null ? imgElement.attr("data-src").split("\\?")[0] : "";

                // title 추출
                String title = imgElement != null ? imgElement.attr("alt") : "";

                // 데이터 저장
                news.put("link", link);
                news.put("img-link", imgLink);
                news.put("title", title);

                newsList.add(news);
            }

            return newsList;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("크롤링 실패: " + e.getMessage(), e);
        }
    }
}
