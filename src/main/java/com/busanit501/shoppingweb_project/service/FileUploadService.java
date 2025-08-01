package com.busanit501.shoppingweb_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service

public class FileUploadService {

    @Value("c:\\upload\\WebShopingDetailImg") // 스프링에서 지원해주는 패키지 경로 사용하기.
    private String UPLOAD_DIR;

    public String saveFile(MultipartFile file) throws IOException {
        try {
            String uuid = UUID.randomUUID().toString();
            String originalName = file.getOriginalFilename();

            String savedName = uuid + "-" + originalName;

            File uploadPath = new File(UPLOAD_DIR);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File dest = new File(uploadPath, savedName);
            file.transferTo(dest);

            return savedName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
        }
    }

    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return; // 유효하지 않은 파일명에 대한 초기 검사는 유지합니다.
        }

        File fileToDelete = new File(UPLOAD_DIR, fileName); // File 객체 생성

        try {
            if (fileToDelete.exists()) { // 파일이 존재하는지 확인
                if (fileToDelete.delete()) { // File.delete() 메서드 사용
                    System.out.println("파일 삭제 성공: " + fileName);
                } else {
                    // delete() 메서드가 false를 반환하는 경우 (예: 권한 문제, 디렉토리가 비어있지 않음 등)
                    System.err.println("파일 삭제 실패 (unknown reason): " + fileName);
                    throw new RuntimeException("파일 삭제 실패: " + fileName + ". 파일이 사용 중이거나 권한이 없습니다.");
                }
            } else {
                System.out.println("삭제할 파일을 찾을 수 없음: " + fileName);
            }
        } catch (SecurityException e) {
            // 파일을 삭제할 권한이 없는 경우 발생하는 예외
            System.err.println("파일 삭제 권한 오류: " + fileName + ", 원인: " + e.getMessage());
            throw new RuntimeException("파일 삭제 권한 오류: " + fileName, e);
        } catch (Exception e) { // 그 외 모든 예외를 RuntimeException으로 래핑
            System.err.println("파일 삭제 중 예상치 못한 오류: " + fileName + ", 원인: " + e.getMessage());
            throw new RuntimeException("파일 삭제 중 오류 발생: " + fileName, e);
        }
    }

    private String extractUUID(String fileName){
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
