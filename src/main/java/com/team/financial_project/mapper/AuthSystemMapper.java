package com.team.financial_project.mapper;

import com.team.financial_project.dto.AuthSystemDTO;
import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthSystemMapper {
    List<AuthSystemDTO> getAuthSystemList( @Param("offset") int offset,
                                           @Param("pageSize") int pageSize,
                                           @Param("auth") String auth);

    int getTotalAuthCount(@Param("auth") String auth);

    List<UserDTO> selectAuthList();

    // security 권한 설정에 사용할 전체 리스트
    List<AuthSystemDTO> getAllAuthMenu();

    List<AuthSystemDTO> getMenuList();
}
