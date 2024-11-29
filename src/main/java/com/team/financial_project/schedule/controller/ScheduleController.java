package com.team.financial_project.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.schedule.dto.CustomerBirthdayDTO;
import com.team.financial_project.schedule.dto.CustomerMtrDtDTO;
import com.team.financial_project.schedule.dto.CustomerScheduleDTO;
import com.team.financial_project.schedule.dto.ScheduleDTO;
import com.team.financial_project.schedule.service.CustomerScheduleService;
import com.team.financial_project.schedule.service.PersonalScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ScheduleController {
    @Autowired
    private PersonalScheduleService personalScheduleService;

    @Autowired
    private CustomerScheduleService customerScheduleService;



    //개인 캘린더에유
    @GetMapping("/schedule/personal")
    public String schedulePersonal(Model model) {
        // 현재 사용자 ID 가져오기
        String user_id = getAuthenticatedUserId();

        // 일정 데이터 가져오기
        List<ScheduleDTO> personalScheduleList = personalScheduleService.getEventsByUserId(user_id);
        if (personalScheduleList == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }
        // 모델에 데이터 추가
        model.addAttribute("user_id", user_id);
        System.out.println("User ID from Controller: " + user_id);
        model.addAttribute("personalScheduleList", personalScheduleList);
        System.out.println("DTO 데이터: " + personalScheduleList);
        return "schedule/personal";
    }

    @PostMapping("/schedule/personal/register")
    @ResponseBody
    public ResponseEntity<?> registerSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        scheduleDTO.setCalendar_create_at(now);

        try {
            // ScheduleDTO 확인 로그
            System.out.println("Received ScheduleDTO: " + scheduleDTO);

            // 서비스 호출
            boolean isSaved = personalScheduleService.saveSchedule(scheduleDTO);
            if (isSaved) {
                return ResponseEntity.ok("Schedule registered successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register schedule.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PostMapping("/schedule/personal/update-task-status")
    @ResponseBody
    public ResponseEntity<String> updateTaskStatus(@RequestBody Map<String, Object> requestData) {
        try {
            int calendarSn = (int) requestData.get("calendar_sn");
            boolean taskCheckedVal = (boolean) requestData.get("task_checked_val");

            personalScheduleService.updateTaskStatus(calendarSn, taskCheckedVal);
            return ResponseEntity.ok("Task status updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update task status.");
        }
    }
    @PostMapping("/schedule/personal/delete-task")
    @ResponseBody
    public ResponseEntity<?> deleteTask(@RequestBody Map<String, Integer> requestData) {
        try {
            int calendarSn = requestData.get("calendar_sn");

            // 데이터 삭제 서비스 호출
            boolean isDeleted = personalScheduleService.deleteSchedule(calendarSn);

            if (isDeleted) {
                return ResponseEntity.ok("Task deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete task: " + e.getMessage());
        }
    }
    @GetMapping("/schedule/customer")
    public String scheduleCustomer(Model model) {
        String user_id = getAuthenticatedUserId();

        try {
            List<CustomerScheduleDTO> customerScheduleList = customerScheduleService.getEventsByUserId(user_id);

            // 디버깅 출력
            System.out.println("Fetched CustomerScheduleList: " + customerScheduleList);

            // 모델에 데이터 추가
            model.addAttribute("user_id", user_id);
            model.addAttribute("customerScheduleList", customerScheduleList);

        } catch (Exception e) {
            System.err.println("Error fetching schedule data: " + e.getMessage());
            model.addAttribute("errorMessage", "데이터를 로드하는 중 문제가 발생했습니다.");
        }

        return "schedule/customer";
    }
















    // 공통 사용 하는 녀석
    // 인증된 사용자 ID를 가져오는 메서드
    private String getAuthenticatedUserId() {
        // Spring Security에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // userId 반환 (로그인 시 사용한 ID)
    }





}
