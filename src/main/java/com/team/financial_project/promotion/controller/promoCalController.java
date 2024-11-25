package com.team.financial_project.promotion.controller;

import com.team.financial_project.promotion.service.PromoCalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class promoCalController {
    @Autowired
    PromoCalService promoCalService;

    // tab 화면전환 컨트롤러
    @GetMapping("/promotion/CalDpst")
    public String calculateFormDpst() {
        return "/promotion/promotionCalDpst";
    }

    @GetMapping("/promotion/CalAcml")
    public String calculateFormAcml() {
        return "/promotion/promotionCalAcml";
    }

    @GetMapping("/promotion/CalLoan")
    public String calculateFormLoan() {
        return "/promotion/promotionCalLoan";
    }

    // 설계 디테일 페이지 이동
    @GetMapping("/promotion/cal/detail")
    public String moveDetailPage(@RequestParam("dsgnSn") Long dsgnSn, Model model) {
        try {
            String dsTyCd = promoCalService.findDsTyCd(dsgnSn);
            System.out.println("===========" + dsTyCd);

            switch (dsTyCd) {
                case "1":
                    return "/promotion/promotionDetailSavg";
                case "2":
                    return "/promotion/promotionDetailAcml";
                case "3":
                    return "/promotion/promotionDetailDpst";
                case "4":
                    return "/promotion/promotionDetailLoan";
                default:
                    model.addAttribute("errorMessage", "잘못된 설계유형코드입니다.");
                    return "/promotion/promotionList";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "처리 중 오류가 발생했습니다: " + e.getMessage());
            return "/promotion/promotionList";
        }
    }
}
