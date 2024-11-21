package com.team.financial_project.mapper;

import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    // 부서 정보 가져오기
    List<UserDTO> selectDepartmentList();

    // 직위 정보 가져오기
    List<UserDTO> selectJopPositionList();

    // 상태 정보 가져오기
    List<UserDTO> selectStatusList();

    // 권한 정보 가져오기
    List<UserDTO> selectAuthList();

    // 전체 직원 수 가져오기 (검색 조건 포함) - list
    int getTotalDataCount(@Param("dept") String dept, @Param("position") String position,
                          @Param("searchField") String searchField, @Param("searchValue") String searchValue);

    // 현재 페이지에 맞는 직원 목록 가져오기 (검색 조건 포함)
    List<UserDTO> selectManagementList(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                       @Param("dept") String dept, @Param("position") String position,
                                       @Param("searchField") String searchField, @Param("searchValue") String searchValue);

    // 전체 직원 수 가져오기 (검색 조건 포함) - employees
    int getTotalEmployeeCount(@Param("dept") String dept, @Param("position") String position,
                              @Param("status") String status, @Param("searchField") String searchField,
                              @Param("searchValue") String searchValue);

    List<UserDTO> selectEmployeeList(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                     @Param("dept") String dept, @Param("position") String position,
                                     @Param("status") String status, @Param("auth") String auth,
                                     @Param("searchField") String searchField,
                                     @Param("searchValue") String searchValue);

    // 전체 직원 수 가져오기 (검색 조건 포함) - insert
    int getTotalInsertCount(@Param("yearMonth") String yearMonth);

    List<UserDTO> selectInsertList(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                   @Param("yearMonth") String yearMonth);

    List<Map<String, Object>> findByNameContaining(@Param("name") String name);
}