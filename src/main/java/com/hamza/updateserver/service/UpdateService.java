package com.hamza.updateserver.service;

import com.hamza.updateserver.model.UpdateInfo;
import com.hamza.updateserver.repository.UpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateService {
    private final UpdateRepository updateRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public UpdateInfo getLatestUpdate() {
        return updateRepository.findFirstByActiveTrueOrderByReleaseDateDesc()
                .orElse(null);
    }

    public boolean isUpdateAvailable(String currentVersion) {
        UpdateInfo latest = getLatestUpdate();
        if (latest == null) return false;
        return compareVersions(currentVersion, latest.getVersion()) < 0;
    }

    @Transactional
    public UpdateInfo publishNewUpdate(UpdateInfo updateInfo) {
        updateInfo.setActive(true);
        UpdateInfo saved = updateRepository.save(updateInfo);

        // إرسال إشعار لجميع العملاء المتصلين عبر WebSocket
        messagingTemplate.convertAndSend("/topic/updates", saved);

        return saved;
    }

    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }

    public void deleteUpdate(Long id) {
        updateRepository.deleteById(id);
    }

    public Object getAllUpdates() {
        return updateRepository.findAll();
    }
}
