package com.store.backend.service;

import com.store.backend.config.filter.UserWithClaims;
import com.store.backend.data.model.report.RegisterAction;
import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.exception.UserAlreadyExists;
import com.store.backend.data.model.worker.Job;
import com.store.backend.data.model.worker.Worker;
import com.store.backend.repository.ShopRepository;
import com.store.backend.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService implements UserDetailsService {
    private final WorkerRepository workerRepository;
    private final ShopRepository shopRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RegisterLogService registerLogService;


    public Worker createWorker(Worker worker) {
        worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        worker.setShop(shopRepository.findById(worker.getShop().getId()).get());
        Worker persistentWorker = this.workerRepository.save(worker);
        RegisterAction registerAction = this.workerRepository.existsById(worker.getWorkerId()) ? RegisterAction.MODIFY : RegisterAction.CREATE;
        this.registerLogService.registerWorkerLog(persistentWorker.getWorkerId(), registerAction);
        return persistentWorker;
    }

    public Worker getWorker(String workerId) {
        return this.workerRepository.findById(workerId).get();
    }

    public List<Worker> workers() {
        return this.workerRepository.findAll();
    }

    public void deleteWorker(String workerId) {
        this.workerRepository.deleteById(workerId);
        this.registerLogService.registerWorkerLog(workerId, RegisterAction.DELETE);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            List<SimpleGrantedAuthority> adminRoles = Arrays.stream(Job.values()).map(x -> new SimpleGrantedAuthority(x.name())).collect(Collectors.toList());
            adminRoles.add(new SimpleGrantedAuthority("ADMIN"));
            return new UserWithClaims("admin", passwordEncoder.encode("admin"), adminRoles, null);
        }
        Worker worker = workerRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new UserWithClaims(worker.getWorkerId(), worker.getPassword(), Collections.singleton(new SimpleGrantedAuthority(worker.getJob().name())), worker.getShop().getId());
    }
}
