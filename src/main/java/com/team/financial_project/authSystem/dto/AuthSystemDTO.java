package com.team.financial_project.authSystem.dto;

import lombok.Data;

@Data
public class AuthSystemDTO {
    private int tb_id;
    private int auth_id;
    private int menu_id;
    private String auth_name;
    private String menu_name;
}
