package com.store.backend.service;

import com.store.backend.data.model.report.RegisterAction;
import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.data.model.report.RegisterType;
import com.store.backend.repository.RegisterLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterLogService {
    private final RegisterLogRepository registerLogRepository;

    private void registerLog(String registerId, RegisterAction action, RegisterType type) {
        registerLogRepository.save(RegisterLog
                .builder()
                .registerId(registerId)
                .workerId(SecurityContextHolder.getContext().getAuthentication().getName())
                .registerType(type).registerAction(action).build());
    }

    public void registerCustomerLog(String registerId, RegisterAction action) {
        registerLog(registerId, action, RegisterType.CUSTOMER);
    }

    public void registerWorkerLog(String registerId, RegisterAction action) {
        registerLog(registerId, action, RegisterType.WORKER);
    }

    public List<RegisterLog> getAll()
    {
        return registerLogRepository.findAll();
    }
}
