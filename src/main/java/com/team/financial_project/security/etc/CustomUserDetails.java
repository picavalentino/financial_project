package com.team.financial_project.security.etc;

import com.team.financial_project.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final UserDTO userDTO;

    public CustomUserDetails(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ObjectUtils.isEmpty(userDTO.getUser_auth_cd())
                ? List.of()
                : List.of(new SimpleGrantedAuthority(userDTO.getUser_auth_cd()));
    }

    @Override
    public String getPassword() {
        return userDTO.getUser_pw();
    }

    @Override
    public String getUsername() {
        return userDTO.getUser_id();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(userDTO.getUser_status());
    }
}
