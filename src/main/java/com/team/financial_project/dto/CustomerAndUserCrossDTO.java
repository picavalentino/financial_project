package com.team.financial_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAndUserCrossDTO {
    private CustomerDTO customer; // 고객 정보
    private UserDTO user;
}
