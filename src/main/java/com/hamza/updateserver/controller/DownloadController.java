package com.hamza.updateserver.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/downloads")
public class DownloadController {

    private static final String DOWNLOAD_DIR = "/opt/update-server/downloads/";

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(DOWNLOAD_DIR).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{filename:.+}/info")
    public ResponseEntity<?> getFileInfo(@PathVariable String filename) {
        Path filePath = Paths.get(DOWNLOAD_DIR).resolve(filename).normalize();
        File file = filePath.toFile();

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> info = new HashMap<>();
        info.put("filename", filename);
        info.put("size", file.length());
        info.put("lastModified", file.lastModified());

        return ResponseEntity.ok(info);
    }
}
