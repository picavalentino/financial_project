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
            String sortColumn, String sortDirection) {

        // 페이징 처리를 위한 시작 위치(offset) 계산
        // 현재 페이지에서 보여줄 데이터의 시작 위치를 구하기 위해 (현재 페이지 - 1) * 페이지 크기(size)를 사용
        int offset = (page - 1) * size;
        List<PromotionListDto> promotionList = mapper.getPagedList(
            prgStcd, dsTyCd, custNm, userNm, prodNm, sortColumn, sortDirection, offset, size);

            // 각 항목에 대해 만기금액 계산
            promotionList.forEach(this::setMaturityAmount);
            return promotionList;
        }

        // 만기 금액 계산 후 DTO에 설정
        private void setMaturityAmount(PromotionListDto dto) {
            BigDecimal maturityAmount = calculateMaturityAmount(dto);
            dto.setMtrAmt(maturityAmount);
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
                    return BigDecimal.ZERO; // 기본값
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
    public boolean updateAllProgressStatuses() {
        try {
            // 1. 모든 데이터 조회
            List<PromotionListDto> promotionList = mapper.getAllPromotions();

            // 2. 상태 업데이트 로직
            for (PromotionListDto dto : promotionList) {
                LocalDate maturityDate = LocalDate.parse(dto.getMtrDate()); // 만기일
                BigDecimal remainingBalance = calculateMaturityAmount(dto); // 만기 금액 계산

                String newStatus; // 상태를 저장할 변수
                if (remainingBalance.compareTo(BigDecimal.ZERO) == 0 || maturityDate.isBefore(LocalDate.now())) {
                    newStatus = "6"; // 만기완료
                } else if (maturityDate.minusMonths(1).isBefore(LocalDate.now())) {
                    newStatus = "4"; // 만기예정
                } else {
                    continue; // 상태 변경이 필요 없는 경우 스킵
                }

                // 3. 상태 업데이트
                if (!newStatus.equals(dto.getPrgStcd())) { // 상태 변경이 필요한 경우만 업데이트
                    mapper.updateProgressStatus(dto.getDsgnSn(), newStatus);
                }
            }

            return true; // 업데이트 성공
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 업데이트 실패
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