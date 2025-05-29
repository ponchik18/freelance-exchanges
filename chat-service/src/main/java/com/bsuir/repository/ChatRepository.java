package com.bsuir.repository;

import com.bsuir.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE (c.firstUserId = :firstUserId AND c.secondUserId = :secondUserId) OR (c.firstUserId = :secondUserId AND c.secondUserId = :firstUserId)")
    Optional<Chat> findByUserIds(String firstUserId, String secondUserId);

}