package com.team.financial_project.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_email;
    private String user_mbl_tel_No;
    private String user_img_path;
    private String user_auth_cd;
    private String user_status_cd;
}
