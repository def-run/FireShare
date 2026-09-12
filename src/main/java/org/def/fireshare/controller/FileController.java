package org.def.fireshare.controller;

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

    @PostMapping("/upload")
    public void fileUpload(@RequestParam("file")MultipartFile file) throws IOException {
        Path uploadPath = Paths.get("fileStorage");
        Path filePath = uploadPath.resolve(file.getOriginalFilename());

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> fileDownload(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get("fileStorage").resolve(filename);

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"" + filename + "\"").body(resource);
    }
}
