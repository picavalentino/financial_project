package com.team.financial_project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MainCalendarDTO {
    private Long calendarSn;
    private String userId;
    private String calendarEventTitle;
    private Timestamp calendarEventBgnDate;
    private Timestamp calendarEventEndDate;
}
