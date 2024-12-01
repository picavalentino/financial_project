package com.team.financial_project.main.interceptor;

import com.team.financial_project.dto.MenuCategoryDTO;
import com.team.financial_project.main.service.SidebarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
public class MenuInterceptor implements HandlerInterceptor {
    private final SidebarService sidebarService;

    public MenuInterceptor(SidebarService sidebarService) {
        this.sidebarService = sidebarService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            List<MenuCategoryDTO> menuList = sidebarService.getMenuList();
            modelAndView.addObject("menuList", menuList);
        }
    }
}
