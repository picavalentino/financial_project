package com.team.financial_project.product.service;

import com.team.financial_project.mapper.ProductMapper;
import com.team.financial_project.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<ProductDTO> findAll() {
        List<ProductDTO> list = productMapper.findAll();
        log.info("product list: " + list);
        return list;
    }

    public ProductDTO findById(Long prodSn) {
        return productMapper.findById(prodSn);
    }

    public void updateProduct(ProductDTO dto) {
        productMapper.updateProduct(dto);
    }

    public void deleteProduct(BigDecimal prodSn) {
        productMapper.deleteProduct(prodSn);
    }

    public void insertProduct(ProductDTO dto) {
        // 상품코드 생성 로직
        if (dto.getProdTyCd().equals("1")) { // 적금
            if (dto.getProdNm().contains("목돈")) {
                // A1로 시작하는 상품코드 사이즈 가져오기
                int size = productMapper.findCdSizeByName("A1");
                // 상품코드 생성: A1 + (size + 1)
                String prodCd;
                if (size < 10) {
                    prodCd = "A10" + (size + 1);
                } else {
                    prodCd = "A1" + (size + 1);
                }
                dto.setProdCd(prodCd);
            } else {
                // A0으로 시작하는 상품코드 사이즈 가져오기
                int size = productMapper.findCdSizeByName("A0");
                // 상품코드 생성: A0 + (size + 1)
                String prodCd;
                if (size < 10) {
                    prodCd = "A00" + (size + 1);
                } else {
                    prodCd = "A0" + (size + 1);
                }
                dto.setProdCd(prodCd);
            }
        } else if (dto.getProdTyCd().equals("2")) { // 예금
            // B0으로 시작하는 상품코드 사이즈 가져오기
            int size = productMapper.findCdSizeByName("B0");
            // 상품코드 생성: B0 + (size + 1)
            String prodCd;
            if (size < 10) {
                prodCd = "B00" + (size + 1);
            } else {
                prodCd = "B0" + (size + 1);
            }
            dto.setProdCd(prodCd);
        } else if (dto.getProdTyCd().equals("3")) { // 대출
            // C0으로 시작하는 상품코드 사이즈 가져오기
            int size = productMapper.findCdSizeByName("C0");
            // 상품코드 생성: C0 + (size + 1)
            String prodCd;
            if (size < 10) {
                prodCd = "C00" + (size + 1);
            } else {
                prodCd = "C0" + (size + 1);
            }
            dto.setProdCd(prodCd);
        }
        System.out.println("### create prodCd: " + dto.getProdCd());
        productMapper.insertProduct(dto);
    }

    public List<ProductDTO> searchProducts(Map<String, Object> searchParams) {
        List<ProductDTO> list = productMapper.searchProducts(searchParams);
        return list;
    }

    public List<ProductDTO> getSortedProducts(String sortColumn, String sortDirection) {
        if (sortColumn == null || sortColumn.isEmpty()) {
            sortColumn = "prodCd"; // 기본 정렬 기준
        }
        if (!sortDirection.equals("asc") && !sortDirection.equals("desc")) {
            sortDirection = "asc"; // 기본 정렬 방향
        }
        return productMapper.getProductsSorted(sortColumn, sortDirection);
    }

}
