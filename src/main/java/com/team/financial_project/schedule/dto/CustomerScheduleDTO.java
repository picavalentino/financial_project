package com.team.financial_project.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
public class CustomerScheduleDTO {
    private String user_id;
    private String calendar_type;
    private String calendar_event_title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp calendar_event_bgn_date;
    private String calendar_event_location;
    private String calendar_event_detail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp calendar_create_at;

    private int calendar_sn;
    private boolean task_checked_val;
    private List<CustomerBirthdayDTO> customerBirthdayDTO;
    private List<CustomerMtrDtDTO> customerMtrDtDTO;

}
