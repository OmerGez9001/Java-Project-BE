package com.store.backend.repository.redis;

import com.store.backend.data.model.login.LoginMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMetadataRepository extends JpaRepository<LoginMetadata,String> {
}
