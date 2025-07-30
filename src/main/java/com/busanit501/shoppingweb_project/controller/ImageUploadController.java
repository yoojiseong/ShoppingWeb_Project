package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.domain.ProductImage;
import com.busanit501.shoppingweb_project.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageUploadController {

    @Value("${com.busanit501.upload.path}")
    private String uploadPath;

    private final ProductImageRepository productImageRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("productId") Long productId) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("파일을 선택해 주세요.", HttpStatus.BAD_REQUEST);
        }

        try {
            String originalFileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String savedFileName = uuid + "_" + originalFileName;

            File destinationFile = new File(uploadPath, savedFileName);

            System.out.println("### 파일 저장될 최종 경로: " + destinationFile.getAbsolutePath());

            file.transferTo(destinationFile);

            ProductImage productImage = ProductImage.builder()
                    .productId(productId)
                    .fileName(savedFileName)
                    .ord(0)
                    .thumbnail(true)
                    .build();

            productImageRepository.save(productImage);

            return new ResponseEntity<>("파일 업로드 성공 및 DB 저장 완료: " + savedFileName, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("파일 업로드 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("알 수 없는 오류 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}