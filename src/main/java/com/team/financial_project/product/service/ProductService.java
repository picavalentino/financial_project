package com.team.financial_project.product.service;

import com.team.financial_project.dto.ProdHistDTO;
import com.team.financial_project.mapper.ProductMapper;
import com.team.financial_project.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    // 매핑 데이터 정의
    private static final Map<String, String> PAY_TYPE_MAP = Map.of(
            "1", "월납",
            "3", "3개월납",
            "6", "6개월납",
            "8", "연납",
            "9", "일시납"
    );

    private static final Map<String, String> TAX_TYPE_MAP = Map.of(
            "1", "일반과세",
            "2", "세금우대",
            "3", "비과세"
    );

    private static final Map<String, String> CURR_STCD_TYPE_MAP = Map.of(
            "0", "판매대기",
            "1", "판매중",
            "2", "판매종료"
    );

    private static final Map<String, String> PROD_TYPE_MAP = Map.of(
            "1", "적금",
            "2", "예금",
            "3", "대출"
    );

    private static final Map<String, String> SBSTG_TYPE_MAP = Map.of(
            "1", "일반개인",
            "2", "청년생활지원",
            "3", "장애자우선지원"
    );

    // 단일 ProductDTO에 매핑 적용
    private void applyMappingsToProduct(ProductDTO product) {
        if (product.getProdPayTyCd() != null) {
            product.setProdPayTyCd(PAY_TYPE_MAP.getOrDefault(product.getProdPayTyCd(), product.getProdPayTyCd()));
        }
        if (product.getProdIntTaxTyCd() != null) {
            product.setProdIntTaxTyCd(TAX_TYPE_MAP.getOrDefault(product.getProdIntTaxTyCd(), product.getProdIntTaxTyCd()));
        }
        if (product.getProdCurrStcd() != null) {
            product.setProdCurrStcd(CURR_STCD_TYPE_MAP.getOrDefault(product.getProdCurrStcd(), product.getProdCurrStcd()));
        }
        if (product.getProdTyCd() != null) {
            product.setProdTyCd(PROD_TYPE_MAP.getOrDefault(product.getProdTyCd(), product.getProdTyCd()));
        }
        if (product.getProdSbstgTyCd() != null) {
            product.setProdSbstgTyCd(SBSTG_TYPE_MAP.getOrDefault(product.getProdSbstgTyCd(), product.getProdSbstgTyCd()));
        }
    }

    // 전체 ProductDTO 리스트에 매핑 적용
    private void applyMappingsToProducts(List<ProductDTO> products) {
        for (ProductDTO product : products) {
            applyMappingsToProduct(product);
        }
    }

    // 전체 리스트 조회
    public List<ProductDTO> findAllList() {
        List<ProductDTO> list = productMapper.findAllList();
        applyMappingsToProducts(list); // 매핑 적용
        log.info("product list: " + list);
        return list;
    }

    // 검색 조건에 따른 리스트 조회
    public List<ProductDTO> searchProducts(Map<String, Object> searchParams) {
        List<ProductDTO> list = productMapper.searchProducts(searchParams);
        applyMappingsToProducts(list); // 매핑 적용
        log.info("searched product list: " + list);
        return list;
    }

    // 정렬된 리스트 조회
    public List<ProductDTO> getSortedProducts(String sortColumn, String sortDirection) {
        if (sortColumn == null || sortColumn.isEmpty()) {
            sortColumn = "prodCd"; // 기본 정렬 기준
        }
        if (!"asc".equals(sortDirection) && !"desc".equals(sortDirection)) {
            sortDirection = "asc"; // 기본 정렬 방향
        }
        List<ProductDTO> list = productMapper.getProductsSorted(sortColumn, sortDirection);
        applyMappingsToProducts(list); // 매핑 적용
        log.info("sorted product list: " + list);
        return list;
    }

    // 단일 제품 조회
    public ProductDTO findById(BigDecimal prodSn) {
        ProductDTO product = productMapper.getProductById(prodSn);

        // histList 초기화
        if (product != null && product.getHistList() == null) {
            product.setHistList(new ArrayList<>());
        }

        List<ProdHistDTO> prodHist = productMapper.getProdHistById(prodSn);
        if (prodHist != null) {
            for (ProdHistDTO x : prodHist) {
                product.getHistList().add(x);
            }
        }

        if (product != null) {
            applyMappingsToProduct(product); // 매핑 적용
        }
        log.info("found product: " + product);
        return product;
    }

    @Transactional
    public void updateProduct(ProductDTO dto, String userId) {
        productMapper.updateProduct(dto, userId);
    }

    @Transactional
    public void deleteProduct(BigDecimal prodSn) {
        productMapper.deleteProduct(prodSn);
    }

    @Transactional
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

    @Transactional
    public void updateProductStatuses() {
        List<ProductDTO> productsBeforeUpdate = productMapper.findAllList(); // 상태 변경 전 조회
        int updatedRows = productMapper.updateProductStatus();
        List<ProductDTO> productsAfterUpdate = productMapper.findAllList();  // 상태 변경 후 조회

        if (updatedRows > 0) {
            log.info("상품 상태 변경 완료: {}개의 상품 상태가 업데이트되었습니다.", updatedRows);

            // 상태 변경된 상품 로깅
            for (int i = 0; i < productsBeforeUpdate.size(); i++) {
                ProductDTO before = productsBeforeUpdate.get(i);
                ProductDTO after = productsAfterUpdate.get(i);

                if (!before.getProdCurrStcd().equals(after.getProdCurrStcd())) {
                    log.info("상품 ID: {}, 상태 변경: {} -> {}",
                            before.getProdSn(), before.getProdCurrStcd(), after.getProdCurrStcd());
                }
            }
        } else {
            log.warn("상품 상태 변경 작업이 수행되지 않았습니다. (업데이트된 상품 없음)");
        }
    }

}
