package com.bsuir.storageservice.controller;

import com.bsuir.storageservice.service.DropBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("storage")
@RequiredArgsConstructor
public class DropboxController {

    private final DropBoxService dropBoxService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String path = dropBoxService.uploadFile(file);
            return ResponseEntity.ok(Map.of("filePath", path));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFileById(@RequestParam String filePath) {
        try {
            byte[] data = dropBoxService.downloadFileByPath(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=%s".formatted(filePath.substring(filePath.lastIndexOf("/") + 1)));
            return ResponseEntity.ok().headers(headers).body(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
