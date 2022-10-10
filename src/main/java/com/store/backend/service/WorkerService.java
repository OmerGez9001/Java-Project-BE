package com.store.backend.service;

import com.store.backend.config.filter.UserWithClaims;
import com.store.backend.data.model.report.RegisterAction;
import com.store.backend.data.model.worker.Job;
import com.store.backend.data.model.worker.Worker;
import com.store.backend.repository.sql.ShopRepository;
import com.store.backend.repository.sql.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService implements UserDetailsService {
    private final WorkerRepository workerRepository;
    private final ShopRepository shopRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RegisterLogService registerLogService;


    public Worker createWorker(Worker worker) {
        if (workerRepository.existsById(worker.getWorkerId()))
            throw new WorkerException("Worker already exists :" + worker.getWorkerId());
        worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        worker.setShop(shopRepository.findById(worker.getShop().getId()).get());
        return this.workerRepository.save(worker);
    }

    public Worker updateWorker(Worker worker) {
        if (!workerRepository.existsById(worker.getWorkerId()))
            throw new WorkerException("Worker is not exists :" + worker.getWorkerId());
        worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        worker.setShop(shopRepository.findById(worker.getShop().getId()).get());
        return this.workerRepository.save(worker);
    }

    public Optional<Worker> getWorker(String workerId) {
        return this.workerRepository.findById(workerId);
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
