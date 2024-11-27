package com.team.financial_project.promotion.calculator;

import com.team.financial_project.promotion.dto.PromotionListDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccumulatedFundCalculator {

    public static BigDecimal calculate(PromotionListDto dto) {
        BigDecimal baseAmount = dto.getBaseAmount();
        BigDecimal interestRate = dto.getInterestRate();
        int period = dto.getPeriod();

        // 입력값 검증 : 입력값이 null이거나 잘못된 값이면 null 반환
        if (baseAmount == null || interestRate == null || period <= 0) {
            return null;
        }
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            return null;
        }

        // 단리 계산: 이자 = 목표 금액 × 금리 × (기간 / 12)
        BigDecimal interest = baseAmount.multiply(interestRate)
                .multiply(BigDecimal.valueOf(period))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        // 만기 금액 = 목표 금액 + 이자 (소수점 올림)
        BigDecimal maturityAmount = baseAmount.add(interest).setScale(0, RoundingMode.CEILING); // 위로 올림 적용

        // 디버깅 로그
        System.out.println("=== Accumulated Fund Calculation Debugging ===");
        System.out.println("DTO Data: " + dto);
        System.out.println("Base Amount (목표 금액): " + baseAmount);
        System.out.println("Interest Rate (금리): " + interestRate);
        System.out.println("Period (기간): " + period);
        System.out.println("Calculated Interest (계산된 이자): " + interest);
        System.out.println("Maturity Amount (만기 금액): " + maturityAmount);
        System.out.println("==============================================");

        return maturityAmount;
    }
}
