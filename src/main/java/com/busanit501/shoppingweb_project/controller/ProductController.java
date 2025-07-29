package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.service.ImageService;
import com.busanit501.shoppingweb_project.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // MultipartFile 임포트

import java.io.IOException; // IOException 임포트
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService; // ImageService 주입 추가

    @GetMapping // 화면에 렌더링 될 때 category를 선택하지 않으면 모든 상품을 불러오고
    // category를 선택하면 category에 해당 되는 상품만 불러온다.
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) String category) {
        if(category != null && !category.isBlank()){
            log.info(category + " 데이터를 불러옵니다.");
            return productService.getProductsByCategory(category);
        }
        List<ProductDTO> products = productService.getAllProducts();
        log.info("모든 데이터를 불러옵니다. 상품 개수: " + products.size());
        return products;
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword){
        // @RequestParam => URL 에 붙은 ?key=value형식의 값을 받아오게 암시해주는 어노테이션
        List<ProductDTO> products = productService.searchProducts(keyword);
        log.info("'" + keyword + "'가 포함된 데이터 검색 결과: " + products.size() + "개");
        return products;
    }

    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId){
        // @PathVariable => URL에 포함된 변수를 메서드 파라미터로 매핑해주는 어노테이션
        log.info("상품 ID " + productId + " 상세 정보 불러오기");
        return productService.getProductById(productId);
    }

    @PostMapping // 상품 등록 API (이미지 파일 포함)
    public ResponseEntity<ProductDTO> registerProduct(
            @RequestPart("productDTO") ProductDTO productDTO, // 상품 정보를 담은 JSON 데이터
            @RequestPart(value = "files", required = false) List<MultipartFile> files // 이미지 파일들 (필수 아님)
    ) {
        log.info("상품 등록 요청: " + productDTO.getProductName());

        try {
            List<String> imageUrls = new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                // ImageService를 통해 파일 업로드 및 썸네일 생성.
                // 이 메서드는 생성된 썸네일 이미지의 URL 목록을 반환한다고 가정.
                imageUrls = imageService.uploadImagesAndCreateThumbnails(files);
                log.info("업로드된 이미지 썸네일 URL: " + imageUrls);
            }

            // DTO에 이미지 URL 목록 설정.
            // imageUrls 리스트는 썸네일 URL들을 담고 있어.
            if (!imageUrls.isEmpty()) {
                productDTO.setImageUrls(imageUrls); // 모든 썸네일 URL
                productDTO.setThumbnailUrl(imageUrls.get(0)); // 첫 번째 썸네일을 대표 썸네일로
            }

            // ProductService를 통해 상품 정보 (이미지 URL 포함) 저장
            ProductDTO registeredProduct = productService.registerProduct(productDTO);
            log.info("상품 등록 성공: " + registeredProduct.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredProduct);

        } catch (IOException e) {
            log.error("상품 이미지 업로드 또는 등록 중 IO 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("상품 등록 중 예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 주석 처리된 다른 CRUD 메서드들은 필요에 따라 활성화하여 구현하면 됨.
    // @PutMapping("/{productId}")
    // public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO requestDto) {
    //     ProductDTO updatedProduct = productService.updateProduct(productId, requestDto);
    //     return ResponseEntity.ok(updatedProduct);
    // }

    // @DeleteMapping("/{productId}")
    // public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
    //     productService.deleteProduct(productId);
    //     return ResponseEntity.noContent().build();
    // }

}