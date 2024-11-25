package com.team.financial_project.mapper;

import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDTO> findManagersByName(@Param("name") String name);
}
