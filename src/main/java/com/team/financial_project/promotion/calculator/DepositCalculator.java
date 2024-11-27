package com.team.financial_project.promotion.calculator;

import com.team.financial_project.promotion.dto.DepositCalDto;
import com.team.financial_project.promotion.dto.DepositDetailDto;
import com.team.financial_project.promotion.dto.PromotionListDto;
import com.team.financial_project.promotion.dto.SavingsCalDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class DepositCalculator {
    public static BigDecimal calculate(PromotionListDto dto) {
        BigDecimal baseAmount = dto.getBaseAmount(); // 원금
        BigDecimal interestRate = dto.getInterestRate(); // 금리
        int period = dto.getPeriod(); // 기간 (개월)

        // 입력값 검증 : 입력값이 null이거나 잘못된 값이면 null 반환
        if (baseAmount == null || interestRate == null || period <= 0) {
            return null;
        }
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            return null;
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

    public static DepositCalDto calculateDeposit(DepositCalDto dto) {
        // 예치금액, 목표기간, 적용금리, 이자과세율, 시작일자
        BigDecimal principal = dto.getDepositAmt(); // 예치금액
        int goalPeriod = dto.getGoalPeriod(); // 목표기간 (개월)
        BigDecimal interestRate = dto.getAplyRate().divide(BigDecimal.valueOf(100)); // %를 소수로 변환
        BigDecimal taxRate = dto.getTaxRate().divide(BigDecimal.valueOf(100)); // %를 소수로 변환
        LocalDate startDate = dto.getStartDate(); // 예금 시작일자

        // 세전 이자 계산: 예치금액 × (적용금리 / 12) × 목표기간
        BigDecimal totalInterest = principal.multiply(interestRate)
                .multiply(BigDecimal.valueOf(goalPeriod))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        // 세전 수령액 = 예치금액 + 세전 이자
        BigDecimal preTaxTotal = principal.add(totalInterest);

        // 이자 과세금 계산 (세전 이자에 대해 과세율 적용)
        BigDecimal taxAmount = totalInterest.multiply(taxRate).setScale(0, RoundingMode.HALF_UP);

        // 세후 수령액 = 세전 수령액 - 이자 과세금
        BigDecimal afterTaxTotal = preTaxTotal.subtract(taxAmount);

        // 상세 계산 기록 (1회만 기록, 예금의 경우 단순 계산)
        DepositDetailDto detail = new DepositDetailDto();
        detail.setPrincipal(principal); // 원금
        detail.setInterest(totalInterest); // 세전 이자
        detail.setTaxAmount(taxAmount); // 이자 과세금
        detail.setAfterTaxAmount(afterTaxTotal); // 세후 수령액

        // 결과 세팅
        dto.setTotalDepositAmt(principal); // 예치금액
        dto.setTotalInterest(totalInterest); // 세전 이자
        dto.setTotalPreTax(preTaxTotal); // 세전 수령액
        dto.setTaxAmount(taxAmount); // 이자 과세금
        dto.setTotalAfterTax(afterTaxTotal); // 세후 수령액
        dto.setDetail(detail); // 상세 정보

        return dto;
    }
}