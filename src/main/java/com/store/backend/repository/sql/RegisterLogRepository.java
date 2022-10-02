package com.store.backend.repository.sql;

import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.data.model.report.RegisterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterLogRepository extends JpaRepository<RegisterLog, Long> {

    List<RegisterLog> findAllByRegisterType(RegisterType registerType);

}
