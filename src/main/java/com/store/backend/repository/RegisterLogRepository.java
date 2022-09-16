package com.store.backend.repository;

import com.store.backend.data.model.report.RegisterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterLogRepository extends JpaRepository<RegisterLog, Long> {

}
