package com.team.financial_project.product.controller;

import com.team.financial_project.product.service.ProductSalesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductSalesController {
    private final ProductSalesService productSalesService;

    public ProductSalesController(ProductSalesService productSalesService) {
        this.productSalesService = productSalesService;
    }

    @GetMapping("/sales")
    public String viewProductSales(){
        return "/product/product-sales";
    }

    @GetMapping("/sales/search")
    public String salesByKeyword(){
        return "/product/product-sales";
    }
}
