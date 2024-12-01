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
        BigDecimal normalizedInterestRate = interestRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal interest = totalPaid.multiply(normalizedInterestRate)
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
        // 불입금액, 목표기간, 적용금리, 이자과세율, 시작일자 가져오기
        BigDecimal monthlyDeposit = dto.getSavgCircleAmt();
        Integer goalPeriod = dto.getSavgGoalPrd();
        BigDecimal interestRate = dto.getSavgAplyRate();
        BigDecimal taxRate = dto.getProdIntTaxRate();
        LocalDate startDate = dto.getSavgStrtDt();

        // 검증
        if (monthlyDeposit == null || goalPeriod == null || interestRate == null ||
                taxRate == null || startDate == null || goalPeriod <= 0) {
            throw new IllegalArgumentException("Invalid input data for savings calculation.");
        }

        // 결과 DTO 초기화
        SavingsCalDto resultDto = new SavingsCalDto();
        List<SavingsDetailDto> detailList = new ArrayList<>();

        // 불입금액 합계 계산
        BigDecimal totalDepositAmount = monthlyDeposit.multiply(BigDecimal.valueOf(goalPeriod));

        // 세전이자 계산 (이자율은 백분율로 가정)
        BigDecimal normalizedInterestRate = interestRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal preTaxInterest = monthlyDeposit.multiply(BigDecimal.valueOf(goalPeriod))
                .multiply(BigDecimal.valueOf(goalPeriod + 1))
                .divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP)
                .multiply(normalizedInterestRate)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        // 세전수령액 계산
        BigDecimal preTaxTotal = totalDepositAmount.add(preTaxInterest);

        // 이자 과세금 계산
        BigDecimal taxAmount = preTaxInterest.multiply(taxRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP))
                .setScale(0, RoundingMode.FLOOR);

        // 세후수령액 계산
        BigDecimal afterTaxTotal = preTaxTotal.subtract(taxAmount).setScale(0, RoundingMode.CEILING);

        // 상세 데이터 생성
        BigDecimal accumulatedAmount = BigDecimal.ZERO;
        for (int i = 1; i <= goalPeriod; i++) {
            SavingsDetailDto detail = new SavingsDetailDto();
            detail.setInstallmentNo(i);
            detail.setInstallmentAmt(monthlyDeposit);

            // 누적 불입금액 계산
            accumulatedAmount = accumulatedAmount.add(monthlyDeposit);
            detail.setAccumulatedAmt(accumulatedAmount);

            // 회차 이자 계산
            BigDecimal installmentInterest = accumulatedAmount.multiply(normalizedInterestRate)
                    .divide(BigDecimal.valueOf(12), 0, RoundingMode.CEILING);
            detail.setInstallmentInt(installmentInterest);

            // 회차 원리금 계산
            detail.setInstallmentPrincipal(monthlyDeposit.add(installmentInterest));
            detailList.add(detail);
        }

        // 결과 DTO에 값 설정
        resultDto.setSavgTotDpstAmt(totalDepositAmount);
        resultDto.setSavgTotDpstInt(preTaxInterest);
        resultDto.setSavgTotRcveAmt(preTaxTotal);
        resultDto.setSavgIntTaxAmt(taxAmount);
        resultDto.setSavgAtxRcveAmt(afterTaxTotal);
        resultDto.setDetailList(detailList);

        return resultDto;
    }


}

