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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // 상품 수정 메서드 구현
    @Override
    public ProductDTO updateProduct(Long productId, String productName, BigDecimal price, int stock,
                                    ProductCategory productTag, MultipartFile thumbnail, List<MultipartFile> details) {
        log.info("ProductService에서 작업중 수정된 ProductDTO : " + productName);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));
        // 상품 정보 수정
        product.changeTitleContent( productName , price , stock , productTag);
        // ✅ 썸네일 이미지 처리
        if (thumbnail != null && !thumbnail.isEmpty()) {
            boolean hasThumbnail = product.getThumbnailImage().isPresent(); // thumbnail == true인 이미지가 있는지 확인

            if (!hasThumbnail) {
                try {
                    String savedFileName = fileUploadService.saveFile(thumbnail);

                    ProductImage thumbnailImage = ProductImage.builder()
                            .fileName(savedFileName)
                            .ord(0)
                            .thumbnail(true) // 썸네일임을 명시
                            .build();

                    product.addImage(thumbnailImage);
                    log.info("새 썸네일 추가됨: " + savedFileName);
                    productImageRepository.save(thumbnailImage);
                } catch (IOException e) {
                    throw new RuntimeException("썸네일 이미지 저장 실패", e);
                }
            } else {
                log.info("기존 썸네일이 있어서 새로 추가하지 않음");
            }
        }

        // ✅ 상세 이미지 처리
        if (details != null && !details.isEmpty()) {
            int ord = 1; // 썸네일이 ord 0이라면 상세는 그 다음부터

            for (MultipartFile detail : details) {
                if (detail != null && !detail.isEmpty()) {
                    try {
                        String savedFileName = fileUploadService.saveFile(detail);

                        ProductImage detailImage = ProductImage.builder()
                                .fileName(savedFileName)
                                .ord(ord++)
                                .thumbnail(false) // 상세 이미지이므로 false
                                .build();

                        product.addImage(detailImage);
                        log.info("상세 이미지 추가됨: " + savedFileName);
                        productImageRepository.save(detailImage);
                    } catch (IOException e) {
                        throw new RuntimeException("상세 이미지 저장 실패", e);
                    }
                }
            }
        }

        // 엔티티 변경은 트랜잭션 내부에서 자동 반영됨
        return modelMapper.map(product, ProductDTO.class);
    }

    // 상품 삭제 메서드 구현
    @Override
    public void deleteProduct(Long productId) {
        // 1. 상품 이미지 목록 조회
        List<ProductImage> images = productImageRepository.findByProduct_ProductId(productId);

        // 2. 실제 파일 삭제
        for (ProductImage img : images) {
            try {
                Path filePath = Paths.get(fileUploadService.getFullPath(img.getFileName()));
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("파일 삭제 실패: " + img.getFileName(), e);
            }
        }


        // 3. 상품 이미지 레코드 삭제 (cascade 설정 없으면 직접 삭제)
        productImageRepository.deleteAll(images);

        // 4. 상품 삭제
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

    @Override
    public void deleteThumbnail(Long productId) {
        // 1. 상품 존재 확인 (없으면 예외 발생)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + productId));

        // 2. 해당 상품의 썸네일 이미지 조회 (thumbnail = true)
        Optional<ProductImage> thumbnailOpt = productImageRepository.findByProduct_ProductIdAndThumbnail(productId, true);

        if (thumbnailOpt.isPresent()) {
            ProductImage thumbnail = thumbnailOpt.get();

            // 3. DB에서 썸네일 이미지 데이터 삭제
            productImageRepository.delete(thumbnail);

            // 4. 실제 파일 시스템(서버 폴더)에서 썸네일 이미지 파일 삭제
            try {
                String fullPath = fileUploadService.getFullPath(thumbnail.getFileName());  // 저장된 이미지 경로 반환
                Files.deleteIfExists(Paths.get(fullPath));  // 해당 경로 파일이 존재하면 삭제
            } catch (Exception e) {
                // 삭제 실패 시 RuntimeException 발생시키고 로그 출력
                throw new RuntimeException("썸네일 이미지 파일 삭제 실패: " + e.getMessage(), e);
            }
        } else {
            // 썸네일 이미지가 없으면 예외 발생
            throw new IllegalArgumentException("삭제할 썸네일 이미지가 없습니다.");
        }
    }

    @Override
    public void deleteDetailImage(Long productId, String fileName) {
        // 1. 상품 존재 확인 (없으면 예외 발생)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + productId));

        // 2. 해당 상품과 파일명으로 상세 이미지 조회
        Optional<ProductImage> detailImageOpt = productImageRepository.findByProduct_ProductIdAndFileName(productId, fileName);

        if (detailImageOpt.isPresent()) {
            ProductImage detailImage = detailImageOpt.get();

            // 3. DB에서 상세 이미지 데이터 삭제
            productImageRepository.delete(detailImage);

            // 4. 실제 파일 시스템에서 상세 이미지 파일 삭제
            try {
                String fullPath = fileUploadService.getFullPath(detailImage.getFileName());  // 이미지 전체 경로 반환
                Files.deleteIfExists(Paths.get(fullPath));  // 파일 존재 시 삭제
            } catch (Exception e) {
                // 삭제 실패 시 예외 발생 및 로그 출력
                throw new RuntimeException("상세 이미지 파일 삭제 실패: " + e.getMessage(), e);
            }
        } else {
            // 상세 이미지가 없으면 예외 발생
            throw new IllegalArgumentException("삭제할 상세 이미지가 없습니다.");
        }
    }

}
