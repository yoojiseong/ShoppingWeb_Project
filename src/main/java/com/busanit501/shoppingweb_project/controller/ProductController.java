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


    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @RequestParam("name") String productName,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") int stock,
            @RequestParam("productTag") ProductCategory productTag,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "details", required = false) MultipartFile[] details,

            // 썸네일 삭제 여부 (삭제 버튼 눌렀을 때 true로 넘어옴)
            @RequestParam(value = "deleteThumbnail", required = false, defaultValue = "false") boolean deleteThumbnail,

            // 삭제할 상세 이미지 파일명 리스트 (여러 개가 있을 수 있음)
            @RequestParam(value = "deletedDetailImageNames", required = false) String[] deletedDetailImageNames
    ) {

        log.info("ProductController에서 작업 중 - productId: " + productId);
        log.info("전송된 데이터: productName: " + productName + ", price: " + price);
        log.info("썸네일 삭제 여부: " + deleteThumbnail);
        log.info("삭제된 상세 이미지들: " + (deletedDetailImageNames == null ? "없음" : String.join(", ", deletedDetailImageNames)));

        // 1. 상품 수정 처리
        ProductDTO updatedProduct = productService.updateProduct(
                productId,
                productName,
                price,
                stock,
                productTag,
                thumbnail,
                details
        );

        // 2. 썸네일 삭제 요청이 있으면 삭제 처리
        if (deleteThumbnail) {
            productService.deleteThumbnail(productId);
        }

        // 3. 삭제 요청된 상세 이미지들 삭제 처리
        if (deletedDetailImageNames != null) {
            for (String fileName : deletedDetailImageNames) {
                productService.deleteDetailImage(productId, fileName);
            }
        }

        // 4. 최종 수정된 상품 정보 반환
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // 썸네일 이미지 삭제
    @DeleteMapping("/image/thumbnail/{productId}")
    public ResponseEntity<Void> deleteThumbnail(@PathVariable Long productId) {
        productService.deleteThumbnail(productId); // 썸네일 삭제 서비스 호출
        return ResponseEntity.noContent().build();
    }

    // 상세 이미지 삭제
    @DeleteMapping("/image/detail/{productId}/{fileName}")
    public ResponseEntity<Void> deleteDetailImage(@PathVariable Long productId,
                                                  @PathVariable String fileName) {
        productService.deleteDetailImage(productId, fileName); // 상세 이미지 삭제 서비스 호출
        return ResponseEntity.noContent().build();
    }
}