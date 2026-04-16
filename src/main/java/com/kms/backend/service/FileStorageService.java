package com.kms.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    // This is the folder where images will be stored
    private final Path root = Paths.get("uploads");

    public String save(MultipartFile file) {
        try {
            // 1. Create the 'uploads' folder if it doesn't exist
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            // 2. Create a unique filename to prevent overwriting
            String filename = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());

            // 3. Copy the file to the 'uploads' folder
            Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            // 4. Return the path/URL so it can be saved in the Database
            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}
