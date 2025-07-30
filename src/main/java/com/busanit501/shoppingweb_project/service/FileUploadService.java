package com.busanit501.shoppingweb_project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    // 저장할 로컬 폴더 경로 (폴더가 없다면 자동 생성)
    private final String uploadDir = "C:/upload_images";

    public String saveFile(MultipartFile file) throws IOException {
        // 폴더 없으면 생성
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // 고유 파일명 생성 (UUID + 원래 파일명)
        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String savedFilename = uuid + "-" + originalFilename;

        // 저장 경로
        Path savePath = Paths.get(uploadDir, savedFilename);

        // 실제 파일 저장
        file.transferTo(savePath.toFile());

        // 웹에서 접근 가능한 URL 경로 반환 (예: /images/uuid-파일명.jpg)
        return "/images/" + savedFilename;
    }
}
