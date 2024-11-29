package com.team.financial_project.product.controller;

import com.team.financial_project.dto.ProductDTO;
import com.team.financial_project.product.service.ProductService;
import com.team.financial_project.product.service.ProductUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//@RestController
@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProductUserService userService;

    public ProductController(ProductService productService, ProductUserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public Object viewProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "8") int size, // 페이지당 데이터 개수
            @RequestParam(required = false) String prodTyCd, // 상품 유형
            @RequestParam(required = false) String prodCurrStcd, // 상품 현재 상태
            @RequestParam(required = false) String prodPayTyCd, // 납입주기
            @RequestParam(required = false) String prodNm, // 상품명
            @RequestParam(required = false) String dateType, // 날짜 조건 (판매시작일/종료일)
            @RequestParam(required = false) String searchBgnYmd, // 시작 날짜
            @RequestParam(required = false) String searchEndYmd, // 종료 날짜
            @RequestParam(value = "sortColumn", required = false) String sortColumn, // 정렬 기준 컬럼
            @RequestParam(value = "sortDirection", required = false) String sortDirection, // 정렬 방향
            @RequestParam(value = "ajax", required = false, defaultValue = "false") boolean ajax, // AJAX 여부
            Model model, HttpServletRequest request) {

        System.out.println("### search conditions: prodTyCd=" + prodTyCd + ", prodCurrStcd=" + prodCurrStcd +
                ", prodPayTyCd=" + prodPayTyCd + ", prodNm=" + prodNm + ", dateType=" + dateType +
                ", searchBgnYmd=" + searchBgnYmd + ", searchEndYmd=" + searchEndYmd);

        // 조건 설정
        Map<String, Object> searchParams = new HashMap<>();
        boolean hasSearchParams = false;

        // 상품 유형 조건
        if (prodTyCd != null && !prodTyCd.trim().isEmpty()) {
            searchParams.put("prodTyCd", prodTyCd.trim());
            hasSearchParams = true;
        }
        // 판매 상태 조건
        if (prodCurrStcd != null && !prodCurrStcd.trim().isEmpty()) {
            searchParams.put("prodCurrStcd", prodCurrStcd.trim());
            hasSearchParams = true;
        }
        // 납입 주기 조건
        if (prodPayTyCd != null && !prodPayTyCd.trim().isEmpty()) {
            searchParams.put("prodPayTyCd", prodPayTyCd.trim());
            hasSearchParams = true;
        }
        // 상품명 조건
        if (prodNm != null && !prodNm.trim().isEmpty()) {
            searchParams.put("prodNm", prodNm.trim());
            hasSearchParams = true;
        }
        if (dateType != null && searchBgnYmd != null && searchEndYmd != null) {
            switch (dateType) {
                case "prodNtslBgnYmd":
                    searchParams.put("prodNtslBgnYmdRange", new String[]{searchBgnYmd.trim(), searchEndYmd.trim()});
                    hasSearchParams = true;
                    break;
                case "prodNtslEndYmd":
                    searchParams.put("prodNtslEndYmdRange", new String[]{searchBgnYmd.trim(), searchEndYmd.trim()});
                    hasSearchParams = true;
                    break;
                default:
                    System.out.println("Invalid dateType: " + dateType);
                    break;
            }
        }

        // 검색 결과 가져오기
        List<ProductDTO> fullList;
        if (hasSearchParams) {
            // 조건 검색
            fullList = productService.searchProducts(searchParams);
        } else {
            // 전체 결과 조회
            fullList = productService.findAllList().stream()
                    .filter(product -> Objects.equals(product.getProdCurrStcd(), "판매중"))
                    .collect(Collectors.toList());
        }
        log.info("### Retrieved fullList in Controller: {}", fullList);

        // requestURI에 페이지 URL을 포함
        String requestURI = request.getRequestURI();
        model.addAttribute("requestURI", requestURI);

        // 정렬
        if (sortColumn != null && sortDirection != null) {
            Comparator<ProductDTO> comparator = null;
            switch (sortColumn) {
                case "prodNm": // 상품명 기준 정렬 (String)
                    comparator = Comparator.comparing(ProductDTO::getProdNm);
                    break;
                case "prodCd": // 상품코드 기준 정렬 (String)
                    comparator = Comparator.comparing(ProductDTO::getProdCd);
                    break;
                case "prodInstlAmtMin": // 최소 가입 금액 기준 정렬 (String -> Double 변환)
                    comparator = Comparator.comparing(p -> Double.parseDouble(String.valueOf(p.getProdInstlAmtMin())));
                    break;
                case "prodInstlAmtMax": // 최대 가입 금액 기준 정렬 (String -> Double 변환)
                    comparator = Comparator.comparing(p -> Double.parseDouble(String.valueOf(p.getProdInstlAmtMax())));
                    break;
                case "prodAirMin": // 최소 이율 (소수점 포함) 기준 정렬
                    comparator = Comparator.comparing(p -> Double.parseDouble(String.valueOf(p.getProdAirMin())));
                    break;
                case "prodAirMax": // 최대 이율 (소수점 포함) 기준 정렬
                    comparator = Comparator.comparing(p -> Double.parseDouble(String.valueOf(p.getProdAirMax())));
                    break;
                default:
                    // 기본 정렬: 상품명
                    comparator = Comparator.comparing(ProductDTO::getProdNm);
                    break;
            }
            // 내림차순 정렬
            if (comparator != null && "desc".equals(sortDirection)) {
                comparator = comparator.reversed();
            }
            if (comparator != null) {
                fullList.sort(comparator);
            }
        }

        int totalItems = fullList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<ProductDTO> paginatedList = fullList.subList(startIndex, endIndex);

        if (ajax) {
            Map<String, Object> response = new HashMap<>();
            response.put("list", paginatedList);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);
            response.put("totalItems", totalItems); // 총 상품 개수 추가
            return ResponseEntity.ok(response); // JSON 데이터 반환
        }

        model.addAttribute("list", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("productSize", totalItems);
        return "product/product-list"; // HTML 템플릿 반환
    }

    // 상품 상세보기 ---------------------------------------
    @GetMapping("/detail/{prodSn}")
    public String viewProductDetail(@PathVariable("prodSn") Long prodSn, Model model) {
        ProductDTO dto = productService.findById(prodSn);

        // 포맷팅 처리
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dto.getHistList().forEach(hist -> {
            if (hist.getHistCreateAt() != null) {
                hist.setFormattedHistCreateAt(hist.getHistCreateAt().format(formatter));
            }
        });
        model.addAttribute("dto", dto);
        return "product/product-detail";
    }

    //신규 상품 등록 ----------------------------------------------
    @GetMapping("/insert")
    public String productInsertView(Model model) {
        // 신규 productDTO 보내기
        model.addAttribute("dto", new ProductDTO());
        return "product/product-insert";
    }

    // 직원 이름 검색 API -----------------------------------------
    @GetMapping("/user/search")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> searchUserByName(@RequestParam String name) {
        List<Map<String, Object>> users = userService.findByNameContaining(name);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(users); // 검색 결과 반환
    }

    @PostMapping("/insert")
    @ResponseBody
    public ResponseEntity<?> productInsertProc(@RequestBody ProductDTO dto) {
        try {
            log.info("### Received DTO: " + dto);

            // Null 값 검증
            if (dto.getProdNm() == null || dto.getProdTyCd() == null) {
                log.error("필수 값 누락: " + dto);
                return ResponseEntity.badRequest().body("필수 값이 누락되었습니다.");
            }

            // 서비스 호출
            productService.insertProduct(dto);
            return ResponseEntity.ok("상품 등록 성공");
        } catch (Exception e) {
            log.error("상품 등록 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류: " + e.getMessage());
        }
    }

    // 상품 수정 -------------------------------------------------------
    @PostMapping("/detail/{prodSn}/update")
    @ResponseBody
    public ResponseEntity<?> productUpdate(@RequestBody ProductDTO dto) {
        try {
            log.info("### Received DTO: " + dto);

            // Null 값 검증
            if (dto.getProdSn() == null) {
                log.error("필수 값 누락: " + dto);
                return ResponseEntity.badRequest().body("필수 값이 누락되었습니다.");
            }
            // 서비스 호출
            productService.updateProduct(dto);
            ProductDTO updateDTO = productService.findById(dto.getProdSn());
            log.info("### updated dto: "+updateDTO);
            return ResponseEntity.ok("상품 수정 성공");
        } catch (Exception e) {
            log.error("상품 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류: " + e.getMessage());
        }
    }

    // 상품 삭제 --------------------------------------------------
    @PostMapping("/detail/{prodSn}/delete")
    public String productDelete(@PathVariable("prodSn")BigDecimal prodSn, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(prodSn);
        String url = "redirect:/product/detail/" + prodSn;
        redirectAttributes.addFlashAttribute("msg", "해당 상품의 상태가 판매 중지로 변경되었습니다.");
        return url;
    }
}
