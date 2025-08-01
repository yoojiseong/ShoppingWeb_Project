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

    // 키워드 검색
    List<ProductDTO> searchProducts(String keyword);

    void saveProduct(Product product);

    // 새 상품 등록 메서드 시그니처 추가 (선택 사항: createProductWithImages와 역할이 겹칠 수 있음)
    // 현재 프로젝트에서 이미지 없는 상품 생성 기능이 필요하다면 유지, 그렇지 않다면 삭제 고려.
    ProductDTO createProduct(ProductDTO productDTO);

    // 상품 수정 메서드 시그니처 추가
    // ProductDTO 기반 updateProduct를 대체하며, 모든 업데이트 요청을 처리합니다.
    ProductDTO updateProduct(Long productId,
                             String productName,
                             BigDecimal price,
                             int stock,
                             ProductCategory productTag,
                             MultipartFile newThumbnail,       // 새로 업로드할 썸네일 파일 (선택 사항)
                             List<MultipartFile> newDetailImages, // 새로 업로드할 상세 이미지 파일 목록 (선택 사항)
                             List<String> deletedImageFileNames); // 삭제할 기존 이미지 파일명 목록 (선택 사항)

    // 상품 삭제 메서드 시그니처 추가
    void deleteProduct(Long productId);

    // 관리자 모드에서 상품과 이미지 함께 저장
    void createProductWithImages(String productName, BigDecimal price, int stock, ProductCategory productTag,
                                 MultipartFile thumbnail,
                                 List<MultipartFile> detailImages);
}
