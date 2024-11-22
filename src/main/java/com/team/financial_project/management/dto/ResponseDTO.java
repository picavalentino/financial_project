package com.team.financial_project.management.dto;

import com.team.financial_project.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO {
    private List<UserDTO> departmentList;
    private List<UserDTO> jobPositionList;
    private List<UserDTO> statusList;
    private List<UserDTO> authList;

    public ResponseDTO(List<UserDTO> departmentList, List<UserDTO> jobPositionList, List<UserDTO> statusList, List<UserDTO> authList) {
        this.departmentList = departmentList;
        this.jobPositionList = jobPositionList;
        this.statusList = statusList;
        this.authList = authList;
    }
}
