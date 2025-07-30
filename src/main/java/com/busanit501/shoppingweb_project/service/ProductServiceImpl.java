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
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDTO getProductById(Long productId) {
        log.info("ProductService - getProductById: " + productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));

        ProductDTO productDTO = entityToDto(product);

        productImageRepository.findByProductIdAndThumbnail(product.getProductId(), true)
                .ifPresent(productImage -> productDTO.setImageFileName(productImage.getFileName()));

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

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO.getProductTag() == null) {
            productDTO.setProductTag(ProductCategory.UNKNOWN);
        }
        Product product = dtoToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return entityToDto(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. id=" + productId));
        // ModelMapper를 사용하거나 수동으로 필드 업데이트
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setProductTag(productDTO.getProductTag());
        Product updatedProduct = productRepository.save(product);
        return entityToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    // Helper method to map Product to ProductDTO and attach image filename
    private ProductDTO mapProductToDtoWithImage(Product product) {
        ProductDTO dto = entityToDto(product);
        productImageRepository.findByProductIdAndThumbnail(product.getProductId(), true)
                .ifPresent(productImage -> dto.setImageFileName(productImage.getFileName()));
        return dto;
    }

}
