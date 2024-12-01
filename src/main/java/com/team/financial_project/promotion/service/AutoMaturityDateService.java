package com.team.financial_project.promotion.service;

import com.team.financial_project.promotion.dto.AutoMaturityDateDto;
import com.team.financial_project.promotion.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class AutoMaturityDateService {
    @Autowired
    private PromotionMapper promotionMapper;

    @Transactional
    public void updateDesignStatuses() {
        List<AutoMaturityDateDto> designs = promotionMapper.findDesignsWithMaturityDates();

        LocalDate currentDate = LocalDate.now();

        for (AutoMaturityDateDto design : designs) {
            LocalDate earliestMaturityDate = Stream.of(
                            design.getAcml_mtr_dt(),
                            design.getDpst_mtr_dt(),
                            design.getLoan_mtr_dt(),
                            design.getSavg_mtr_dt()
                    ).filter(Objects::nonNull) // null 값 제거
                    .min(LocalDate::compareTo) // 가장 빠른 날짜 찾기
                    .orElse(null);

            if (earliestMaturityDate != null) {
                long daysToMaturity = ChronoUnit.DAYS.between(currentDate, earliestMaturityDate);

                int newStatus;
                if (daysToMaturity <= 0) {
                    newStatus = 6; // 이미 만기일이 지난 경우
                } else if (daysToMaturity <= 30) {
                    newStatus = 4; // 만기일이 30일 이하로 남은 경우
                } else {
                    continue; // 상태 변경 없음
                }

                promotionMapper.updateDesignStatus(design.getDsgn_sn(), newStatus);
            }
        }
    }
}
