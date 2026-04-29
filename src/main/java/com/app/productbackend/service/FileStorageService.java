package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    // MUST match WebConfig: file:uploads/
    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {

        // Create folder if not exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        File targetFile = new File(uploadDir + fileName);

        // Save file
        file.transferTo(targetFile);

        // Return URL path (used by frontend)
        return "/uploads/" + fileName;
    }
}