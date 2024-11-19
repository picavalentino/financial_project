package com.team.financial_project;

import com.team.financial_project.promotion.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinancialProjectApplicationTests {

	@Autowired
	private PromotionService promotionService; // PromotionService를 자동 주입

	@Test
	void contextLoads() {
	}

	@Test
	void testUpdateAllProgressStatuses() {
		// updateAllProgressStatuses 메서드 호출
		boolean result = promotionService.updateAllProgressStatuses();

		// 결과 출력
		System.out.println("Batch Job Execution Result: " + result);
	}

}
