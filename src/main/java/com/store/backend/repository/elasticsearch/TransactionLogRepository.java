package com.store.backend.repository.elasticsearch;

import com.store.backend.data.model.report.TransactionLog;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TransactionLogRepository extends ElasticsearchRepository<TransactionLog, String> {
    @Async
    CompletableFuture<List<TransactionLog>> findAllByPerformedByOrderByCreationTime(String performedBy, PageRequest pageRequest);

    @Async
    CompletableFuture<List<TransactionLog>> findAllByPerformedOnOrderByCreationTime(String performedOn, PageRequest pageRequest);

}
