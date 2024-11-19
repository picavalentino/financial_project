package com.team.financial_project.promotion.calculator;

import com.team.financial_project.promotion.dto.PromotionListDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DepositCalculator {
    public static BigDecimal calculate(PromotionListDto dto) {
        BigDecimal baseAmount = dto.getBaseAmount(); // 원금
        BigDecimal interestRate = dto.getInterestRate(); // 금리
        int period = dto.getPeriod(); // 기간 (개월)

        // 입력값 검증
        if (baseAmount == null || interestRate == null || period <= 0) {
            return BigDecimal.ZERO;
        }
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        // 단리 계산: 이자 = 원금 × 금리 × (기간 / 12)
        BigDecimal interest = baseAmount.multiply(interestRate)
                .multiply(BigDecimal.valueOf(period))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        // 만기 금액 = 원금 + 이자 (소수점 올림)
        BigDecimal maturityAmount = baseAmount.add(interest).setScale(0, RoundingMode.CEILING);

        // 디버깅 로그: 입력값 출력
        System.out.println("=== Deposit Calculation Debugging ===");
        System.out.println("DTO Data: " + dto);
        System.out.println("Base Amount (원금): " + baseAmount);
        System.out.println("Interest Rate (금리): " + interestRate);
        System.out.println("Period (기간): " + period);
        System.out.println("Calculated Interest (이자): " + interest);
        System.out.println("Maturity Amount (만기 금액): " + maturityAmount);

        // 만기 금액 = 원금 + 이자
        return maturityAmount;
    }
}