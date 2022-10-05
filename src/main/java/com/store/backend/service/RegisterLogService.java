package com.store.backend.service;

import com.store.backend.data.model.report.RegisterAction;
import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.data.model.report.RegisterType;
import com.store.backend.repository.sql.RegisterLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterLogService {
    private final RegisterLogRepository registerLogRepository;

    private void registerLog(String registerId, RegisterAction action, RegisterType type) {
        registerLogRepository.save(RegisterLog
                .builder()
                .registerId(registerId)
                .workerId(Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Principal::getName).orElse("admin"))
                .registerType(type).registerAction(action).build());
    }

    public void registerCustomerLog(String registerId, RegisterAction action) {
        registerLog(registerId, action, RegisterType.CUSTOMER);
    }

    public void registerWorkerLog(String registerId, RegisterAction action) {
        registerLog(registerId, action, RegisterType.WORKER);
    }

    public List<RegisterLog> getAllByRegisterType(RegisterType registerType) {
        return registerLogRepository.findAll();
    }
}
