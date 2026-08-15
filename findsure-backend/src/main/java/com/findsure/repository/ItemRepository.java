package com.findsure.repository;

import com.findsure.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByQrToken(String qrToken);

    Optional<Item> findByQrTokenAndDeletedAtIsNull(String qrToken);

    Optional<Item> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);

    @Query("""
            SELECT i FROM Item i
            WHERE i.userId = :userId
              AND i.deletedAt IS NULL
              AND (:status IS NULL OR i.status = :status)
              AND (:search IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')))
            ORDER BY i.createdAt DESC
            """)
    Page<Item> search(
            @Param("userId") Long userId,
            @Param("status") Item.Status status,
            @Param("search") String search,
            Pageable pageable
    );

    long countByUserIdAndDeletedAtIsNull(Long userId);

    long countByUserIdAndStatusAndDeletedAtIsNull(Long userId, Item.Status status);
}
