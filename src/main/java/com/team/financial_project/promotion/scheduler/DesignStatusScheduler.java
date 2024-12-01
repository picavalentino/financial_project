package com.team.financial_project.promotion.scheduler;

import com.team.financial_project.promotion.service.AutoMaturityDateService;
import com.team.financial_project.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class DesignStatusScheduler {
    @Autowired
    private AutoMaturityDateService autoMaturityDateService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void scheduleStatusUpdate() {
        autoMaturityDateService.updateDesignStatuses();
    }
}
