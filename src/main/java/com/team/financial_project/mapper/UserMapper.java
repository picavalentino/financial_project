package com.team.financial_project.mapper;

import com.team.financial_project.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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


    // employee modal에서 출력할 user 찾기
    UserDTO selectUserById(@Param("userId") String userId);

    // employee modal에서 정보 변경하기
    void updateUser(UserDTO userDTO);

    // 사원번호 생성하는 로직
    int getEmployeeCountForDate(String joiningDate);

    // employee InsertModal에서 직원등록하기
    void insertEmployee(UserDTO userDTO);

    // 로그인
    UserDTO findById(String id);

    // 이름으로 사원번호 찾기
    List<UserDTO> findUsersByKeyword(String keyword);

    // 휴대전화 중복확인
    UserDTO certifyByUserTelno(String telno);

    // 사원 등록
    void updateUserFromReg(UserDTO user);

    // 사번 찾기에서 해당 유저가 있는지 확인
    UserDTO hasExistByUser_name(@Param("name") String name, @Param("telno")String telno);

    // 사번 찾기
    String findUserIdByUserTelno(String telno);

    // 비밀번호 찾기에서 해당 유저가 있는지 확인
    UserDTO hasExistByUser_id(@Param("id") String id, @Param("telno")String telno);

    // 사원번호로 찾아 비밀번호 변경
    void updateUserPw(@Param("id")String userId, @Param("pw")String userPw);

    // 상품등록페이지: 담당자 검색
    List<Map<String, Object>> findByNameContaining(@Param("name") String name);

    // 고객관리페이지 : 담당자 검색
    List<UserDTO> findManagersByName(@Param("name") String name);

    // 사용자 정보 조회
    @Select("SELECT user_imgpath, user_name, user_jbps_ty_cd FROM tb_user WHERE user_id = #{userId}")
    Map<String, String> findUserById(@Param("userId") String userId);

    // 직위 이름 조회
    @Select("SELECT code_nm FROM tb_code WHERE code_cl = #{codeCl} AND code_no = #{codeNo}")
    String findCodeNameByCodeClAndCodeNo(@Param("codeCl") String codeCl, @Param("codeNo") String codeNo);
}
