package com.store.backend.repository;

import com.store.backend.data.model.chat.ReturnChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnChatRepository extends JpaRepository<ReturnChat, String> {
    List<ReturnChat> findAllByToShopIdOrderByCreated(Long shopId);
}
