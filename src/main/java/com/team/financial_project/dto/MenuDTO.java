package com.team.financial_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private Long menu_id;
    private String menu_category;
    private String menu_url;
    private String menu_name;
    private List<String> authList;
}
