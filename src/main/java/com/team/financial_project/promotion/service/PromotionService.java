package com.team.financial_project.promotion.service;

import com.team.financial_project.promotion.calculator.AccumulatedFundCalculator;
import com.team.financial_project.promotion.calculator.DepositCalculator;
import com.team.financial_project.promotion.calculator.LoanCalculator;
import com.team.financial_project.promotion.calculator.SavingsCalculator;
import com.team.financial_project.promotion.dto.*;
import com.team.financial_project.promotion.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class PromotionService {
    private final PromotionMapper mapper;

    public PromotionService(PromotionMapper mapper) {
        this.mapper = mapper;
    }

    // MyBatis Test
    public int checkConnection() {
        return mapper.checkConnection();
    }

    // 페이지에 출력하기 위한 코드 리스트 조회
    public List<CodeDto> getCodeListByCl(String codeCl) {
        return mapper.getCodeListByCl(codeCl);
    }

    // 설계 조회 페이지
    // 전체 데이터 개수 조회
    public int getTotalCount(
            String prgStcd, String dsTyCd, String custNm, String userNm, String prodNm) {
        return mapper.getTotalCount(prgStcd, dsTyCd, custNm, userNm, prodNm); // 조건 기반으로 총 데이터 개수를 조회
    }

    // 데이터 리스트 조회 + 만기금액 계산
    public List<PromotionListDto> getPagedList(
            int page, int size, String prgStcd, String dsTyCd,
            String custNm, String userNm, String prodNm,
            String sortColumn, String sortDirection, String custId) {

        // 페이징 처리를 위한 시작 위치(offset) 계산
        // 현재 페이지에서 보여줄 데이터의 시작 위치를 구하기 위해 (현재 페이지 - 1) * 페이지 크기(size)를 사용
        int offset = (page - 1) * size;
        List<PromotionListDto> promotionList = mapper.getPagedList(
            prgStcd, dsTyCd, custNm, userNm, prodNm, sortColumn, sortDirection, offset, size, custId);

        // 각 항목에 대해 만기금액 계산
        promotionList.forEach(this::setMaturityAmount);
        return promotionList;
    }

    // 만기 금액 계산 후 DTO에 설정
    private void setMaturityAmount(PromotionListDto dto) {
        BigDecimal maturityAmount = calculateMaturityAmount(dto);
        if (maturityAmount == null) {
            dto.setMtrAmt("N/A"); // 상태를 문자열로 저장
        } else {
            dto.setMtrAmt(String.format("%,d", maturityAmount.longValue())); // 숫자에 콤마 추가
        }
    }

    // 만기 금액 계산 로직
    private BigDecimal calculateMaturityAmount(PromotionListDto dto) {

        if (dto == null || dto.getDsTyCd() == null) {
            return BigDecimal.ZERO;
        }

        switch (dto.getDsTyCd()) {
            case "1": // 적금
                return SavingsCalculator.calculate(dto);
            case "2": // 목돈
                return AccumulatedFundCalculator.calculate(dto);
            case "3": // 예금
                return DepositCalculator.calculate(dto);
            case "4": // 대출
                return LoanCalculator.calculate(dto);
            default:
                return null; // 계산 불가
        }
    }

    // 적금 계산 디버깅 로그
    private void logSavingsCalculation(PromotionListDto dto) {
        System.out.println("=== Savings Calculation Debugging ===");
        System.out.println("DTO Data: " + dto);
        System.out.println("Base Amount (Monthly Deposit): " + dto.getBaseAmount());
        System.out.println("Interest Rate: " + dto.getInterestRate());
        System.out.println("Start Date: " + dto.getStartDate());
        System.out.println("Period (Months): " + dto.getPeriod());
        System.out.println("Current Date: " + LocalDate.now());
        long elapsedMonths = ChronoUnit.MONTHS.between(dto.getStartDate(), LocalDate.now());
        System.out.println("Elapsed Months: " + elapsedMonths);
        BigDecimal totalPaid = dto.getBaseAmount().multiply(BigDecimal.valueOf(elapsedMonths));
        System.out.println("Total Paid So Far: " + totalPaid);
    }

    // 진행 상태 업데이트
    @Transactional
    public int updateAllProgressStatuses() {
        int updatedCount = 0; // 상태 변경된 항목 수를 저장할 변수

        try {
            // 1. 모든 데이터 조회
            List<PromotionListDto> promotionList = mapper.getAllPromotions();

            // 2. 상태 업데이트 로직
            for (PromotionListDto dto : promotionList) {

                // 필수 값 검증
                if (dto.getMtrDate() == null || dto.getPrgStcd() == null) {
                    continue; // 필수 값이 없으면 스킵
                }

                LocalDate maturityDate = LocalDate.parse(dto.getMtrDate()); // 만기일
                BigDecimal remainingBalance = calculateMaturityAmount(dto); // 만기 금액 계산

                String oldStatus = dto.getPrgStcd(); // 기존 상태
                String newStatus; // 상태를 저장할 변수

                // 상태 결정
                if ("4".equals(dto.getDsTyCd()) && remainingBalance.compareTo(BigDecimal.ZERO) == 0) {
                    newStatus = "6"; // 대출 상환 완료 (만기 완료)
                } else if (maturityDate.isBefore(LocalDate.now())) {
                    newStatus = "6"; // 일반적인 만기 완료
                } else if (maturityDate.minusMonths(1).isBefore(LocalDate.now())) {
                    newStatus = "4"; // 만기 예정
                } else {
                    continue; // 상태 변경이 필요 없는 경우 스킵
                }

                // 상태 변경
                if (!newStatus.equals(dto.getPrgStcd())) { // 상태 변경이 필요한 경우만 업데이트
                    mapper.updateProgressStatus(dto.getDsgnSn(), newStatus);
                    updatedCount++; // 변경된 항목 수 증가

                    // 변경 내역을 콘솔에 출력
                    System.out.println("Updated Status: ID = " + dto.getDsgnSn() +
                            ", Old Status = " + oldStatus +
                            ", New Status = " + newStatus);
                }
            }
            System.out.println("Progress status update completed.");
            return updatedCount; // 변경된 건수 반환
        } catch (Exception e) {
            System.out.println("Error occurred during status update.");
            e.printStackTrace();
            throw new RuntimeException("진행 상태 갱신 중 오류 발생", e);
        }
    }

    // 금융계산기 페이지



    // 상품 정보 리스트 조회
    public List<ProductInfoDto> getProductList(String prodCd, String prodNm) {
        return mapper.getProductList(prodCd, prodNm);
    }

    // 고객 정보 리스트 조회
    public List<UserInfoDto> getUserInfoList(String custNm, String custTelno) {
        return mapper.getUserInfoList(custNm, custTelno);
    }

    @Transactional
    public void save(SavingsSaveDto savingsSaveDto) {
        // Step 1: TB_CUSTPROD_DSGN에 데이터 삽입 (dsgnSn 값 자동 생성)
        mapper.insertCustprodDsgn(savingsSaveDto);

        // Step 2: 생성된 dsgnSn 값을 savingsSaveDto에 설정
        Integer generatedDsgnSn = savingsSaveDto.getDsgnSn();
        savingsSaveDto.setDsgnSn(generatedDsgnSn);

        // Step 3: TB_PROD_DSGN_SAVG에 데이터 삽입
        mapper.insertProdDsgnSavg(savingsSaveDto);
    }

}