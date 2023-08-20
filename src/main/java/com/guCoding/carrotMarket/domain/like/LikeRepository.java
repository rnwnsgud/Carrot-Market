package com.guCoding.carrotMarket.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Modifying
    @Query(value = "INSERT INTO like_tb(stuffId, userId, createDate) VALUES(:stuffId, :userId, now())", nativeQuery = true)
    void mLike(@Param("stuffId") Long stuffId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM like_tb WHERE stuffId = :stuffId AND userId = :userId", nativeQuery = true)
    void munLike(@Param("stuffId") Long stuffId, @Param("userId") Long userId);
}
