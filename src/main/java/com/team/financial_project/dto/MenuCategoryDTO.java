package com.team.financial_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuCategoryDTO {
    private String iconImg;
    private String categoryName;
    private List<MenuDTO> menuList;
}
