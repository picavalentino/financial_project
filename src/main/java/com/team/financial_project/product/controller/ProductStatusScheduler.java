package com.team.financial_project.product.controller;

import com.team.financial_project.product.service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProductStatusScheduler {

    private final ProductService productService;

    public ProductStatusScheduler(ProductService productService) {
        this.productService = productService;
    }

//    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Scheduled(cron = "0 * * * * ?") // 매일 자정에 실행
    public void updateProductStatusDaily() {
        productService.updateProductStatuses();
    }
}
