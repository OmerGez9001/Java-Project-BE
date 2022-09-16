package com.store.backend.service;

import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.repository.RegisterLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterLogService {
    private final RegisterLogRepository registerLogRepository;
    public void registerLog (RegisterLog registerLog) {
        registerLogRepository.save(registerLog);
    }
}
