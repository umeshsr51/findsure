package com.findsure.repository;

import com.findsure.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ScanRepository extends JpaRepository<Scan, Long> {
    List<Scan> findByItemIdOrderByScannedAtDesc(Long itemId);
    Optional<Scan> findFirstByItemIdOrderByScannedAtDesc(Long itemId);
    long countByItemId(Long itemId);
}
