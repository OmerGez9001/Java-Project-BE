package com.store.backend.data.mapper;

import com.store.backend.data.dto.TransactionLogDto;
import com.store.backend.data.model.report.TransactionLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionLogMapper {
    List<TransactionLogDto> transactionLogToTransactionLog(List<TransactionLog> transactionLog);
}
