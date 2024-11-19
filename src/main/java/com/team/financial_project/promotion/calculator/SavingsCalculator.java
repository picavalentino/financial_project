package com.team.financial_project.promotion.calculator;

import com.team.financial_project.promotion.dto.PromotionListDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SavingsCalculator {
    public static BigDecimal calculate(PromotionListDto dto) {

        // 월 납입액, 이자율, 적금 기간(개월), 시작일 가져오기
        BigDecimal monthlyDeposit = dto.getBaseAmount();
        BigDecimal interestRate = dto.getInterestRate();
        LocalDate startDate = dto.getStartDate();
        int totalPeriod = dto.getPeriod();

        // 유효성 검사: 입력값이 null이거나 잘못된 값이면 0 반환
        if (monthlyDeposit == null || interestRate == null || startDate == null || totalPeriod <= 0) {
            return BigDecimal.ZERO;
        }

        // 현재까지 경과된 회차 계산
        long elapsedPeriods = ChronoUnit.MONTHS.between(startDate, LocalDate.now());
        elapsedPeriods = Math.min(elapsedPeriods, totalPeriod); // 경과된 기간이 전체 기간을 초과하지 않도록 제한

        // 현재까지 납입 금액 = 월 납입액 × 경과 회차
        BigDecimal totalPaid = monthlyDeposit.multiply(BigDecimal.valueOf(elapsedPeriods));
        System.out.println("Total Paid: " + totalPaid);

        // 현재까지 발생한 이자 = 총 납입 금액 × 이자율 × (경과 회차 / 12)
        BigDecimal interest = totalPaid.multiply(interestRate)
                .multiply(BigDecimal.valueOf(elapsedPeriods))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        // 현재 잔액 = 총 납입 금액 + 이자 (소수점 올림)
        BigDecimal currentBalance = totalPaid.add(interest).setScale(0, RoundingMode.CEILING);

        // 디버깅 로그: 입력값 출력
        System.out.println("=== Savings Calculation Debugging ===");
        System.out.println("DTO Data: " + dto);
        System.out.println("Monthly Deposit (월 납입액): " + monthlyDeposit);
        System.out.println("Interest Rate (이자율): " + interestRate);
        System.out.println("Start Date (시작일): " + startDate);
        System.out.println("Total Period (전체 기간): " + totalPeriod);
        System.out.println("Elapsed Periods (경과 회차): " + elapsedPeriods);
        System.out.println("Total Paid So Far (총 납입 금액): " + totalPaid);
        System.out.println("Calculated Interest (계산된 이자): " + interest);
        System.out.println("Current Balance (현재 잔액): " + currentBalance);

        return currentBalance;
    }
}

