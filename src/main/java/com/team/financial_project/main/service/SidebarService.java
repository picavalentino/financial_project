package com.team.financial_project.main.service;

import com.team.financial_project.dto.AuthSystemDTO;
import com.team.financial_project.dto.MenuCategoryDTO;
import com.team.financial_project.dto.SubMenuDTO;
import com.team.financial_project.mapper.AuthSystemMapper;
import com.team.financial_project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SidebarService {
    private final UserMapper userMapper;
    private final AuthSystemMapper authSystemMapper;

    public SidebarService(UserMapper userMapper, AuthSystemMapper authSystemMapper) {
        this.userMapper = userMapper;
        this.authSystemMapper = authSystemMapper;
    }

    public Map<String, String> getUserInfo(String userId) {
        // 사용자 정보와 직위 코드 가져오기
        Map<String, String> user = userMapper.findUserById(userId);

        if (user != null) {
            // 직위 코드로 직위 이름 가져오기
            String codeNm = userMapper.findCodeNameByCodeClAndCodeNo("201", user.get("user_jbps_ty_cd"));
            user.put("code_nm", codeNm); // 직위 이름 추가
        }
        return user;
    }

    public List<MenuCategoryDTO> getMenuList() {
        List<AuthSystemDTO> authMenu = authSystemMapper.getMenuList();
        List<MenuCategoryDTO> menuCategoryList = new ArrayList<>();
        String commonUrl = "/images/common/";
        String imgType = ".png";

        for(AuthSystemDTO authData : authMenu){ // 가져온 정보 루프
            for (MenuCategoryDTO menuCategory : menuCategoryList){ // 가공된 카테고리 루프
                if(menuCategory.getCategoryName().equals(authData.getMenu_category())){ // 해당 카테고리가 이미 존재한다면
                    List<SubMenuDTO> subMenuList = menuCategory.getMenuList();

                    for(SubMenuDTO subMenu : subMenuList){
                        if(subMenu.getName().equals(authData.getMenu_name())){ // 권한 리스트에 같은 이름의 서브메뉴가 있다면
                            // 한 줄로 가공 ex) "MASTER HR_TEAM"
                            subMenu.setHasAnyRole(subMenu.getHasAnyRole() + " " + authData.getAuth_name());
                        }else { // 없으면 새로운 서브메뉴 생성 후 넣기
                            // 새로운 서브메뉴 생성
                            SubMenuDTO newSubMenu = new SubMenuDTO(
                                    authData.getMenu_id(),
                                    authData.getMenu_url(),
                                    authData.getMenu_name(),
                                    authData.getAuth_name()
                            );
                            // 해당 카테고리 서브메뉴 리스트에 추가 후 카테고리 업데이트
                            subMenuList.add(newSubMenu);
                        }
                    }
                }else {
                    // 새로운 서브메뉴 생성
                    SubMenuDTO newSubMenu = new SubMenuDTO(
                            authData.getMenu_id(),
                            authData.getMenu_url(),
                            authData.getMenu_name(),
                            authData.getAuth_name()
                    );
                    // 새로운 서브메뉴 리스트에 넣어주기
                    List<SubMenuDTO> subMenuList = new ArrayList<>();
                    subMenuList.add(newSubMenu);
                    // 새로운 메뉴카테고리 생성
                    MenuCategoryDTO newMenuCategory = new MenuCategoryDTO(
                            commonUrl + authData.getMenu_category().trim() +imgType,
                            authData.getMenu_category(),
                            subMenuList
                    );
                    // 메뉴 카테고리 리스트에 넣어주기
                    menuCategoryList.add(newMenuCategory);
                }
            }
        }
        menuCategoryList.forEach(System.out::println);
        return null;
    }
}
