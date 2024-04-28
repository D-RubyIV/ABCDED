package org.example.app.service;


import org.apache.commons.io.FilenameUtils;
import org.example.app.model.FileModel;
import org.example.app.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    private String newUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    @Value("${spring.file.custom-upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        } catch (Exception e) {
            throw new Exception("File could not be saved: " + e.getMessage());
        }
    }

    public FileModel downloadFile(Long fileId) throws Exception {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new Exception("A file with Id : " + fileId + " could not be found"));
    }
}
