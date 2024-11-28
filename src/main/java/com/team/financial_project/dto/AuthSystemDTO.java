package com.team.financial_project.dto;

import lombok.Data;

@Data
public class AuthSystemDTO {
    private int tb_id;
    private int auth_id;
    private int menu_id;
    private String auth_name;
    private String menu_name;
    private String menu_url;
}
