package com.guCoding.carrotMarket.domain.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c where c.customer.id = :customerId and c.seller.id = :sellerId and c.stuff.id = :stuffId")
    Optional<ChatRoom> findUsersAndStuff(@Param("customerId") Long customerId, @Param("sellerId") Long sellerId, @Param("stuffId") Long stuffId);

    @Query("select c from ChatRoom c where c.seller.id = :userId or c.customer.id = :userId")
    List<ChatRoom> findMyChat(@Param("userId") Long userId);
}
