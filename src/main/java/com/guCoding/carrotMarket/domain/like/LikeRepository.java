package com.guCoding.carrotMarket.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Modifying
    @Query(value = "INSERT INTO like_tb(stuff_Id, user_Id, create_Date, last_modified_date) VALUES(:stuffId, :userId, now(), now())", nativeQuery = true)
    void mLike(@Param("stuffId") Long stuffId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM like_tb WHERE stuff_Id = :stuffId AND user_Id = :userId", nativeQuery = true)
    void munLike(@Param("stuffId") Long stuffId, @Param("userId") Long userId);
}
