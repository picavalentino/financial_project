package com.team.financial_project.security.service;

import com.team.financial_project.dto.AuthSystemDTO;
import com.team.financial_project.mapper.AuthSystemMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthMenuAccessService {
    private final AuthSystemMapper authSystemMapper;

    public AuthMenuAccessService(AuthSystemMapper authSystemMapper) {
        this.authSystemMapper = authSystemMapper;
    }

    public MultiValueMap<String, String> getAllAuthMenu(){
        List<AuthSystemDTO> menuList = authSystemMapper.getAllAuthMenu();
        // <"url", "권한">
        MultiValueMap<String, String> mapList = new LinkedMultiValueMap<>();
        menuList.forEach(menu-> mapList.add(menu.getMenu_url(), menu.getAuth_name()));
        return mapList;
    }
}
