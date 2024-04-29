package org.example.app.controller;


import org.apache.commons.io.FilenameUtils;
import org.apache.coyote.BadRequestException;
import org.example.app.model.FileModel;
import org.example.app.repository.FileRepository;
import org.example.app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/manage/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileRepository fileRepository;
    @Value("${spring.file.custom-upload-dir}")
    private String uploadDir;
    @GetMapping("")
    public ResponseEntity<?> findAll() {
        List<FileModel> modelList = fileRepository.findAll();
        return ResponseEntity.ok(modelList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        FileModel model = fileRepository.findById(id).orElseThrow(() -> new BadRequestException("Not found"));
        fileRepository.delete(model);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            Files.copy(file.getInputStream(), filePath);

            FileModel fileModel = new FileModel();
            fileModel.setName(fileName);
            fileModel.setType(file.getContentType());
            fileRepository.save(fileModel);
            return ResponseEntity.ok().body(fileModel);
        } catch (Exception e) {
            throw new BadRequestException("File could not be saved: " + e.getMessage());
        }
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Long fileId) throws Exception {
        FileModel fileModel = fileRepository.findById(fileId).orElseThrow(() -> new BadRequestException("File not found"));

        Path filePath = Paths.get(uploadDir).resolve(fileModel.getName());
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new BadRequestException("File not found");
        }

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        String encodedFilename = URLEncoder.encode(fileModel.getName(), StandardCharsets.UTF_8.toString())
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileModel.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

}
