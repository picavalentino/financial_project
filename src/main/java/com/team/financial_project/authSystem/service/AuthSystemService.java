package com.team.financial_project.authSystem.service;

import com.team.financial_project.authSystem.dto.AuthSystemDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.AuthSystemMapper;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthSystemService {
    private final AuthSystemMapper authSystemMapper;

    public AuthSystemService(AuthSystemMapper authSystemMapper) {
        this.authSystemMapper = authSystemMapper;
    }
    public List<UserDTO> getauthList() {
        return authSystemMapper.selectAuthList();
    }

    public int getTotalAuthCount(String auth) {
        return authSystemMapper.getTotalAuthCount(auth);
    }

    public List<AuthSystemDTO> getAuthSystemList(int page, int pageSize, String auth) {
        int offset = (page - 1) * pageSize;
        return authSystemMapper.getAuthSystemList(offset, pageSize, auth);
    }

}
