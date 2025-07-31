package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.ProductImage;
import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import com.busanit501.shoppingweb_project.repository.ProductImageRepository;
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

    @Override
    public ProductDTO getProductById(Long productId) {
        log.info("ProductService - getProductById: " + productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));

        ProductDTO productDTO =  Product.entityToDTO(product);
        log.info("ProductService 에서 작업중 productDTO.thumbnailFileName : " + productDTO.getThumbnailFileName());
        log.info("ProductService 에서 작업중 productDTO.FileName : " + productDTO.getFileNames());

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
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        log.info("ProductService에서 작업중 수정된 ProductDTO : " + productDTO.getProductName());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));
        // 상품 정보 수정
        product.changeTitleContent(productDTO);
        log.info("ProductService에서 작업중 수정된 Product : " + product.getProductName());
        Product updatedProduct = productRepository.save(product);
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
    private ProductDTO mapProductToDtoWithImage(Product product) {
        ProductDTO dto = Product.entityToDTO(product);
        productImageRepository.findByProduct_ProductIdAndThumbnail(product.getProductId(), true)
                .ifPresent(productImage -> dto.setImageFileName(productImage.getFileName()));
        return dto;
    }

}
