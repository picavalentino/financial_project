package com.team.financial_project.mapper;

import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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


    // employee modal에서 출력할 user 찾기
    UserDTO selectUserById(@Param("userId") String userId);

    // employee modal에서 정보 변경하기
    void updateUser(UserDTO userDTO);

    // 사원번호 생성하는 로직
    int getEmployeeCountForDate(String joiningDate);

    // employee InsertModal에서 직원등록하기
    void insertEmployee(UserDTO userDTO);

    UserDTO findById(String id);

    List<UserDTO> findUsersByKeyword(String keyword);

    UserDTO certifyByUserTelno(String telno);

    void updateUserFromReg(UserDTO user);
}
