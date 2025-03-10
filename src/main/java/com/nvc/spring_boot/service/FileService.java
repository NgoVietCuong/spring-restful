package com.nvc.spring_boot.service;

import com.nvc.spring_boot.util.error.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    @Value("${upload-file.base-uri}")
    private String baseUri;

    public String storeFile(MultipartFile file, String folder) throws URISyntaxException {
        validateFile(file);
        createUploadFolder(baseUri + folder);

        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        URI uri = new URI(baseUri + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("This should never happen", e);
        }
    }

    public void createUploadFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());

        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println("Folder created");
            } catch (IOException e) {
                throw new RuntimeException("This should never happen", e);
            }
        } else {
            System.out.println("Folder already exists");
        }
    }

    public void validateFile(MultipartFile file) throws FileUploadException {
        if (file == null || file.isEmpty()) throw new FileUploadException("File is empty");

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(fileName.toLowerCase()::endsWith);
        if (!isValid) throw new FileUploadException("File type is not allowed");
    }
}
