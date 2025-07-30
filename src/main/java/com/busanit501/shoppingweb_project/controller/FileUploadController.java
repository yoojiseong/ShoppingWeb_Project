package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = fileUploadService.saveFile(file);
            return ResponseEntity.ok(imageUrl); // 클라이언트에 이미지 URL 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 실패");
        }
    }
}
