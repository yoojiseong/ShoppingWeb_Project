package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    @GetMapping // 화면에 렌더링 될 때 category를 선택하지 않으면 모든 상품을 불러오고
    // category를 선택하면 category에 해당 되는 상품만 불러온다.
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) String category) {
        if(category != null && !category.isBlank()){
            log.info(category + "데이터를 불러옵니다.");
            return productService.getProductsByCategory(category);
        }
        List<ProductDTO> products = productService.getAllProducts();
        log.info("모든 데이터를 불러옵니다."+ products);
        return products;
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword){
        // @RequestParam => URL 에 붙은 ?key=value형식의 값을 받아오게 암시해주는 어노테이션
        List<ProductDTO> products = productService.searchProducts(keyword);
        log.info(keyword + "가 포함된 데이터 : "+keyword);
        return products;
    }

//    @GetMapping("/{productId}")
//    public ProductDTO getProductById(@PathVariable Long productId){
//        // @PathVariable => URL에 포함된 변수를 메서드 파라미터로 매핑해주는 어노테이션
//        return productService.getProductById(productId);
////        productService.getProductById(productId) => productDTO
//    }

@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> createProduct(
        @RequestParam String productName,
        @RequestParam BigDecimal price,
        @RequestParam int stock,
        @RequestParam ProductCategory productTag,
        @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
        @RequestParam(value = "details", required = false) List<MultipartFile> details) {

    productService.createProductWithImages(productName, price, stock, productTag, thumbnail, details);
    return ResponseEntity.ok().build();
}

//    @GetMapping
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        List<ProductDTO> products = productService.getAllProducts();
//        return ResponseEntity.ok(products);
//    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }


    @PatchMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @RequestParam String productName,
            @RequestParam BigDecimal price,
            @RequestParam int stock,
            @RequestParam ProductCategory productTag,
            @RequestParam(value = "newThumbnail", required = false) MultipartFile newThumbnail, // 새로 업로드할 썸네일 (선택 사항)
            @RequestParam(value = "newDetails", required = false) List<MultipartFile> newDetails, // 새로 업로드할 상세 이미지들 (선택 사항)
            @RequestParam(value = "deletedImageFileNames", required = false) List<String> deletedImageFileNames) { // 삭제할 파일명 리스트 (선택 사항)

        log.info("ProductController에서 상품 업데이트 요청 - ID: {}", productId);
        log.info("수정된 상품명: {}", productName);
        log.info("삭제할 파일 목록: {}", deletedImageFileNames);
        // newThumbnail과 newDetails 파일 정보도 로깅하면 디버깅에 좋습니다.

        // 서비스 계층의 updateProduct 메서드 호출 (변경된 시그니처에 맞춰 파라미터 전달)
        ProductDTO updatedProduct = productService.updateProduct(
                productId,
                productName,
                price,
                stock,
                productTag,
                newThumbnail,
                newDetails,
                deletedImageFileNames
        );

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}