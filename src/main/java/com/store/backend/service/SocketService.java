package com.store.backend.service;

import com.store.backend.data.dto.Message;
import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.data.dto.WorkerStatus;
import com.store.backend.data.model.chat.ReturnChat;
import com.store.backend.repository.ReturnChatRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.queue.SynchronizedQueue;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Lazy
public class SocketService {

    private final ShopService shopService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Map<Long, Queue<String>> shopIdToWorkersId = new HashMap<>();

    private final Map<String, WorkerStatus> workerIdToStatus = new HashMap<>();

    private final ReturnChatRepository returnChatRepository;


    @PostConstruct
    private void buildShopsQueue() {
        shopService.shops().forEach(shop -> shopIdToWorkersId.put(shop.getId(), SynchronizedQueue.synchronizedQueue((new LinkedList<>()))));
    }

    @SneakyThrows
    public void setWorkerOnConnect(WorkerDetails workerDetails) {
        if (!workerIdToStatus.containsKey(workerDetails.getWorkerId())) {
            workerIdToStatus.put(workerDetails.getWorkerId(), new WorkerStatus(new ArrayList<>(), true));
            Queue<String> shopWorkersQueue = shopIdToWorkersId.get(workerDetails.getShopId());
            shopWorkersQueue.add(workerDetails.getWorkerId());
        } else
            this.workerIdToStatus.get(workerDetails.getWorkerId()).setConnected(true);

        sendConnectMessage(workerDetails.getWorkerId());
    }

    @SneakyThrows
    public void sendDisconnectMessage(String workerId) {
        List<String> chatWith = this.workerIdToStatus.get(workerId).getChatWith();
        for (String worker : chatWith) {
            simpMessagingTemplate.convertAndSendToUser(worker, "queue/specific-user", new Message(workerId + " has been disconnected from the chat", "SERVER", null));
        }
    }

    @SneakyThrows
    public void sendConnectMessage(String workerId) {
        List<String> chatWith = this.workerIdToStatus.get(workerId).getChatWith();
        for (String worker : chatWith) {
            simpMessagingTemplate.convertAndSendToUser(worker, "queue/specific-user", new Message(workerId + " has been connected to the chat", "SERVER", null));
        }
    }

    public void returnToChat(WorkerDetails workerDetails) {
        List<ReturnChat> chats = returnChatRepository.findAllByToShopIdOrderByCreated(workerDetails.getShopId());
        if (!chats.isEmpty()) {
            String firstWorkerToSendMessage = chats.get(0).getWorkerId();
            List<ReturnChat> chatsFromWorker = chats.stream().filter(x -> x.getWorkerId().equals(firstWorkerToSendMessage)).toList();
            chatsFromWorker.forEach(returnChat -> sendToUserByShopId(new WorkerDetails(returnChat.getWorkerId(), returnChat.getFromShopId()), new Message(returnChat.getContent(), workerDetails.getWorkerId(), returnChat.getToShopId())));
            this.returnChatRepository.deleteAll(chatsFromWorker);
        }
    }

    public List<String> getAllExistingChats() {
        return workerIdToStatus.values().stream().map(WorkerStatus::getChatWith).flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }

    public void addManager(String workerId, String managerId) {
        WorkerStatus managerStatus = workerIdToStatus.get(managerId);
        if (managerStatus.getChatWith().isEmpty()) {
            WorkerStatus workerStatus = workerIdToStatus.get(workerId);
            List<String> otherWorkerIds = workerStatus.getChatWith();
            for (String otherWorkerId : otherWorkerIds) {
                managerStatus.getChatWith().add(otherWorkerId);
                workerIdToStatus.get(otherWorkerId).getChatWith().add(managerId);
            }
            workerStatus.getChatWith().add(managerId);
            managerStatus.getChatWith().add(workerId);
        }
    }

    public void setWorkerOnDisconnect(String workerId) {
        sendDisconnectMessage(workerId);
        workerIdToStatus.get(workerId).setConnected(false);
    }

    public void sendToUserByShopId(WorkerDetails senderDetails, Message message) {
        WorkerStatus workerStatus = workerIdToStatus.get(senderDetails.getWorkerId());
        String sendTO;

        if (workerStatus.getChatWith().isEmpty()) {
            sendTO = getFirstAvailableWorker(message.getShopId());
            if (sendTO == null) {
                returnChatRepository.save(ReturnChat
                        .builder()
                        .workerId(senderDetails.getWorkerId())
                        .fromShopId(senderDetails.getShopId())
                        .toShopId(message.getShopId())
                        .content(message.getContent())
                        .build());
                return;
            }
            workerStatus.getChatWith().add(sendTO);
            workerIdToStatus.get(sendTO).getChatWith().add(senderDetails.getWorkerId());
            sendConnectMessage(sendTO);
            sendConnectMessage(senderDetails.getWorkerId());
        }
        message.setSender(senderDetails.getWorkerId());
        for (String sendTo : workerStatus.getChatWith()) {
            message.setSender(senderDetails.getWorkerId());
            simpMessagingTemplate.convertAndSendToUser(sendTo, "queue/specific-user", message);
        }
    }

    public String getFirstAvailableWorker(Long shopId) {
        Queue<String> shopWorkersQueue = shopIdToWorkersId.get(shopId);
        while (shopWorkersQueue.peek() != null) {
            String workerId = shopWorkersQueue.poll();
            WorkerStatus workerStatus = workerIdToStatus.get(workerId);
            if (workerStatus == null || !workerStatus.getChatWith().isEmpty() || !workerStatus.isConnected()) continue;
            return workerId;
        }
        return null;
    }
}
