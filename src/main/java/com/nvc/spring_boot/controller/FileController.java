package com.nvc.spring_boot.controller;

import com.nvc.spring_boot.service.FileService;
import com.nvc.spring_boot.util.annotation.ApiMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @ApiMessage("upload file")
    public ResponseEntity<String> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file,
                                             @RequestParam("folder") String folder) throws URISyntaxException {
        return ResponseEntity.ok(fileService.storeFile(file, folder));
    }
}
