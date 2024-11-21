package com.team.financial_project.mapper;

import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDTO findById(String id);

    List<UserDTO> findUsersByKeyword(String keyword);

    UserDTO certifyByUserTelno(String telno);
}
