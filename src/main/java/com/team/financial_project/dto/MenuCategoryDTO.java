package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MenuCategoryDTO {
    private String iconImg;
    private String categoryName;
    private List<SubMenuDTO> menuList;
}
