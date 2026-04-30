package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    public String saveFile(MultipartFile file) throws IOException {

        // create folder if not exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Could not create upload directory");
            }
        }

        // generate unique name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        File targetFile = new File(uploadDir + fileName);

        // save file
        file.transferTo(targetFile);

        // return URL path
        return "/uploads/" + fileName;
    }
}