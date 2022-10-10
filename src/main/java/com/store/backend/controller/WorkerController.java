package com.store.backend.controller;

import com.store.backend.assembler.WorkerDtoAssembler;
import com.store.backend.data.dto.WorkerDto;
import com.store.backend.data.mapper.WorkerMapper;
import com.store.backend.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/worker")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    private final WorkerDtoAssembler workerDtoAssembler;

    private final WorkerMapper mapper;


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<WorkerDto>> getWorker(@PathVariable String id) {
        return ResponseEntity
                .of(workerService.getWorker(id)
                .map(mapper::workerToWorkerDto)
                .map(workerDtoAssembler::toModel));
    }

    @PostMapping
    public ResponseEntity<EntityModel<WorkerDto>> createWorker(@RequestBody WorkerDto worker) {
        WorkerDto savedWorker = mapper.workerToWorkerDto(workerService.createWorker(mapper.workerDtoToWorker(worker)));
        return ResponseEntity.ok(workerDtoAssembler.toModel(savedWorker));
    }

    @PutMapping
    public ResponseEntity<EntityModel<WorkerDto>> updateWorker(@RequestBody WorkerDto worker) {
        WorkerDto savedWorker = mapper.workerToWorkerDto(workerService.updateWorker(mapper.workerDtoToWorker(worker)));
        return ResponseEntity.ok(workerDtoAssembler.toModel(savedWorker));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteWorker(@PathVariable String id) {
        workerService.deleteWorker(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public CollectionModel<EntityModel<WorkerDto>> getAllWorkers() {
        List<WorkerDto> collect = workerService.workers().stream().map(mapper::workerToWorkerDto).toList();
        return workerDtoAssembler.toCollectionModel(collect);
    }
}
