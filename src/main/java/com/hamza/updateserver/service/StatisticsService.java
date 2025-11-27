package com.hamza.updateserver.service;

import com.hamza.updateserver.model.ClientInfo;
import com.hamza.updateserver.model.Statistics;
import com.hamza.updateserver.repository.ClientInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final ClientInfoRepository clientInfoRepository;

    @Transactional
    public void recordClientCheck(String clientId, String version,
                                  String osName, String osVersion,
                                  String javaVersion, String ipAddress) {
        ClientInfo client = clientInfoRepository.findByClientId(clientId)
                .orElseGet(() -> {
                    ClientInfo newClient = new ClientInfo();
                    newClient.setClientId(clientId);
                    newClient.setFirstSeen(LocalDateTime.now());
                    return newClient;
                });

        client.setCurrentVersion(version);
        client.setOsName(osName);
        client.setOsVersion(osVersion);
        client.setJavaVersion(javaVersion);
        client.setLastSeen(LocalDateTime.now());
        client.setIpAddress(ipAddress);
        client.setCheckCount(client.getCheckCount() + 1);

        clientInfoRepository.save(client);
    }

    public Statistics getStatistics() {
        Statistics stats = new Statistics();

        stats.setTotalClients(clientInfoRepository.count());
        stats.setActiveClientsToday(
                clientInfoRepository.countByLastSeenAfter(LocalDateTime.now().minusDays(1))
        );
        stats.setActiveClientsThisWeek(
                clientInfoRepository.countByLastSeenAfter(LocalDateTime.now().minusDays(7))
        );

        // توزيع الإصدارات
        Map<String, Long> versionDist = new HashMap<>();
        clientInfoRepository.countByVersion().forEach(row ->
                versionDist.put((String) row[0], (Long) row[1])
        );
        stats.setVersionDistribution(versionDist);

        // توزيع أنظمة التشغيل
        Map<String, Long> osDist = new HashMap<>();
        clientInfoRepository.countByOs().forEach(row ->
                osDist.put((String) row[0], (Long) row[1])
        );
        stats.setOsDistribution(osDist);

        Long totalChecks = clientInfoRepository.sumAllCheckCounts();
        stats.setTotalUpdateChecks(totalChecks != null ? totalChecks : 0L);

        return stats;
    }

    public String generateClientId() {
        return UUID.randomUUID().toString();
    }
}
