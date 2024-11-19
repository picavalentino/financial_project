package com.team.financial_project.promotion.scheduler;

import com.team.financial_project.promotion.service.PromotionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PromotionBatchJob {

    private final PromotionService promotionService;

    public PromotionBatchJob(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void updateProgressStatuses() {
        System.out.println("Batch job started at: " + LocalDate.now());
        promotionService.updateAllProgressStatuses();
        System.out.println("Batch job completed.");
    }

    // @Scheduled는 Cron 표현식을 사용해 실행 주기를 설정
    // 0 0 0 * * ?: 매일 자정 실행
    // 0 0 9 * * ?: 매일 아침 9시에 실행
    // 0 0 0 1 * ?: 매월 1일 자정에 실행
}
