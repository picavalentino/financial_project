package com.team.financial_project.promotion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDto {
    // 상품설계번호
    public String prodSn;
    // 설계유형코드
    public String prodDsTyCd;
    // 상품코드
    public String prodCd;
    // 상품명
    public String prodNm;
    // 가입대상
    public String prodSbstgTy;
    // 납입주기
    public String prodPayTy;
    // 납입주기코드
    public String prodPayTyCd;
    // 최소 적용금리
    public String prodAirMin;
    // 최대 적용금리
    public String prodAirMax;
    // 이자과세
    public String prodIntTaxTyCd;
    // 이자과세명
    public String prodIntTaxTy;
    // 이자과세율
    public String prodIntTaxRate;
}
