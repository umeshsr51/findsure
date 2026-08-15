package com.findsure.repository;

import com.findsure.entity.FinderContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinderContactRepository extends JpaRepository<FinderContact, Long> {
    List<FinderContact> findByScanItemIdOrderByCreatedAtDesc(Long itemId);
}
