package com.korit.post_mini_project_back.service;

import com.korit.post_mini_project_back.entity.ImageFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileService {


    @Value("${user.dir}")
    private String projectPath;


    public List<ImageFile> upload(String category, List<MultipartFile> files) {
        List<ImageFile> imageFiles = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return null;
        }

        for (MultipartFile file : files ) {
            String originalFileName = file.getOriginalFilename();
            String newFilename = UUID.randomUUID().toString() + "_" + originalFileName;
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1); // 확장자
            Path uploadDirPath = Paths.get(projectPath, "upload", category);
            try {
                // 경로가 존재하지 않으면 자동으로 전체 폴더 경로를 생성
                Files.createDirectories(uploadDirPath);
            } catch (IOException e) {}
            Path filePath = uploadDirPath.resolve(newFilename);

            try {
                file.transferTo(filePath);
            } catch (IOException e) {}

            imageFiles.add(ImageFile.builder()
                    .originalFilename(originalFileName)
                    // substring의 괄호를 먼저 닫고, 그 결과 문자열에 replaceAll을 실행합니다.
                    .filePath(filePath.toString()
                            .substring(filePath.toString().indexOf("upload") + "upload".length())
                            .replaceAll("\\\\", "/"))
                    .extension(extension)
                    .category(category) // 이 부분은 필요에 따라 category 변수로 변경하세요.
                    .size(file.getSize())
                    .build());

//            System.out.println(newFilename);
//            System.out.println(filePath);
        }

        return imageFiles;
    }

}
