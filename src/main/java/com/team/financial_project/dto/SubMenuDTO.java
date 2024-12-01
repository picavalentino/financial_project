package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubMenuDTO {
    private int id;
    private String url;
    private String name;
    private String hasAnyRole;
}
