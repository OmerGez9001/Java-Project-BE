package com.store.backend.data.mapper;

import com.store.backend.data.dto.WorkerDto;
import com.store.backend.data.model.worker.Worker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    WorkerDto workerToWorkerDto(Worker worker);

    Worker workerDtoToWorker(WorkerDto workerDto);

}
