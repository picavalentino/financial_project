package com.team.financial_project.customer.controller;

import com.team.financial_project.customer.service.CustomerService;
import com.team.financial_project.dto.CustomerDTO;
import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.management.service.ManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ManagementService managementService;

    public CustomerController(CustomerService customerService, ManagementService managementService) {
        this.customerService = customerService;
        this.managementService = managementService;
    }

    @GetMapping("/list")
    public String getCustomerList(@RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                  Model model) {
        // 고객 목록 조회 및 총 고객 수 조회
        List<CustomerDTO> customers = customerService.getCustomersWithPagination(page, pageSize);
        int totalCustomers = customerService.getTotalCustomerCount();
        int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

        // 모델에 데이터 추가
        model.addAttribute("customers", customers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "customer/customerList";
    }

    /* ================================================================================================================= */
    @GetMapping("/list/message")
    public ResponseEntity<?> getCustomerMessage(@RequestParam("custId") String custId) {
        try {
            // custId를 이용해 고객 정보를 가져옴 (예: 데이터베이스에서)
            CustomerDTO customer = customerService.getCustomerById(custId);

            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("고객 정보를 찾을 수 없습니다.");
            }
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("고객 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    /* ================================================================================================================= */

    /* 고객 상세 정보 페이지 */
    @GetMapping("/detail/{custId}")
    public String getCustomerDetail(@PathVariable("custId") String custId, Model model) {
        // 고객 상세 정보 가져오기
        CustomerDTO customer = customerService.getCustomerById(custId);
        model.addAttribute("customer", customer);
        return "customer/customerDetail"; // 고객 상세 정보 페이지로 이동
    }

    /* ================================================================================================================= */
   /* 수정하기 */
    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            customerService.updateCustomer(customerDTO);
            return ResponseEntity.ok("고객 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();  // 예외의 전체 스택 트레이스를 로그에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("고객 정보 수정에 실패했습니다. 오류 메시지: " + e.getMessage());
        }
    }

    /* ================================================================================================================= */

    // 고객 목록 출력 페이지
    @GetMapping("/list/print")
    public String customerListPrint() {
        return "customer/customerListModal"; // 고객 목록 출력 페이지로 이동
    }
    /* ================================================================================================================= */

    @GetMapping("/list/insert")
    public String showInsertForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());  // 빈 객체 전달
        return "customer/list/insert";
    }

    // 고객 등록 (POST 요청)
    @PostMapping("/list/insert")
    @ResponseBody
    public ResponseEntity<?> insertCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            // 고객 등록 로직 수행
            customerService.insertCustomer(customerDTO);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "고객이 성공적으로 등록되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "고객 등록에 실패했습니다: " + e.getMessage()));
        }
    }

    // 고객 상세 정보 출력 페이지
    @GetMapping("/detail/id/print")
    public String customerDetailPrint() {
        return "customer/customerDetailPrint"; // 고객 상세 출력 페이지로 이동
    }

    /* ================================================================================================================= */
    // 담당자 검색 API
    @GetMapping("/detail/{custId}/searchManager")
    public ResponseEntity<List<UserDTO>> searchManagers(@PathVariable("custId") String custId, @RequestParam("name") String name) {
        List<UserDTO> managers = managementService.getManagersByName(name);
        return ResponseEntity.ok(managers);
    }
    /* ================================================================================================================= */
}
