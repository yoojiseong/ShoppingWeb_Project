package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.ProductImage;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    // 단건 조회
    ProductDTO getProductById(Long productId);

    // 전체 목록 조회
    List<ProductDTO> getAllProducts();

    // 카테고리별 조회
    List<ProductDTO> getProductsByCategory(String category);

    ProductDTO mapProductToDtoWithImage(Product product);
    // 키워드 검색
    List<ProductDTO> searchProducts(String keyword);

    void saveProduct(Product product);




    // 새 상품 등록 메서드 시그니처 추가
    ProductDTO createProduct(ProductDTO productDTO);

    // 상품 수정 메서드 시그니처 추가
    ProductDTO updateProduct(Long productId, String productName, BigDecimal price, int stock,
                             ProductCategory productTag, MultipartFile thumbnail, List<MultipartFile> details);

    // 상품 삭제 메서드 시그니처 추가
    void deleteProduct(Long productId);

    // 관리자 모드에서 상품과 이미지 함께 저장
    void createProductWithImages(String productName, BigDecimal price, int stock, ProductCategory productTag,
                                 MultipartFile thumbnail,
                                 List<MultipartFile> detailImages);

    // 썸네일 이미지 삭제 메서드
    void deleteThumbnail(Long productId);

    // 상세 이미지 삭제 메서드
    void deleteDetailImage(Long productId, String fileName);
}
