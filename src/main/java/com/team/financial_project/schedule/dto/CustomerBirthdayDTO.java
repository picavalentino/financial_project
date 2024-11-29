package com.team.financial_project.schedule.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CustomerBirthdayDTO {
    private String cust_id;
    private String cust_birthday;
    private String cust_nm;
}
