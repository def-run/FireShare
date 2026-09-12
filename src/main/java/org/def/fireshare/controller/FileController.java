package org.def.fireshare.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.def.fireshare.model.File;
import org.def.fireshare.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> fileUpload(@RequestParam("file")MultipartFile file) {
        File uploadedFile = fileService.uploadFile(file);

        return ResponseEntity.ok().body("File uploaded successfully, File ID: " + uploadedFile.getId());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> fileDownload(@PathVariable Long id) {
        Resource downloadedFile = fileService.downloadFile(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + downloadedFile.getFilename() + "\"").body(downloadedFile);
    }
}
