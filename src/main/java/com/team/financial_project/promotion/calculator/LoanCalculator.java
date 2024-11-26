package com.team.financial_project.promotion.calculator;

import com.team.financial_project.promotion.dto.LoanCalDto;
import com.team.financial_project.promotion.dto.LoanDetailsDto;
import com.team.financial_project.promotion.dto.PromotionListDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoanCalculator {

    // 원리금 균등 상환 방식의 대출 잔액 계산
    public static BigDecimal calculate(PromotionListDto dto) {

        BigDecimal loanAmount = dto.getBaseAmount(); // 대출 금액
        BigDecimal interestRate = dto.getInterestRate(); // 연 이자율
        int period = dto.getPeriod(); // 상환 기간 (개월)
        LocalDate startDate = dto.getStartDate(); // 대출 시작일

        // 입력값 검증
        if (loanAmount == null || interestRate == null || period <= 0 || startDate == null) {
            return BigDecimal.ZERO;
        }

        // 연 이자율 -> 월 이자율 (백분율 기반 계산)
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        // 월 상환액 계산: 대출금 × 월이자율 / (1 - (1 + 월이자율)^(-기간))
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(
                onePlusRate.pow(period, MathContext.DECIMAL128),
                MathContext.DECIMAL128
        ));
        BigDecimal monthlyRepayment = loanAmount.multiply(monthlyRate)
                .divide(denominator, 2, RoundingMode.HALF_UP);

        // 경과 회차 계산 (현재 날짜 기준)
        long elapsedPeriods = Math.min(
                ChronoUnit.MONTHS.between(startDate, LocalDate.now()),
                period
        );

        // 총 상환 금액 계산
        BigDecimal totalPaid = monthlyRepayment.multiply(BigDecimal.valueOf(elapsedPeriods));

        // 남은 대출 잔액 계산 (소수점 내림)
        BigDecimal remainingBalance = loanAmount.subtract(totalPaid).max(BigDecimal.ZERO).setScale(0, RoundingMode.FLOOR);

        // 디버깅 로그
        System.out.println("=== Loan Calculation Debugging ===");
        System.out.println("Loan Amount: " + loanAmount);
        System.out.println("Interest Rate: " + interestRate);
        System.out.println("Monthly Rate: " + monthlyRate);
        System.out.println("Period: " + period);
        System.out.println("Start Date: " + startDate);
        System.out.println("Elapsed Periods: " + elapsedPeriods);
        System.out.println("Monthly Repayment: " + monthlyRepayment);
        System.out.println("Total Paid: " + totalPaid);
        System.out.println("Remaining Balance: " + remainingBalance);

        return remainingBalance;
    }

    // 원리금균등상환 계산식
    public static LoanCalDto calculateEqualPrincipalAndInterest(LoanCalDto loanCalDto) {
        // 입력값 가져오기
        BigDecimal loanAmount = loanCalDto.getSavgCircleAmt(); // 대출금액
        int loanPeriodMonths = loanCalDto.getSavgGoalPrd(); // 대출기간 (개월)
        BigDecimal annualInterestRate = loanCalDto.getSavgAplyRate(); // 연이율

        // 월 이자율 계산
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 12), 12, RoundingMode.HALF_UP);

        // 월 상환액 계산
        BigDecimal multiplier = (BigDecimal.ONE.add(monthlyInterestRate)).pow(loanPeriodMonths);
        BigDecimal monthlyPayment = loanAmount.multiply(monthlyInterestRate).multiply(multiplier)
                .divide(multiplier.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        List<LoanDetailsDto> paymentSchedule = new ArrayList<>();
        LocalDate currentDate = loanCalDto.getSavgStrtDt();
        BigDecimal totalInterest = BigDecimal.ZERO; // 총 이자
        BigDecimal remainingBalance = loanAmount; // 남은 원금

        for (int i = 1; i <= loanPeriodMonths; i++) {
            LocalDate dueDate = currentDate.plusMonths(i - 1);

            // 이자 및 원금 계산
            BigDecimal interestPayment = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
            remainingBalance = remainingBalance.subtract(principalPayment).setScale(2, RoundingMode.HALF_UP);

            // 총 이자 누적
            totalInterest = totalInterest.add(interestPayment);

            // 상세 데이터 추가
            LoanDetailsDto detail = new LoanDetailsDto();
            detail.setInstallment(i);
            detail.setMonthlyInterest(interestPayment.doubleValue());
            detail.setPrincipal(principalPayment.doubleValue());
            detail.setTotalPayment(monthlyPayment.doubleValue());
            paymentSchedule.add(detail);
        }

        // 총 상환 금액 계산
        BigDecimal totalRepayment = loanAmount.add(totalInterest).setScale(2, RoundingMode.HALF_UP);

        // 결과 DTO에 값 설정
        loanCalDto.setSavgTotDpstAmt(loanAmount); // 대출액
        loanCalDto.setSavgTotDpstInt(totalInterest); // 총 이자
        loanCalDto.setSavgAtxRcveAmt(totalRepayment); // 총 납입금액
        loanCalDto.setDetailList(paymentSchedule); // 상세 리스트

        return loanCalDto;
    }


    public static LoanCalDto calculateEqualPrincipal(LoanCalDto loanCalDto) {

        // 입력값 가져오기
        BigDecimal loanAmount = loanCalDto.getSavgCircleAmt(); // 대출금액
        int loanPeriodMonths = loanCalDto.getSavgGoalPrd(); // 대출 기간 (개월)
        BigDecimal annualInterestRate = loanCalDto.getSavgAplyRate(); // 연이율

        // 월 원금 계산
        BigDecimal monthlyPrincipal = loanAmount.divide(BigDecimal.valueOf(loanPeriodMonths), 2, RoundingMode.HALF_UP);

        // 초기 변수 설정
        List<LoanDetailsDto> paymentSchedule = new ArrayList<>();
        BigDecimal totalInterest = BigDecimal.ZERO; // 총 이자
        BigDecimal remainingBalance = loanAmount; // 잔액
        LocalDate currentDate = loanCalDto.getSavgStrtDt(); // 시작일자

        for (int i = 1; i <= loanPeriodMonths; i++) {
            LocalDate dueDate = currentDate.plusMonths(i - 1);

            // 월 이자 계산
            BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 12), 12, RoundingMode.HALF_UP);
            BigDecimal monthlyInterest = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);

            // 총 이자 누적
            totalInterest = totalInterest.add(monthlyInterest);

            // 월 납입금액 계산
            BigDecimal totalMonthlyPayment = monthlyPrincipal.add(monthlyInterest);

            // 잔액 감소
            remainingBalance = remainingBalance.subtract(monthlyPrincipal).setScale(2, RoundingMode.HALF_UP);

            // 회차별 상세 데이터 추가
            LoanDetailsDto detail = new LoanDetailsDto();
            detail.setInstallment(i);
            detail.setMonthlyInterest(monthlyInterest.doubleValue());
            detail.setPrincipal(monthlyPrincipal.doubleValue());
            detail.setTotalPayment(totalMonthlyPayment.doubleValue());
            paymentSchedule.add(detail);
        }

        // 총 납입 금액 계산
        BigDecimal totalRepayment = loanAmount.add(totalInterest).setScale(2, RoundingMode.HALF_UP);

        // 결과 DTO 설정
        loanCalDto.setSavgTotDpstAmt(loanAmount); // 대출액
        loanCalDto.setSavgTotDpstInt(totalInterest); // 총 이자
        loanCalDto.setSavgAtxRcveAmt(totalRepayment); // 총 납입 금액
        loanCalDto.setDetailList(paymentSchedule); // 상세 리스트

        return loanCalDto;
    }

    public static LoanCalDto calculateBalloonPayment(LoanCalDto loanCalDto) {
        // 입력값 가져오기
        BigDecimal loanAmount = loanCalDto.getSavgCircleAmt(); // 대출금액
        int loanPeriodMonths = loanCalDto.getSavgGoalPrd();    // 대출기간 (개월)
        BigDecimal annualInterestRate = loanCalDto.getSavgAplyRate(); // 연이율
        LocalDate startDate = loanCalDto.getSavgStrtDt();      // 시작일자

        // 월 이자율 계산
        BigDecimal dailyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 365), 12, RoundingMode.HALF_UP);

        BigDecimal totalInterest = BigDecimal.ZERO; // 총 이자
        List<LoanDetailsDto> paymentSchedule = new ArrayList<>();
        BigDecimal monthlyInterest;

        for (int i = 1; i <= loanPeriodMonths; i++) {
            LocalDate dueDate = startDate.plusMonths(i - 1); // 회차별 날짜 계산
            int daysInMonth = dueDate.lengthOfMonth(); // 해당 월의 일수 계산
            monthlyInterest = loanAmount.multiply(dailyInterestRate).multiply(BigDecimal.valueOf(daysInMonth)).setScale(2, RoundingMode.HALF_UP);

            totalInterest = totalInterest.add(monthlyInterest); // 총 이자 누적

            // 상세 데이터 추가
            LoanDetailsDto detail = new LoanDetailsDto();
            detail.setInstallment(i);
            detail.setMonthlyInterest(monthlyInterest.doubleValue());
            detail.setPrincipal(i == loanPeriodMonths ? loanAmount.doubleValue() : 0); // 마지막 회차에만 원금 납부
            detail.setTotalPayment(i == loanPeriodMonths
                    ? loanAmount.add(monthlyInterest).doubleValue()
                    : monthlyInterest.doubleValue());
            paymentSchedule.add(detail);
        }

        // 만기 시 총 상환 금액 계산
        BigDecimal totalRepayment = loanAmount.add(totalInterest);

        // 결과 DTO에 값 설정
        loanCalDto.setSavgTotDpstAmt(loanAmount); // 대출액
        loanCalDto.setSavgTotDpstInt(totalInterest); // 총 이자
        loanCalDto.setSavgAtxRcveAmt(totalRepayment); // 총 상환 금액
        loanCalDto.setDetailList(paymentSchedule); // 회차별 상세 정보

        return loanCalDto;
    }
}


