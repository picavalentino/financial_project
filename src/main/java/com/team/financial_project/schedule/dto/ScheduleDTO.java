package com.team.financial_project.schedule.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ScheduleDTO {
    //사원번호
    private String user_id;
    //일정 유형 : 팀,개인,고객 (1,2,3)
    private String calendar_type;
    private String calendar_type_code_cl;
    //일정 제목
    private String calendar_event_title;
    //일정 시작 날짜
    private Timestamp calendar_event_bgn_date;
    //일정 종료 날짜
    private Timestamp calendar_event_end_date;
    //일정 장소
    private String calendar_event_location;
    //일정 작성자명 ( 일정 대상 이름으로 하는건 어떤지,,)
    private String calendar_writer_nm;
    //종일 구분 ( event-bar-allday)
    private String calendar_is_all_day;
    private String calendar_is_all_day_code_cl;
    //일정 상세 내용






}
