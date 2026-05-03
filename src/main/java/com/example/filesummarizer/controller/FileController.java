package com.example.filesummarizer.controller;

import com.example.filesummarizer.model.FileDocument;
import com.example.filesummarizer.repository.FileDocumentRepository;
import com.example.filesummarizer.service.AiService;
import com.example.filesummarizer.service.FileStorageService;
import com.example.filesummarizer.service.TextExtractionService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    private final FileStorageService fileStorageService;
    private final TextExtractionService textExtractionService;
    private final AiService aiService;
    private final FileDocumentRepository fileDocumentRepository;

    public FileController(
            FileStorageService fileStorageService,
            TextExtractionService textExtractionService,
            AiService aiService,
            FileDocumentRepository fileDocumentRepository) {

        this.fileStorageService = fileStorageService;
        this.textExtractionService = textExtractionService;
        this.aiService = aiService;
        this.fileDocumentRepository = fileDocumentRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<FileDocument>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files) {

        List<FileDocument> results = files.stream().map(file -> {
            try {

                String contentType = file.getContentType();
                String originalFilename = file.getOriginalFilename();

                textExtractionService.validateFileType(contentType, originalFilename);

                String category = textExtractionService.getCategory(contentType);

                byte[] fileBytes = file.getBytes();

                String savedPath = fileStorageService.saveFile(file);

                String summary;

                if ("IMAGE".equals(category)) {
                    summary = aiService.describeImage(fileBytes);
                } else {
                    String extractedText =
                            textExtractionService.extractTextFromBytes(fileBytes, contentType);
                    summary = aiService.summarizeText(extractedText);
                }

                FileDocument doc = FileDocument.builder()
                        .fileName(originalFilename)
                        .fileType(contentType)
                        .filePath(savedPath)
                        .fileSize(file.getSize())
                        .summary(summary)
                        .uploadedAt(LocalDateTime.now())
                        .category(category)
                        .build();

                return fileDocumentRepository.save(doc);

            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to process file: " + file.getOriginalFilename(), e);
            }

        }).toList();

        return ResponseEntity.ok(results);
    }

    @GetMapping
    public ResponseEntity<List<FileDocument>> getAllFiles() {
        return ResponseEntity.ok(
                fileDocumentRepository.findAllByOrderByUploadedAtDesc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDocument> getFileById(@PathVariable String id) {

        FileDocument doc = fileDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("File not found with id: " + id));

        return ResponseEntity.ok(doc);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {

        FileDocument doc = fileDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("File not found with id: " + id));

        byte[] fileBytes = fileStorageService.readFile(doc.getFilePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getFileType()))
                .body(fileBytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {

        FileDocument doc = fileDocumentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("File not found with id: " + id));

        fileStorageService.deleteFile(doc.getFilePath());
        fileDocumentRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}