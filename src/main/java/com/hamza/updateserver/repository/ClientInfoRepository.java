package com.hamza.updateserver.repository;

import com.hamza.updateserver.model.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, Long> {
    Optional<ClientInfo> findByClientId(String clientId);

    long countByLastSeenAfter(LocalDateTime dateTime);

    @Query("SELECT c.currentVersion, COUNT(c) FROM ClientInfo c GROUP BY c.currentVersion")
    List<Object[]> countByVersion();

    @Query("SELECT c.osName, COUNT(c) FROM ClientInfo c GROUP BY c.osName")
    List<Object[]> countByOs();

    @Query("SELECT SUM(c.checkCount) FROM ClientInfo c")
    Long sumAllCheckCounts();
}
