package com.hamza.updateserver.model;

import lombok.Data;
import java.util.Map;

@Data
public class Statistics {
    private long totalClients;
    private long activeClientsToday;
    private long activeClientsThisWeek;
    private Map<String, Long> versionDistribution;
    private Map<String, Long> osDistribution;
    private long totalUpdateChecks;
}
