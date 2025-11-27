package com.hamza.updateserver.controller;

import com.hamza.updateserver.model.UpdateInfo;
import com.hamza.updateserver.service.FileStorageService;
import com.hamza.updateserver.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UpdateService updateService;
    private final FileStorageService fileStorageService;

    /**
     * رفع ملف التحديث
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadUpdate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("version") String version,
            @RequestParam("changelogAr") String changelogAr,
            @RequestParam("changelogEn") String changelogEn,
            @RequestParam(value = "required", defaultValue = "false") boolean required,
            @RequestParam(value = "minSupportedVersion", required = false) String minVersion) {

        try {
            // التحقق من صحة الملف
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "الملف فارغ"));
            }

            if (!file.getOriginalFilename().endsWith(".jar")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "يجب أن يكون الملف من نوع JAR"));
            }

            // حفظ الملف
            String fileName = fileStorageService.storeFile(file, version);
            String downloadUrl = fileStorageService.getFileUrl(fileName);
            String checksum = fileStorageService.calculateChecksum(file);

            // إنشاء معلومات التحديث
            UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setVersion(version);
            updateInfo.setReleaseDate(LocalDateTime.now());
            updateInfo.setDownloadUrl(downloadUrl);
            updateInfo.setFileSize(file.getSize());
            updateInfo.setChecksum(checksum);
            updateInfo.setRequired(required);
            updateInfo.setMinSupportedVersion(minVersion);

            Map<String, String> changelog = new HashMap<>();
            changelog.put("ar", changelogAr);
            changelog.put("en", changelogEn);
            updateInfo.setChangelog(changelog);

            // نشر التحديث
            UpdateInfo published = updateService.publishNewUpdate(updateInfo);

            return ResponseEntity.ok(Map.of(
                    "message", "تم رفع التحديث بنجاح",
                    "update", published
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "فشل رفع التحديث: " + e.getMessage()));
        }
    }

    /**
     * حذف تحديث
     */
    @DeleteMapping("/updates/{id}")
    public ResponseEntity<?> deleteUpdate(@PathVariable Long id) {
        try {
            updateService.deleteUpdate(id);
            return ResponseEntity.ok(Map.of("message", "تم حذف التحديث"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * الحصول على قائمة جميع التحديثات
     */
    @GetMapping("/updates")
    public ResponseEntity<?> getAllUpdates() {
        return ResponseEntity.ok(updateService.getAllUpdates());
    }
}
