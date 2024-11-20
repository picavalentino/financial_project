package com.team.financial_project.product.controller;

import com.team.financial_project.product.dto.ProductDTO;
import com.team.financial_project.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@RestController
@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 전체 목록 조회
    @GetMapping("/list")
    public String viewProductList(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 8;
        List<ProductDTO> fullList = productService.findAll();
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

    // 상품 조건 검색
    @GetMapping("/list/search")
    public String searchProduct() {
        return "/product/product-list";
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
