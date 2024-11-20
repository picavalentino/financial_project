package com.team.financial_project.product.controller;

import com.team.financial_project.product.dto.ProductDTO;
import com.team.financial_project.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/product")
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
        System.out.println(fullList);
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
    public String productInsertProc() {
        return "redirect:/product/list";
    }

    // 상품 수정
    @PatchMapping("/update")
    public String productUpdate() {
        return "redirect:/product/detail";
    }

    // 상품 삭제
    @PatchMapping("/delete")
    public String productDelete() {
        return "redirect:/product/list";
    }
}
