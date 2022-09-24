package com.store.backend.controller;

import com.store.backend.data.model.worker.Worker;
import com.store.backend.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/worker")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @GetMapping("/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable String id) {
        return ResponseEntity.of(workerService.getWorker(id));
    }

    @PostMapping
    public ResponseEntity<Worker> upsertWorker(@RequestBody Worker worker) {
        return ResponseEntity.ok(workerService.createWorker(worker));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteWorker(@PathVariable String id) {
        workerService.deleteWorker(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(workerService.workers());
    }
}
