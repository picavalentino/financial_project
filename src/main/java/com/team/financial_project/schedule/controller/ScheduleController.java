package com.team.financial_project.schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScheduleController {
    @GetMapping("schedule/personal")
    public String schedulePersonal(){
        return "schedule/personal";
    }
    @GetMapping("schedule/customer")
    public String scheduleCustomer(){
        return "schedule/customer";
    }

}
