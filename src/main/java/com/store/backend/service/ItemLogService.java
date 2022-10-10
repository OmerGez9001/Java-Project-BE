package com.store.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.backend.data.dto.ItemsTransactionAggregation;
import com.store.backend.data.model.report.TransactionLog;
import com.store.backend.repository.elasticsearch.TransactionLogRepository;
import com.store.backend.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class ItemLogService {

    private final TransactionLogRepository transactionLogRepository;

    private final Utils utils;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void logItems(List<TransactionLog> transactionLogs) {
        for (TransactionLog transactionLog : transactionLogs) {
            transactionLog.setCreationTime(new Date());
            transactionLog.setPerformedBy(utils.getCurrentUser());
        }
        transactionLogRepository.saveAll(transactionLogs);
    }

    public List<ItemsTransactionAggregation> getTransactedItems(Optional<Long> quantity) {


        TermsAggregationBuilder itemsAggregation = new TermsAggregationBuilder("items").size(20).field("itemName.keyword");
        TermsAggregationBuilder transactionAggregation = new TermsAggregationBuilder("transactionAction").size(2).field("transactionAction");
        FilterAggregationBuilder quantityAggregation = new FilterAggregationBuilder("quantity", QueryBuilders.rangeQuery("quantity").gte(quantity.orElse(3L)));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withAggregations(itemsAggregation.subAggregation(transactionAggregation.subAggregation(quantityAggregation)))
                .build();

        SearchHits<TransactionLog> result = elasticsearchRestTemplate.search(searchQuery, TransactionLog.class);

        return getAggregationFromResult(result, "items")
                .getBuckets()
                .stream()
                .map(x -> parseAggregationCountAsObject("transactionAction", x.getAggregations(), x.getKeyAsString(), ItemsTransactionAggregation.class)).toList();
    }

    public CompletableFuture<List<TransactionLog>> getWorkerTransactedItems(String workerId, Optional<Integer> page, Optional<Integer> size) {
        return transactionLogRepository.findAllByPerformedByOrderByCreationTime(workerId, PageRequest.of(page.orElse(0), size.orElse(10)));
    }

    public CompletableFuture<List<TransactionLog>> getCustomerTransactedItems(String workerId, Optional<Integer> page, Optional<Integer> size) {
        return transactionLogRepository.findAllByPerformedOnOrderByCreationTime(workerId, PageRequest.of(page.orElse(0), size.orElse(10)));

    }

    private ParsedStringTerms getAggregationFromResult(SearchHits<?> searchHits, String name) {
        return ((Aggregations) searchHits.getAggregations().aggregations()).get(name);
    }

    private <T> T parseAggregationCountAsObject(String name, Aggregations aggregations, String keyName, Class<T> castedObject) {
        Map<String, Object> bucketValues = new HashMap<>();
        bucketValues.put("name", keyName);
        ((ParsedStringTerms) aggregations.get(name)).getBuckets().forEach(x -> bucketValues.put(x.getKeyAsString().toLowerCase(),((ParsedFilter) x.getAggregations().get("quantity")).getDocCount()));
        return new ObjectMapper().convertValue(bucketValues, castedObject);
    }

}
