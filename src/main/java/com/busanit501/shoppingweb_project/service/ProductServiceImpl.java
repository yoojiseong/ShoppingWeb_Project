package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import com.busanit501.shoppingweb_project.repository.ProductImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional()
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;

    @Override
    public ProductDTO getProductById(Long productId) {
        log.info("ProductService 에서 받아온 Id확인중 : " + productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        // ModelMapper를 사용해서 기본 매핑
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new RuntimeException("상품을 찾을 수 없습니다: " + productId);
        }
        Product product = productOptional.get();
        ProductDTO productDTO = new ModelMapper().map(product, ProductDTO.class); // ModelMapper 사용

        log.info("ProductService 에서 받아온 pproductDTO확인 : " + productDTO);

        //  단일 상품 조회 시에도 이미지 파일명 채우기
        productImageRepository.findByProductIdAndThumbnail(product.getProductId(), true)
                .ifPresent(productImage -> {
                    productDTO.setImageFileName(productImage.getFileName());
                    log.info("상품 ID " + productId + "의 이미지 파일명 설정: " + productImage.getFileName());
                });
        return productDTO;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("getAllProducts 호출: 모든 상품 불러오기 시작");
        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> {
                    ProductDTO dto = entityToDto(product);

                    productImageRepository.findByProductIdAndThumbnail(product.getProductId(), true)
                            .ifPresent(productImage -> dto.setImageFileName(productImage.getFileName()));

                    return dto;
                })
                .collect(Collectors.toList());

        log.info("모든 상품 DTO 변환 완료, 개수: " + productDTOs.size());
        return productDTOs;
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        ProductCategory productCategory;
        try {
            productCategory = ProductCategory.fromKoreanName(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 카테고리 값입니다: " + category);
        }

        // 2. Repository 호출
        List<Product> products = productRepository.findByProductTag(productCategory);

        // 3. Entity → DTO 변환
        return products.stream()
                .map(product -> ProductDTO.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .productTag(ProductCategory.valueOf(product.getProductTag().name())) // 문자열로 변환
                        .build())
                .toList();

    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        List<Product> products = productRepository.searchByKeyword(keyword);

        return products.stream()
                .map(product -> ProductDTO.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .productTag(ProductCategory.valueOf(product.getProductTag().name())) // Enum → String
                        .build())
                .toList();
    }

    @Override
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    // [추가] 새 상품 등록 메서드 구현
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO.getProductTag() == null) {
            productDTO.setProductTag(ProductCategory.UNKNOWN);
        }
        Product product = dtoToEntity(productDTO);
        Product saved = productRepository.save(product);
        return entityToDto(saved);
    }

    // [추가] 상품 수정 메서드 구현
    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        Product updated = productRepository.save(product);
        return entityToDto(updated);
    }

    // [추가] 상품 삭제 메서드 구현
    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    // DTO → Entity 변환
    public Product dtoToEntity(ProductDTO dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .image(dto.getImage())
                .productTag(dto.getProductTag())
                .build();
    }

    // Entity → DTO 변환
    public ProductDTO entityToDto(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .image(product.getImage())
                .productTag(product.getProductTag())
                .build();
    }

}
