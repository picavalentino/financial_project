package com.team.financial_project.product.controller;

import com.team.financial_project.dto.UserDTO;
import com.team.financial_project.dto.ProductDTO;
import com.team.financial_project.product.service.ProductService;
import com.team.financial_project.product.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RestController
@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    // 상품 전체 목록 조회
    @GetMapping("/list")
    public String viewProductList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 8;
        List<ProductDTO> fullList = productService.findAll().stream()
                .filter(product -> product.getProdCurrStcd().equals("1")) // prodCurrStcd 값이 1인 경우만 필터링
                .collect(Collectors.toList());
        int totalItems = fullList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        // 페이지에 해당하는 상품 추출
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<ProductDTO> paginatedList = fullList.subList(startIndex, endIndex);

        model.addAttribute("list", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "/product/product-list";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false) String prodTyCd,
            @RequestParam(required = false) String prodCurrStcd,
            @RequestParam(required = false) String prodPayTyCd,
            @RequestParam(required = false) String prodNm,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        System.out.println("### search conditions: " + prodTyCd + "/" + prodCurrStcd + "/" + "prodPayTyCd" + "/" + "prodNm");
        // 조건 설정
        Map<String, Object> searchParams = new HashMap<>();
        if (prodTyCd != null && !prodTyCd.isEmpty()) {
            searchParams.put("prodTyCd", prodTyCd);
        }
        if (prodCurrStcd != null && !prodCurrStcd.isEmpty()) {
            searchParams.put("prodCurrStcd", prodCurrStcd);
        }
        if (prodPayTyCd != null && !prodPayTyCd.isEmpty()) {
            searchParams.put("prodPayTyCd", prodPayTyCd);
        }
        if (prodNm != null && !prodNm.isEmpty()) {
            searchParams.put("prodNm", prodNm);
        }

        // 검색 결과 가져오기
        List<ProductDTO> fullList = productService.searchProducts(searchParams);

        // 페이지네이션 처리
        int pageSize = 8;
        int totalItems = fullList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<ProductDTO> paginatedList = fullList.subList(startIndex, endIndex);

        // 모델에 데이터 추가
        model.addAttribute("list", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "/product/product-list";
    }

    @GetMapping("/list/sort")
    @ResponseBody
    public ResponseEntity<Map<String, Object>>  getSortedProductList(
            @RequestParam(defaultValue = "1") int page, // 현재 페이지
            @RequestParam(defaultValue = "prodNm") String sortColumn, // 기본 정렬 컬럼
            @RequestParam(defaultValue = "asc") String sortOrder // 기본 정렬 순서
    ) {
        List<ProductDTO> fullList = productService.findAll();
        int pageSize = 8;
        // 정렬 처리
        if ("prodTyCd".equals(sortColumn)) {
            if ("desc".equals(sortOrder)) {
                fullList.sort((a, b) -> b.getProdTyCd().compareTo(a.getProdTyCd()));
            } else {
                fullList.sort((a, b) -> a.getProdTyCd().compareTo(b.getProdTyCd()));
            }
        } else if ("prodNm".equals(sortColumn)) {
            if ("desc".equals(sortOrder)) {
                fullList.sort((a, b) -> b.getProdNm().compareTo(a.getProdNm()));
            } else {
                fullList.sort((a, b) -> a.getProdNm().compareTo(b.getProdNm()));
            }
        } else if ("prodInstlAmtMin".equals(sortColumn)) {
            if ("desc".equals(sortOrder)) {
                fullList.sort((a, b) -> b.getProdInstlAmtMin().compareTo(a.getProdInstlAmtMin()));
            } else {
                fullList.sort((a, b) -> a.getProdInstlAmtMin().compareTo(b.getProdInstlAmtMin()));
            }
        } else if ("prodInstlAmtMax".equals(sortColumn)) {
            if ("desc".equals(sortOrder)) {
                fullList.sort((a, b) -> b.getProdInstlAmtMax().compareTo(a.getProdInstlAmtMax()));
            } else {
                fullList.sort((a, b) -> a.getProdInstlAmtMax().compareTo(b.getProdInstlAmtMax()));
            }
        } else if ("prodAirMin".equals(sortColumn)) {
            if ("desc".equals(sortOrder)) {
                fullList.sort((a, b) -> b.getProdAirMin().compareTo(a.getProdAirMin()));
            } else {
                fullList.sort((a, b) -> a.getProdAirMin().compareTo(b.getProdAirMin()));
            }
        } else if ("prodAirMax".equals(sortColumn)) {
            if ("desc".equals(sortOrder)) {
                fullList.sort((a, b) -> b.getProdAirMax().compareTo(a.getProdAirMax()));
            } else {
                fullList.sort((a, b) -> a.getProdAirMax().compareTo(b.getProdAirMax()));
            }
        }
        // 페이지네이션 처리
        int totalItems = fullList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<ProductDTO> paginatedList = fullList.subList(startIndex, endIndex);

        // JSON 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("list", paginatedList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }



    // 상품 상세보기
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
        return "/product/product-detail";
    }

    //신규 상품 등록
    @GetMapping("/insert")
    public String productInsertView(Model model) {
        // 신규 productDTO 보내기
        model.addAttribute("dto", new ProductDTO());
        return "/product/product-insert";
    }

    // 직원 이름 검색 API
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
    public String productInsertProc(ProductDTO dto) {
        log.info("### insert product: " + dto);
        productService.insertProduct(dto);
        return "redirect:/product/list";
    }

    // 상품 수정
    @PostMapping("/detail/{prodSn}/update")
    public String productUpdate(ProductDTO dto, RedirectAttributes redirectAttributes) {
        log.info("### update product: "+dto);
        productService.updateProduct(dto);
        String url = "redirect:/product/detail/" + dto.getProdSn();
        redirectAttributes.addFlashAttribute("msg", "상품 정보가 변경되었습니다.");
        return url;
    }

    // 상품 삭제
    @PostMapping("/detail/{prodSn}/delete")
    public String productDelete(@PathVariable("prodSn")BigDecimal prodSn, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(prodSn);
        String url = "redirect:/product/detail/" + prodSn;
        redirectAttributes.addFlashAttribute("msg", "해당 상품의 상태가 판매 중지로 변경되었습니다.");
        return url;
    }
}
