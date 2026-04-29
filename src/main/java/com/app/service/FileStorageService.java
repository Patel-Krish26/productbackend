package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "src/main/resources/static/uploads/";

    public String saveFile(MultipartFile file) throws IOException {

        // Unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        File targetFile = new File(uploadDir + fileName);

        file.transferTo(targetFile);

        return "/uploads/" + fileName;
    }
}