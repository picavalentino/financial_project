package com.team.financial_project.security.service;

import com.team.financial_project.dto.CodeDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.CodeMapper;
import com.team.financial_project.mapper.UserMapper;
import com.team.financial_project.security.etc.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserMapper userMapper;
    private final CodeMapper codeMapper;

    public CustomUserDetailService(UserMapper userMapper, CodeMapper codeMapper) {
        this.userMapper = userMapper;
        this.codeMapper = codeMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.findById(id);
        if(!ObjectUtils.isEmpty(userDTO)){
            List<CodeDTO> authCodeList = codeMapper.getAllAuth();
            for(CodeDTO auth : authCodeList){
                // 권한 코드중에 코드번호가 일치하면 코드명 가져오기
                if(auth.getCode_no().equals(userDTO.getUser_auth_cd())) userDTO.setUser_auth_cd("ROLE_"+ auth.getCode_nm());
            }
            List<CodeDTO> statusCodeList = codeMapper.getAllStatus();
            for(CodeDTO status : statusCodeList){
                if(status.getCode_no().equals(userDTO.getUser_status())) userDTO.setUser_status(status.getCode_nm());
            }
            log.info("####사용자 권한 : " + userDTO.getUser_auth_cd());
            log.info("####사용자 상태 : " + userDTO.getUser_status());
            return new CustomUserDetails(userDTO);
        }
        return null;
    }
}
