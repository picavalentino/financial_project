package com.team.financial_project.main.service;

import com.team.financial_project.dto.AuthSystemDTO;
import com.team.financial_project.dto.MenuCategoryDTO;
import com.team.financial_project.dto.MenuDTO;
import com.team.financial_project.mapper.AuthSystemMapper;
import com.team.financial_project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SidebarService {
    private final UserMapper userMapper;
    private final AuthSystemMapper authSystemMapper;

    public SidebarService(UserMapper userMapper, AuthSystemMapper authSystemMapper) {
        this.userMapper = userMapper;
        this.authSystemMapper = authSystemMapper;
    }

    public Map<String, String> getUserInfo(String userId) {
        // 사용자 정보와 직위 코드 가져오기
        Map<String, String> user = userMapper.findUserById(userId);

        if (user != null) {
            // 직위 코드로 직위 이름 가져오기
            String codeNm = userMapper.findCodeNameByCodeClAndCodeNo("201", user.get("user_jbps_ty_cd"));
            user.put("code_nm", codeNm); // 직위 이름 추가
        }
        return user;
    }

    public List<MenuCategoryDTO> getMenuList() {
        List<AuthSystemDTO> authMenu = authSystemMapper.getMenuList();
        authMenu.forEach(m-> {
            
        });
        return null;
    }
}
