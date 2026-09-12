package org.def.fireshare.service;

import org.def.fireshare.model.File;
import org.def.fireshare.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File uploadFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get("fileStorage");

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            File fileEntity = new File();

            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setSize(file.getSize());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setStoragePath(filePath.toString());
            fileEntity.setUploadedAt(OffsetDateTime.now());

            return fileRepository.save(fileEntity);

        } catch(IOException e) {
            throw new RuntimeException("Failed to upload file, " + e);
        }
    }

    public Resource downloadFile(Long id) {
        try {
            File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

            Path filePath = Paths.get(file.getStoragePath());

            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch(IOException e) {
            throw new RuntimeException("Could not load file", e);
        }
    }
}
