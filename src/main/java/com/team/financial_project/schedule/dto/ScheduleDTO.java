package com.team.financial_project.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class ScheduleDTO {
    //사원번호
    private String user_id;
    //일정 유형 : 팀,개인,고객 (1,2,3)
    private String calendar_type;
    //일정 제목
    private String calendar_event_title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp calendar_event_bgn_date;

    //일정 장소
    private String calendar_event_location;
    //일정 작성자명
//    private String calendar_writer_nm;
    //종일 구분 ( event-bar-allday)
    private String calendar_is_all_day;
    //일정 상세 내용
    private  String calendar_event_detail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp calendar_create_at;

    private int calendar_sn;

    private boolean task_checked_val;




}
