package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.ProductImage;
import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import com.busanit501.shoppingweb_project.repository.ProductImageRepository;
import com.busanit501.shoppingweb_project.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional()
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;
    private final FileUploadService fileUploadService;
    private final ReviewRepository reviewRepository;

    @Override
    public ProductDTO getProductById(Long productId) {
        log.info("ProductService - getProductById: " + productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));

        ProductDTO productDTO =  Product.entityToDTO(product);
        log.info("ProductService 에서 작업중 productDTO.thumbnailFileName : " + productDTO.getThumbnailFileName());
        log.info("ProductService 에서 작업중 productDTO.FileName : " + productDTO.getFileNames());
        log.info("ProductService 에서 작업중 productDTO.avgRate : " + product.getAvgRate());

        // 썸네일 이미지 설정
        productImageRepository.findByProduct_ProductIdAndThumbnail(product.getProductId(), true)
                .ifPresent(productImage -> productDTO.setImageFileName(productImage.getFileName()));
        log.info("ProductService 에서 작업중 productDTO.thumbnailFileName : " + productDTO.getThumbnailFileName());
        log.info("ProductService 에서 작업중 productDTO.FileName : " + productDTO.getFileNames());

        return productDTO;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("ProductService - getAllProducts 호출");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapProductToDtoWithImage)
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        log.info("ProductService - getProductsByCategory: " + category);
        ProductCategory productCategory = ProductCategory.fromKoreanName(category);
        List<Product> products = productRepository.findByProductTag(productCategory);
        return products.stream()
                .map(this::mapProductToDtoWithImage)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        log.info("ProductService - searchProducts: " + keyword);
        List<Product> products = productRepository.searchByKeyword(keyword);
        return products.stream()
                .map(this::mapProductToDtoWithImage)
                .collect(Collectors.toList());
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // 새 상품 등록 메서드 구현
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO.getProductTag() == null) {
            productDTO.setProductTag(ProductCategory.UNKNOWN);
        }
        Product product = productRepository.findByProductId(productDTO.getProductId());
        Product savedProduct = productRepository.save(product);
        return Product.entityToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long productId,
                                    String productName,
                                    BigDecimal price,
                                    int stock,
                                    ProductCategory productTag,
                                    MultipartFile newThumbnail,       // 새로 업로드할 썸네일 파일
                                    List<MultipartFile> newDetailImages, // 새로 업로드할 상세 이미지 파일 목록
                                    List<String> deletedImageFileNames) { // 삭제할 기존 이미지 파일명 목록

        log.info("ProductService - updateProduct 호출 (이미지 포함): productId={}, deletedImageCount={}", productId, deletedImageFileNames != null ? deletedImageFileNames.size() : 0);

        // 1. 상품 엔티티 조회 (없으면 예외 발생)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productId));

        // 2. 상품 기본 정보 업데이트
        // ProductDTO를 직접 받지 않으므로 임시 ProductDTO 객체를 생성하여 changeTitleContent 호출
        product.changeTitleContent(ProductDTO.builder()
                .productName(productName)
                .price(price)
                .stock(stock)
                .productTag(productTag)
                .build());

        // 3. 삭제할 기존 이미지 처리 (파일 시스템 및 DB에서 제거)
        if (deletedImageFileNames != null && !deletedImageFileNames.isEmpty()) {
            // Product 엔티티의 imageSet에서 해당 파일명을 가진 이미지 엔티티 제거
            product.getImageSet().removeIf(productImage -> {
                boolean shouldDelete = deletedImageFileNames.contains(productImage.getFileName());
                if (shouldDelete) {
                    fileUploadService.deleteFile(productImage.getFileName()); // 실제 파일 시스템에서 삭제
                    log.info("기존 이미지 삭제 (DB & File System): " + productImage.getFileName());
                }
                return shouldDelete; // true를 반환하면 Set에서 해당 요소가 제거됩니다.
            });
        }

        // 4. 새 썸네일 이미지 처리 (기존 썸네일 교체 또는 추가)
        if (newThumbnail != null && !newThumbnail.isEmpty()) {
            // 기존 썸네일이 있다면 제거하고 파일 시스템에서도 삭제합니다.
            product.getImageSet().stream()
                    .filter(ProductImage::isThumbnail) // 썸네일인 이미지를 찾음
                    .findFirst() // 첫 번째 썸네일 (보통 하나만 존재)
                    .ifPresent(existingThumbnail -> {
                        product.getImageSet().remove(existingThumbnail); // Product의 Set에서 제거 (JPA cascade로 DB에서도 삭제)
                        fileUploadService.deleteFile(existingThumbnail.getFileName()); // 파일 시스템에서 삭제
                        log.info("기존 썸네일 교체를 위해 삭제: " + existingThumbnail.getFileName());
                    });

            try {
                // 새 썸네일 파일 저장 및 ProductImage 엔티티 생성
                String savedFileName = fileUploadService.saveFile(newThumbnail);
                ProductImage newThumbnailEntity = ProductImage.builder()
                        .fileName(savedFileName)
                        .ord(0) // 썸네일은 보통 0번 순서
                        .thumbnail(true)
                        .product(product) // 연관 관계 설정 (매우 중요)
                        .build();
                product.addImage(newThumbnailEntity); // Product 엔티티에 새 썸네일 추가 (JPA cascade로 DB에 저장)
                log.info("새 썸네일 추가: " + savedFileName);
            } catch (IOException e) {
                log.error("새 썸네일 이미지 저장 실패: " + e.getMessage(), e);
                // 예외 처리: 이미지 저장 실패 시 롤백 또는 사용자에게 에러 메시지 전달
            }
        }

        // 5. 새 상세 이미지 처리 (추가)
        if (newDetailImages != null && !newDetailImages.isEmpty()) {
            // 현재 상세 이미지들 중 가장 높은 ord 값을 찾아 새 이미지의 순서에 사용 (썸네일 제외)
            int currentMaxOrd = product.getImageSet().stream()
                    .filter(img -> !img.isThumbnail()) // 썸네일 제외
                    .mapToInt(ProductImage::getOrd)
                    .max()
                    .orElse(0); // 상세 이미지가 없으면 0부터 시작

            int order = currentMaxOrd + 1; // 새 이미지의 시작 순서
            for (MultipartFile file : newDetailImages) {
                try {
                    if (!file.isEmpty()) {
                        // 새 상세 이미지 파일 저장 및 ProductImage 엔티티 생성
                        String savedFileName = fileUploadService.saveFile(file);
                        ProductImage detailImageEntity = ProductImage.builder()
                                .fileName(savedFileName)
                                .ord(order++) // 순서 증가
                                .thumbnail(false)
                                .product(product) // 연관 관계 설정 (매우 중요)
                                .build();
                        product.addImage(detailImageEntity); // Product 엔티티에 새 상세 이미지 추가
                        log.info("새 상세 이미지 추가: " + savedFileName);
                    }
                } catch (IOException e) {
                    log.error("새 상세 이미지 저장 실패: " + e.getMessage(), e);
                    // 예외 처리: 이미지 저장 실패 시 롤백 또는 사용자에게 에러 메시지 전달
                }
            }
        }

        // 6. 변경된 Product 엔티티 최종 저장 (JPA cascade 설정으로 ProductImage도 함께 처리됩니다.)
        Product updatedProduct = productRepository.save(product);
        log.info("ProductService - 상품 업데이트 완료: {}", updatedProduct.getProductName());

        // 7. 업데이트된 Product를 DTO로 변환하여 반환
        return Product.entityToDTO(updatedProduct);
    }

    // 상품 삭제 메서드 구현
    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void createProductWithImages(String productName, BigDecimal price, int stock, ProductCategory productTag, MultipartFile thumbnail, List<MultipartFile> detailImages) {
        Product product = Product.builder()
                .productName(productName)
                .price(price)
                .stock(stock)
                .productTag(productTag)
                .build();
        log.info("ProductService에서 작업중 관리자가 생성한 Product : "+ product.getProductName());
        productRepository.save(product);

        // 2. 섬네일 이미지 저장 thumnail = true로 저장
        if(thumbnail != null && !thumbnail.isEmpty()) {
            try {
                String savedFileName = fileUploadService.saveFile(thumbnail);
                ProductImage thumbnailEntity = ProductImage.builder()
                        .fileName(savedFileName)
                        .ord(0)
                        .thumbnail(true)
                        .build();
                product.addImage(thumbnailEntity);
                log.info("ProductService에서 작업중 썸네일 이미지인지 확인 : " + thumbnailEntity.getFileName());
                productImageRepository.save(thumbnailEntity);
            }catch(IOException e) {
                e.printStackTrace();
            }

            //상세 이미지 DB에 저장 thumnail = false로 표시해주기
            if(detailImages != null && !detailImages.isEmpty()) {
                int order=1;
                for(MultipartFile file : detailImages){
                    try {
                        if(!file.isEmpty()) {
                            String savedFileName = fileUploadService.saveFile(file);
                            ProductImage detailImageEntity = ProductImage.builder()
                                    .fileName(savedFileName)
                                    .ord(order++)
                                    .thumbnail(false)
                                    .build();
                            product.addImage(detailImageEntity);
                            log.info("ProductService에서 작업중 썸네일 이미지인지 확인 : " + detailImageEntity.getFileName());
                            productImageRepository.save(detailImageEntity);
                        }
                    }catch(IOException e) {}
                }
            }
        }
    }

    // Helper method to map Product to ProductDTO and attach image filename
    @Override
    public ProductDTO mapProductToDtoWithImage(Product product) {
        ProductDTO dto = Product.entityToDTO(product);
        productImageRepository.findByProduct_ProductIdAndThumbnail(product.getProductId(), true)
                .ifPresent(productImage -> dto.setImageFileName(productImage.getFileName()));
        return dto;
    }

}
