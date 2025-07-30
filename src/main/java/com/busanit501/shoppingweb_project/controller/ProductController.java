package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.busanit501.shoppingweb_project.dto.PageResponseDTO;
import com.busanit501.shoppingweb_project.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    /**
     * [lsr/feature/paging] 상품 목록/검색 페이징 API
     * 이유: 기존의 목록 조회, 카테고리별 조회, 검색 기능을 페이징이 가능한 단일 API로 통합합니다.
     * 
     * @param pageRequestDTO 페이징 및 검색 조건(keyword, type)을 담고 있습니다.
     * @return 페이징된 상품 목록 데이터
     */
    @GetMapping
    public PageResponseDTO<ProductDTO> getProductList(PageRequestDTO pageRequestDTO) {
        log.info("getProductList ....." + pageRequestDTO);
        return productService.getProductList(pageRequestDTO);
    }

    /*
     * ==================================================
     * [lsr/feature/paging] 기존 코드 보존 (주석 처리)
     * - 이유: 아래 메소드들은 페이징 기능이 없는 버전이며, 새로운 getProductList 메소드로 통합되었습니다.
     * ==================================================
     */
    // @GetMapping // 화면에 렌더링 될 때 category를 선택하지 않으면 모든 상품을 불러오고
    // // category를 선택하면 category에 해당 되는 상품만 불러온다.
    // public List<ProductDTO> getAllProducts(@RequestParam(required = false) String
    // category) {
    // if(category != null && !category.isBlank()){
    // log.info(category + "데이터를 불러옵니다.");
    // return productService.getProductsByCategory(category);
    // }
    // List<ProductDTO> products = productService.getAllProducts();
    // log.info("모든 데이터를 불러옵니다."+ products);
    // return products;
    // }
    //
    // @GetMapping("/search")
    // public List<ProductDTO> searchProducts(@RequestParam String keyword){
    // // @RequestParam => URL 에 붙은 ?key=value형식의 값을 받아오게 암시해주는 어노테이션
    // List<ProductDTO> products = productService.searchProducts(keyword);
    // log.info(keyword + "가 포함된 데이터 : "+keyword);
    // return products;
    // }

    // @GetMapping("/{productId}")
    // public ProductDTO getProductById(@PathVariable Long productId){
    // // @PathVariable => URL에 포함된 변수를 메서드 파라미터로 매핑해주는 어노테이션
    // return productService.getProductById(productId);
    //// productService.getProductById(productId) => productDTO
    // }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO requestDto) {
        ProductDTO responseDto = productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // @GetMapping
    // public ResponseEntity<List<ProductDTO>> getAllProducts() {
    // List<ProductDTO> products = productService.getAllProducts();
    // return ResponseEntity.ok(products);
    // }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO requestDto) {
        ProductDTO updatedProduct = productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}