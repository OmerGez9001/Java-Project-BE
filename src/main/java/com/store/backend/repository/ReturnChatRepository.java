package com.store.backend.repository;

import com.store.backend.data.model.chat.ReturnChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnChatRepository extends JpaRepository<ReturnChat, String> {
    Optional<ReturnChat> findByToShopId(Long shopId);
}
