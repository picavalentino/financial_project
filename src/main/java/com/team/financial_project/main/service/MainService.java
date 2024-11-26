package com.team.financial_project.main.service;

import com.team.financial_project.dto.MainStatisticsAgeDTO;
import com.team.financial_project.dto.MainStatisticsGenderDTO;
import com.team.financial_project.dto.MainInquireDTO;
import com.team.financial_project.dto.MainStatisticsSalesDTO;
import com.team.financial_project.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MainService {
    @Autowired
    MainMapper mainMapper;

    public List<MainInquireDTO> mainInqireList() {
        return mainMapper.mainInqireList();
    }

    public List<MainStatisticsGenderDTO> mainCustomerList() {
        return mainMapper.mainCustomerList();
    }

    public List<MainStatisticsAgeDTO> getAgeStatistics() {
        return mainMapper.getAgeStatistics();
    }

    public List<MainStatisticsSalesDTO> getSalesStatistics(String userId) {
        System.out.println("Service received userId: " + userId);
        return mainMapper.getSalesStatistics(userId);
    }

    public Map<String, String> getUserInfo(String userId) {
        // 사용자 정보와 직위 코드 가져오기
        Map<String, String> user = mainMapper.findUserById(userId);

        if (user != null) {
            // 직위 코드로 직위 이름 가져오기
            String codeNm = mainMapper.findCodeNameByCodeClAndCodeNo("201", user.get("user_jbps_ty_cd"));
            user.put("code_nm", codeNm); // 직위 이름 추가
        }

        return user;
    }
}
