package com.hamza.updateserver.controller;

import com.hamza.updateserver.model.Statistics;
import com.hamza.updateserver.model.UpdateInfo;
import com.hamza.updateserver.service.StatisticsService;
import com.hamza.updateserver.service.UpdateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UpdateController {

    private final UpdateService updateService;
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to the update server!");
    }

    @GetMapping("/version")
    public ResponseEntity<UpdateInfo> checkForUpdates(
            @RequestParam String clientId,
            @RequestParam String currentVersion,
            @RequestParam(required = false) String osName,
            @RequestParam(required = false) String osVersion,
            @RequestParam(required = false) String javaVersion,
            HttpServletRequest request) {

        // تسجيل معلومات العميل
        String ipAddress = request.getRemoteAddr();
        statisticsService.recordClientCheck(
                clientId, currentVersion, osName, osVersion, javaVersion, ipAddress
        );

        // الحصول على آخر تحديث
        UpdateInfo latestUpdate = updateService.getLatestUpdate();

        return ResponseEntity.ok(latestUpdate);
    }

    @PostMapping("/updates")
    public ResponseEntity<UpdateInfo> publishUpdate(@RequestBody UpdateInfo updateInfo) {
        // TODO: إضافة authentication وauthorization
        UpdateInfo published = updateService.publishNewUpdate(updateInfo);
        return ResponseEntity.ok(published);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Statistics> getStatistics() {
        // TODO: إضافة authentication وauthorization
        Statistics stats = statisticsService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/client-id")
    public ResponseEntity<String> generateClientId() {
        String clientId = statisticsService.generateClientId();
        return ResponseEntity.ok(clientId);
    }
}
