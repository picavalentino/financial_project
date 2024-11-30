package com.team.financial_project.promotion.calculator;

import com.team.financial_project.promotion.dto.PromotionListDto;
import com.team.financial_project.promotion.dto.SavingsCalDto;
import com.team.financial_project.promotion.dto.SavingsDetailDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SavingsCalculator {

    // 만기금액 계산
    public static BigDecimal calculate(PromotionListDto dto) {

        // 월 납입액, 이자율, 적금 기간(개월), 시작일 가져오기
        BigDecimal monthlyDeposit = dto.getBaseAmount();
        BigDecimal interestRate = dto.getInterestRate();
        LocalDate startDate = dto.getStartDate();
        int totalPeriod = dto.getPeriod();

        // 입력값 검증 : 입력값이 null이거나 잘못된 값이면 null 반환
        if (monthlyDeposit == null || interestRate == null || startDate == null || totalPeriod <= 0) {
            return null;
            // return BigDecimal.ZERO;
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


    public static SavingsCalDto calculateSavings(SavingsCalDto dto) {
        // 불입금액, 목표기간, 적용금리, 이자과세율, 시작일자
        BigDecimal monthlyDeposit = dto.getSavgCircleAmt();
        int goalPeriod = dto.getSavgGoalPrd();
        BigDecimal interestRate = dto.getSavgAplyRate().divide(BigDecimal.valueOf(100)); // %를 소수로 변환
        BigDecimal taxRate = dto.getProdIntTaxRate().divide(BigDecimal.valueOf(100)); // %를 소수로 변환
        LocalDate startDate = dto.getSavgStrtDt();

        // 불입금액 합계 계산
        BigDecimal totalDeposit = monthlyDeposit.multiply(BigDecimal.valueOf(goalPeriod));

        // 세전 이자 계산 (누적이자 방식)
        BigDecimal totalInterest = BigDecimal.ZERO;
        List<SavingsDetailDto> detailList = new ArrayList<>();

        // 누적 금액을 기준으로 이자 계산
        BigDecimal accumulatedAmount = BigDecimal.ZERO;

        // 목표기간 동안 각 회차에 대해 이자 계산
        for (int i = 1; i <= goalPeriod; i++) {
            // 매 회차마다 누적된 금액에 대해서 이자 계산
            BigDecimal installmentInterest = accumulatedAmount.multiply(interestRate).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

            // 누적 금액 업데이트
            accumulatedAmount = accumulatedAmount.add(monthlyDeposit);  // 누적 금액에 불입금액을 더함

            SavingsDetailDto detail = new SavingsDetailDto();
            detail.setInstallmentNo(i); // 회차
            detail.setInstallmentAmt(monthlyDeposit); // 불입 금액
            detail.setAccumulatedAmt(accumulatedAmount); // 누적 금액 (누적된 잔액)
            detail.setInstallmentInt(installmentInterest); // 회차 이자
            detail.setInstallmentPrincipal(monthlyDeposit.add(installmentInterest)); // 회차 원리금
            detailList.add(detail);

            // 세전 이자 합산
            totalInterest = totalInterest.add(installmentInterest);
        }

        // 세전 수령액 = 불입금액 + 세전 이자
        BigDecimal preTaxTotal = totalDeposit.add(totalInterest);

        // 이자 과세금 계산 (세전 이자에 대해 과세율 적용)
        BigDecimal taxAmount = totalInterest.multiply(taxRate).setScale(0, RoundingMode.HALF_UP);

        // 세후 수령액 = 세전 수령액 - 이자 과세금
        BigDecimal afterTaxTotal = preTaxTotal.subtract(taxAmount);

        // 결과 세팅
        dto.setSavgTotDpstAmt(totalDeposit);
        dto.setSavgTotDpstInt(totalInterest);
        dto.setSavgTotRcveAmt(preTaxTotal);
        dto.setSavgIntTaxAmt(taxAmount);
        dto.setSavgAtxRcveAmt(afterTaxTotal);
        dto.setDetailList(detailList);

        return dto;
    }


}

