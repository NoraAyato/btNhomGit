package com.example.demo.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadUtil {
    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try {
            Path filePath = uploadPath.resolve(fileName);
            multipartFile.transferTo(filePath.toFile());
            return filePath.toString();
        } catch (IOException e) {
            throw new IOException("Could not save image file: " + fileName, e);
        }
    }

    public static String getImagePath(String uploadDir, String fileName) {
        return Paths.get(uploadDir, fileName).toString();
    }
}
