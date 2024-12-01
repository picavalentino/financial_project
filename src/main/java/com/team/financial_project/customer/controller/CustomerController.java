package com.team.financial_project.customer.controller;

import com.team.financial_project.counsel.dto.CodeDTO;
import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.service.CounselService;
import com.team.financial_project.customer.service.CustomerService;
import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.CustomerUpdateHistoryDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.inquire.service.InquireService;
import com.team.financial_project.main.service.SmsService;
import com.team.financial_project.management.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {


    private final SmsService smsService;
    private final CustomerService customerService;
    private final ManagementService managementService;
    private final CounselService counselService;
    private final InquireService inquireService;
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public CustomerController(SmsService smsService, CustomerService customerService, ManagementService managementService, InquireService inquireService, CounselService counselService) {
        this.smsService = smsService;
        this.customerService = customerService;
        this.managementService = managementService;
        this.counselService = counselService;
        this.inquireService = inquireService;
    }

    //로그인
    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // username을 반환
    }

    @GetMapping("/list")
    public String getCustomerList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "searchType", required = false) String searchType,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) String status, // 필터링 추가
            Model model) {

        // 로그인된 사용자 ID 설정
        String userId = getAuthenticatedUserId();
        String userName = inquireService.getUserName(userId);
        model.addAttribute("userName", userName);
        model.addAttribute("staffId", userId);

        // 요청 파라미터가 비어 있는 경우에만 기본값 설정
        if ((searchType == null || searchType.isEmpty()) && (keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty())) {
            searchType = "manager";
            keyword = userName;
        }

        // 고객 데이터 조회 (검색, 필터링, 페이징 처리)
        List<CustomerDTO> customers = customerService.searchCustomersWithPagination(page, pageSize, searchType, keyword, status);

        // 총 고객 수 및 총 페이지 수 계산
        int totalCustomers = customerService.getTotalCustomerCount(searchType, keyword, status);
        int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

        // 모델에 데이터 추가
        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status); // 현재 필터링 상태 전달

        return "customer/customerList"; // 뷰 이름
    }

    /* ================================================================================================================= */

    // 고객 등록 (POST 요청)
    @PostMapping("/list/insert")
    @ResponseBody
    public ResponseEntity<?> insertCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            // 고객 등록 로직 수행
            customerService.insertCustomer(customerDTO);

            return ResponseEntity.ok().body(Map.of("success", true, "message", "고객이 성공적으로 등록되었습니다.", "redirectUrl", "/customer/list"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "고객 등록에 실패했습니다: " + e.getMessage()));
        }
    }

    /* ================================================================================================================= */
    /* 고객 상세 정보 페이지 */
    // 담당자 검색 API
    @GetMapping("/detail/{custId}/searchManager")
    public ResponseEntity<List<UserDTO>> searchManagers(@PathVariable("custId") String custId, @RequestParam("name") String name) {
        List<UserDTO> managers = customerService.getManagersByName(name);
        return ResponseEntity.ok(managers);
    }

    @GetMapping("/detail/{custId}")
    public String getCustomerDetail(@PathVariable("custId") String custId, Model model) {

        // 로그인된 사용자 ID 설정
        String staffId = getAuthenticatedUserId();
        model.addAttribute("staffId", staffId);

        // 고객 정보 수정 목록 가져오기
        List<CustomerUpdateHistoryDTO> history = customerService.getCustomerHistoryById(custId);

        // 고객 상세 정보 가져오기
        CustomerDTO customer = customerService.getCustomerById(custId);

        // 고객 직업정보 셀렉트 박스
        List<CustomerDTO> custOccpTyCdList = customerService.getCustOccpTyCdList();

        // 특정 고객 최근 상담 내역 1건 가져오기
        TbCounselDTO latestCounsel = counselService.getLatestCounselByCustomerId(custId);

        // 페이지에 출력하기 위한 코드 리스트 조회
        List<CodeDTO> counselCategories = counselService.getCodeListByCl("700");

        model.addAttribute("history", history);
        model.addAttribute("custOccpTyCdList", custOccpTyCdList);
        model.addAttribute("customer", customer);
        model.addAttribute("latestCounsel", latestCounsel);
        model.addAttribute("counselCategories", counselCategories);
        return "customer/customerDetail"; // 고객 상세 정보 페이지로 이동
    }

    /* 수정하기 */
    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            // 인증된 사용자 ID 가져오기
            String staffId = getAuthenticatedUserId();

            // 고객 정보 업데이트
            customerService.updateCustomer(customerDTO, staffId);

            // 고객 수정 내역 생성 및 저장
            List<CustomerUpdateHistoryDTO> historyList = customerService.createAndSaveHistory(customerDTO,customerDTO, staffId);

            // 응답 데이터 생성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "고객 정보가 성공적으로 수정되었습니다.");
            response.put("updateHistory", historyList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("고객 정보 수정 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "고객 정보 수정 중 오류가 발생했습니다."));
        }
    }


    /* ================================================================================================================= */
    /* 찐 메세지 발송 */
    @PostMapping("/sms/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            // 메시지 내용
            String messageContent = (String) request.get("messageContent");

            // 수신자 목록
            List<Map<String, String>> recipients = (List<Map<String, String>>) request.get("recipients");

            // 각 수신자에게 메시지 발송
            for (Map<String, String> recipient : recipients) {
                String custNm = recipient.get("custNm");
                String custTelno = recipient.get("custTelno");

                // 메시지 발송
                boolean sent = smsService.sendCustomMessage(custTelno, messageContent);

                if (!sent) {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "message", "메시지 발송에 실패했습니다."));
                }
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "메시지가 성공적으로 발송되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "오류 발생: " + e.getMessage()));
        }
    }
}
