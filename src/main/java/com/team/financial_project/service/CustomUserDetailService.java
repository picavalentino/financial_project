package com.team.financial_project.service;

import com.team.financial_project.dto.CustomUserDetails;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserMapper userMapper;

    public CustomUserDetailService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.findById(id);

        if(!ObjectUtils.isEmpty(userDTO)){
            return new CustomUserDetails(userDTO);
        }
        return null;
    }
}
