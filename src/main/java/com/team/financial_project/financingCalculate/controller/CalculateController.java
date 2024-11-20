package com.team.financial_project.financingCalculate.controller;

import com.team.financial_project.financingCalculate.dto.*;
import com.team.financial_project.financingCalculate.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CalculateController {
    @Autowired
    CalculateService calculateService;

    @GetMapping("/calculateForm")
    public String calculateForm() {
        return "/financial/financialCalculate";
    }

    @GetMapping("/calculateSavg")
    public String calculateSavg(@RequestParam("inputMoney") Long inputMoney,
                                @RequestParam("period") Long period,
                                @RequestParam("interest") Double interest,
                                @RequestParam("taxRate") Double taxRate,
                                Model model) {

            CalculateSavgDTO dto = new CalculateSavgDTO();
            dto.setInputMoney(inputMoney);
            dto.setPeriod(period);
            dto.setInterest(interest);
            dto.setTaxRate(taxRate);

            // 초기화
            Long totalMoney = 0L;
            Double preTaxInterest = 0.0;
            Double preTaxMyMoney = 0.0;
            Double tax = 0.0;
            Double finalMoney = 0.0;

        if (inputMoney != null && period != null && interest != null && taxRate != null) {
            totalMoney = inputMoney * period; // 총 불입금
            preTaxInterest = (inputMoney * period * (period + 1) / 2 * ((interest/ 100) / 12)); // 세전 이자 계산
            preTaxMyMoney = totalMoney + preTaxInterest; // 세전 수령액
            tax = preTaxInterest * (taxRate / 100); // 세금 계산
            finalMoney = preTaxMyMoney - tax; // 세후 수령액
        }

            // DecimalFormat으로 포맷팅
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            String formattedTotalMoney = formatter.format(totalMoney);
            String formattedPreTaxInterest = formatter.format(preTaxInterest);
            String formattedPreTaxMyMoney = formatter.format(preTaxMyMoney);
            String formattedTax = formatter.format(tax);
            String formattedFinalMoney = formatter.format(finalMoney);

            // 모델에 포맷팅된 값 추가
            model.addAttribute("result", new CalculateResultDTO(
                Optional.ofNullable(formattedTotalMoney).orElse("0"),
                Optional.ofNullable(formattedPreTaxInterest).orElse("0"),
                Optional.ofNullable(formattedPreTaxMyMoney).orElse("0"),
                Optional.ofNullable(formattedTax).orElse("0"),
                Optional.ofNullable(formattedFinalMoney).orElse("0")
        ));
            return "/financial/financialCalculate";
        }
    @GetMapping("/products")
    public List<CalculateProductDTO> getAllProduct() {
        return calculateService.getAllSavgProduct();
    }

    @GetMapping("/calculateDpst")
    public String calculateDpst(@RequestParam("inputMoney") Long inputMoney,
                            @RequestParam("period") Long period,
                            @RequestParam("interest") Double interest,
                            @RequestParam("taxRate") Double taxRate,
                            Model model) {

        CalculateDpstDTO dto = new CalculateDpstDTO();
        dto.setInputMoney(inputMoney);
        dto.setPeriod(period);
        dto.setInterest(interest);
        dto.setTaxRate(taxRate);

        // 초기화
        Long totalMoney = 0L;
        Double preTaxInterest = 0.0;
        Double preTaxMyMoney = 0.0;
        Double tax = 0.0;
        Double finalMoney = 0.0;

        if (inputMoney != null && period != null && interest != null && taxRate != null) {
            totalMoney = inputMoney; // 총 예치금
            preTaxInterest = (inputMoney * (period * (interest/ 100) / 12)); // 세전 이자 계산
            preTaxMyMoney = totalMoney + preTaxInterest; // 세전 수령액
            tax = preTaxInterest * (taxRate / 100); // 세금 계산
            finalMoney = preTaxMyMoney - tax; // 세후 수령액
        }
        System.out.println(totalMoney);
        System.out.println(preTaxInterest);
        System.out.println(preTaxMyMoney);
        System.out.println(tax);
        System.out.println(finalMoney);

        // DecimalFormat으로 포맷팅
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        String formattedTotalMoney = formatter.format(totalMoney);
        String formattedPreTaxInterest = formatter.format(preTaxInterest);
        String formattedPreTaxMyMoney = formatter.format(preTaxMyMoney);
        String formattedTax = formatter.format(tax);
        String formattedFinalMoney = formatter.format(finalMoney);

        // 모델에 포맷팅된 값 추가
        model.addAttribute("dpstResult", new CalculateResultDTO(
                Optional.ofNullable(formattedTotalMoney).orElse("0"),
                Optional.ofNullable(formattedPreTaxInterest).orElse("0"),
                Optional.ofNullable(formattedPreTaxMyMoney).orElse("0"),
                Optional.ofNullable(formattedTax).orElse("0"),
                Optional.ofNullable(formattedFinalMoney).orElse("0")
        ));

        System.out.println("dpstResult: " + model.getAttribute("dpstResult"));


        return "/financial/financialCalculate";
    }

    @GetMapping("/calculateLumpSum")
    public String calculateLumpSum(@RequestParam("loanAmount") double loanAmount,
                                   @RequestParam("loanPeriod") int loanPeriodMonths,
                                   @RequestParam("interestRate") double annualInterestRate,
                                   Model model) {
        // 사용자 입력 DTO 생성
        LoanDTO inputDTO = new LoanDTO(loanAmount, loanPeriodMonths, annualInterestRate);

        // 계산 수행
        LoanResultDTO resultDTO = performCalculation(inputDTO);

        // 결과를 모델에 추가
        model.addAttribute("loanResult", resultDTO);

        System.out.println("loanResult: " + model.getAttribute("loanResult"));

        return "/financial/financialCalculate";
    }

    private LoanResultDTO performCalculation(LoanDTO inputDTO) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        double loanAmount = inputDTO.getLoanTotal();
        int loanPeriodMonths = inputDTO.getLoanPeriod();
        double annualInterestRate = inputDTO.getLoanInterestRate();

        // 월 이자율 계산
        double monthlyInterestRate = annualInterestRate / 100 / 12;

        // 총 이자 및 회차별 계산
        double totalInterest = 0.0;
        List<MonthlyInterestDTO> interestDetails = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (int i = 1; i <= loanPeriodMonths; i++) {
            LocalDate dueDate = currentDate.plusMonths(i - 1); // 회차별 날짜 계산
            int daysInMonth = dueDate.lengthOfMonth(); // 해당 월의 일수 계산
            double dailyInterestRate = annualInterestRate / 100 / 365; // 일할 이자율
            double monthlyInterest = loanAmount * dailyInterestRate * daysInMonth; // 월 이자 계산
            totalInterest += monthlyInterest;

            // 회차별 결과 저장
            interestDetails.add(new MonthlyInterestDTO(i, dueDate, monthlyInterest));
        }

        // 만기 시 총 상환 금액 계산
        double totalRepayment = loanAmount + totalInterest;

        // 결과 DTO 생성
        return new LoanResultDTO(totalInterest, totalRepayment, interestDetails);
    }

    @GetMapping("/calculateEqualPrincipal")
    private String calculateEqualPrincipal(@RequestParam("loanAmount") double loanAmount,
                                           @RequestParam("loanPeriod") int loanPeriodMonths,
                                           @RequestParam("interestRate") double annualInterestRate,
                                           Model model) {
        // 사용자 입력 DTO 생성
        LoanDTO inputDTO = new LoanDTO(loanAmount, loanPeriodMonths, annualInterestRate);

        // 계산 수행
        LoanResultDTO resultDTO = performEqualPrincipalCalculation(inputDTO);

        // 결과를 모델에 추가
        model.addAttribute("loanResult", resultDTO);

        System.out.println("loanResult2: " + model.getAttribute("loanResult2"));

        return "/financial/financialCalculate";
    }

    private LoanResultDTO performEqualPrincipalCalculation(LoanDTO inputDTO) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        double loanAmount = inputDTO.getLoanTotal();
        int loanPeriodMonths = inputDTO.getLoanPeriod();
        double annualInterestRate = inputDTO.getLoanInterestRate();

        // 월 원금 계산
        double monthlyPrincipal = loanAmount / loanPeriodMonths;

        // 회차별 납입 정보 저장
        List<MonthlyInterestDTO> paymentSchedule = new ArrayList<>();
        double totalInterest = 0.0;
        LocalDate currentDate = LocalDate.now();
        double remainingBalance = loanAmount;

        for (int i = 1; i <= loanPeriodMonths; i++) {
            LocalDate dueDate = currentDate.plusMonths(i - 1);

            // 월 이자 계산
            double monthlyInterestRate = annualInterestRate / 100 / 12;
            double monthlyInterest = remainingBalance * monthlyInterestRate;

            // 총 이자 계산
            totalInterest += monthlyInterest;

            // 월 납입금액 계산
            double totalMonthlyPayment = monthlyPrincipal + monthlyInterest;

            // 잔액 감소
            remainingBalance -= monthlyPrincipal;

            // 회차별 결과 저장
            paymentSchedule.add(new MonthlyInterestDTO(
                    i,
                    dueDate,
                    monthlyInterest,
                    monthlyPrincipal,
                    totalMonthlyPayment
            ));
        }

        // 결과 DTO 생성
        double totalRepayment = loanAmount + totalInterest;

        return new LoanResultDTO(totalInterest, totalRepayment, paymentSchedule);
    }

    @GetMapping("/calculateEqualPrincipalAndInterest")
    public String calculateEqualPrincipalAndInterest(@RequestParam("loanAmount") double loanAmount,
                                                     @RequestParam("loanPeriod") int loanPeriodMonths,
                                                     @RequestParam("interestRate") double annualInterestRate,
                                                     Model model) {

        // 계산 수행
        LoanResultDTO resultDTO = performCalculation(loanAmount, loanPeriodMonths, annualInterestRate);

        // 결과를 모델에 추가
        model.addAttribute("loanResult", resultDTO);

        System.out.println("loanResult: " + model.getAttribute("loanResult"));

        return "/financial/financialCalculate";
    }

    private LoanResultDTO performCalculation(double loanAmount, int loanPeriodMonths, double annualInterestRate) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        // 월 이자율 계산
        double monthlyInterestRate = annualInterestRate / 100 / 12;

        // 월 상환액 계산
        double monthlyPayment = loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanPeriodMonths)
                / (Math.pow(1 + monthlyInterestRate, loanPeriodMonths) - 1);

        List<MonthlyInterestDTO> paymentSchedule = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        double totalInterest = 0.0; // 총 이자
        double remainingBalance = loanAmount; // 잔액

        for (int i = 1; i <= loanPeriodMonths; i++) {
            LocalDate dueDate = currentDate.plusMonths(i - 1);

            double interestPayment = remainingBalance * monthlyInterestRate; // 이자 계산
            double principalPayment = monthlyPayment - interestPayment; // 원금 계산
            remainingBalance -= principalPayment; // 잔액 감소
            totalInterest += interestPayment;

            // 회차별 결과 저장
            paymentSchedule.add(new MonthlyInterestDTO(
                    i,
                    dueDate,
                    interestPayment,
                    principalPayment,
                    monthlyPayment
            ));
        }

        double totalRepayment = loanAmount + totalInterest;

        // 결과 DTO 생성
        return new LoanResultDTO(totalInterest, totalRepayment, paymentSchedule);
    }
}

