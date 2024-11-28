package com.team.financial_project.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.financial_project.mypage.dto.MypageDTO;
import com.team.financial_project.schedule.dto.ScheduleDTO;
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
import java.util.List;
import java.util.Map;

@Controller
public class ScheduleController {
    @Autowired
    private PersonalScheduleService personalScheduleService;




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
    public ResponseEntity<?> updateTaskStatus(@RequestBody ScheduleDTO scheduleDTO) {
        try {
            // ScheduleDTO 확인 로그
            System.out.println("Received ScheduleDTO for update: " + scheduleDTO);

            // 서비스 호출
            boolean isUpdated = personalScheduleService.updateTaskStatus(scheduleDTO);
            if (isUpdated) {
                return ResponseEntity.ok("Task status updated successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update task status.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
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
    public String scheduleCustomer(Model model){
        // 현재 사용자 ID 가져오기
        String userId = getAuthenticatedUserId();
        List<ScheduleDTO> customerScheduleList = personalScheduleService.getEventsByUserId(userId);
        if (customerScheduleList == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }
        // 3. 모델에 데이터 추가
        model.addAttribute("userId", userId);
        model.addAttribute("customerScheduleList", customerScheduleList);

        System.out.println("DTO 데이터: " + customerScheduleList);


        return "schedule/customer";
    }

    // 인증된 사용자 ID를 가져오는 메서드
    private String getAuthenticatedUserId() {
        // Spring Security에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // userId 반환 (로그인 시 사용한 ID)
    }





}
