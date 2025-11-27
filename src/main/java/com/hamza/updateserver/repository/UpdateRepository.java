package com.hamza.updateserver.repository;

import com.hamza.updateserver.model.UpdateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UpdateRepository extends JpaRepository<UpdateInfo, Long> {
    Optional<UpdateInfo> findByVersionAndActiveTrue(String version);
    Optional<UpdateInfo> findFirstByActiveTrueOrderByReleaseDateDesc();
}
