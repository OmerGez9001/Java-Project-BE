package com.store.backend.controller;

import com.store.backend.data.model.worker.Worker;
import com.store.backend.exception.UserAlreadyExists;
import com.store.backend.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/worker")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @GetMapping("/{id}")
    public Worker getWorker(@PathVariable String id) {
        return workerService.getWorker(id);
    }

    @PostMapping
    public Worker createWorker(@RequestBody Worker worker) throws  UserAlreadyExists {
        return workerService.createWorker(worker);
    }

    @PutMapping
    public Worker updateWorker(@RequestBody Worker worker){
        return workerService.updateWorker(worker);
    }


    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerService.workers();
    }
}
