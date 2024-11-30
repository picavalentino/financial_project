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

        for(AuthSystemDTO authData : authMenu){
            // menuCategoryList에 일치하는 카테고리가 있는지
            MenuCategoryDTO existingCategory = menuCategoryList.stream()
                    .filter(category -> category.getCategoryName().equals(authData.getMenu_category()))
                    .findFirst().orElse(null);

            if(existingCategory != null){
                List<SubMenuDTO> subMenuList = existingCategory.getMenuList();
                SubMenuDTO existingSubMenu = subMenuList.stream()
                        .filter(subMenu -> subMenu.getName().equals(authData.getMenu_name()))
                        .findFirst().orElse(null);

                if(existingSubMenu != null){
                    // 기존 서브메뉴의 권한 가공
                    menuCategoryList.forEach(category -> {
                        if (category.getCategoryName().equals(authData.getMenu_category())) {
                            category.getMenuList().forEach(subMenu -> {
                                if(subMenu.getName().equals(authData.getMenu_name())){
                                    subMenu.setHasAnyRole(subMenu.getHasAnyRole() + " " + authData.getAuth_name());
                                }
                            });
                        }
                    });
                }else {
                    // 새로운 서브메뉴 생성, 리스트에 넣기
                    SubMenuDTO newSubMenu = createSubMenu(authData);
                    subMenuList.add(newSubMenu);
                    menuCategoryList.forEach(category -> {
                        if (category.getCategoryName().equals(authData.getMenu_category())) {
                            category.setMenuList(subMenuList);
                        }
                    });
                }
            }else { // 메뉴카테고리 새로 추가
                // 새로운 서브메뉴 생성, 새로운 리스트에 넣기
                SubMenuDTO newSubMenu = createSubMenu(authData);
                List<SubMenuDTO> newSubMenuList = new ArrayList<>();
                newSubMenuList.add(newSubMenu);
                // 새로운 카테고리 생성
                MenuCategoryDTO newMenuCategory = createMenuCategory(authData, newSubMenuList);
                // 리스트에 넣기
                menuCategoryList.add(newMenuCategory);
            }
        }
        return menuCategoryList;
    }

    public SubMenuDTO createSubMenu(AuthSystemDTO authData){
        return new SubMenuDTO(
                authData.getMenu_id(),
                authData.getMenu_url(),
                authData.getMenu_name(),
                authData.getAuth_name()
        );
    }

    public MenuCategoryDTO createMenuCategory(AuthSystemDTO authData, List<SubMenuDTO> subMenuList){
        String commonUrl = "/images/common/";
        String imgType = ".png";

        return new MenuCategoryDTO(
                commonUrl + authData.getMenu_category().trim() + imgType,
                authData.getMenu_category(),
                subMenuList
        );
    }
}
