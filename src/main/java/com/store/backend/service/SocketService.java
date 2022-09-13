package com.store.backend.service;

import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.data.dto.WorkerStatus;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.queue.SynchronizedQueue;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Service
@AllArgsConstructor
@Lazy
public class SocketService {

    private final ShopService shopService;

    private final Map<Long, Queue<String>> shopIdToWorkersId = new HashMap<>();

    private final Map<String, WorkerStatus> workerIdToStatus = new HashMap<>();

    @PostConstruct
    private void buildShopsQueue() {
        shopService.shops().forEach(shop -> shopIdToWorkersId.put(shop.getId(), SynchronizedQueue.synchronizedQueue((new LinkedList<>()))));
    }

    public void setWorkerOnConnect(WorkerDetails workerDetails) {
        workerIdToStatus.put(workerDetails.getWorkerId(), new WorkerStatus(false));
        Queue<String> shopWorkersQueue = shopIdToWorkersId.get(workerDetails.getShopId());
        shopWorkersQueue.add(workerDetails.getWorkerId());
    }

    public void setWorkerBusy(String workerId) {
        WorkerStatus workerStatus = workerIdToStatus.get(workerId);
        workerStatus.setBusy(true);

    }

    public void setWorkerOnDisconnect(String workerId) {
        workerIdToStatus.remove(workerId);
    }

    public String getFirstAvailableWorker(Long shopId) {
        Queue<String> shopWorkersQueue = shopIdToWorkersId.get(shopId);
        while (shopWorkersQueue.peek() != null) {
            String workerId = shopWorkersQueue.poll();
            WorkerStatus workerStatus = workerIdToStatus.get(workerId);
            if (workerStatus == null || workerStatus.isBusy()) continue;
            return workerId;
        }
        return null;
    }
}
