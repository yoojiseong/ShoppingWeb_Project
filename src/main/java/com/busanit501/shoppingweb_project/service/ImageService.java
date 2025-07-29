// src/main/java/com/busanit501/shoppingweb_project/service/ImageService.java
package com.busanit501.shoppingweb_project.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class ImageService {

    @Value("${image.upload.path}")
    private String uploadPath;

    // 여러 이미지 파일을 업로드하고 원본 및 썸네일 URL 목록을 반환
    public List<String> uploadImagesAndCreateThumbnails(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("Created upload directory: " + uploadDir.toAbsolutePath());
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String savedFileName = uuid + "_" + originalFileName; // 저장될 원본 파일명
                String thumbnailFileName = "s_" + savedFileName; // 저장될 썸네일 파일명 (s_ 접두사)

                File destinationFile = new File(uploadPath, savedFileName); // 원본 파일 경로
                File thumbnailFile = new File(uploadPath, thumbnailFileName); // 썸네일 파일 경로

                // 1. 원본 파일 저장
                file.transferTo(destinationFile);
                log.info("Original file saved: " + destinationFile.getAbsolutePath());

                // 2. 썸네일 생성 및 저장
                Thumbnails.of(destinationFile)
                        .size(150, 150) // 썸네일 크기 (가로 150px, 세로 150px)
                        .toFile(thumbnailFile); // 썸네일 파일 저장
                log.info("Thumbnail saved: " + thumbnailFile.getAbsolutePath());

                // 웹에서 접근 가능한 썸네일 URL을 반환 (이 URL로 프론트에서 이미지를 불러옴)
                imageUrls.add("/displayImage/" + thumbnailFileName);
                // 만약 원본 URL도 필요하면: imageUrls.add("/displayImage/" + savedFileName);
            }
        }
        return imageUrls; // 썸네일 URL 목록 반환
    }
}