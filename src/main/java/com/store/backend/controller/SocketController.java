package com.store.backend.controller;

import com.store.backend.data.dto.Message;
import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.service.SocketService;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;


@Controller
@AllArgsConstructor
public class SocketController {

    @Autowired
    private final SocketService socketService;

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent disconnectEvent) {
        WorkerDetails workerDetails = workerDetailsByPrincipal(disconnectEvent.getUser());
        socketService.setWorkerOnDisconnect(workerDetails.getWorkerId());
    }

    @EventListener
    public void onConnectEvent(SessionConnectEvent connectEvent) {
        WorkerDetails workerDetails = workerDetailsByPrincipal(connectEvent.getUser());
        socketService.setWorkerOnConnect(workerDetails);
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        return chatMessage;
    }

    private WorkerDetails workerDetailsByPrincipal(Principal principal) {
        return (WorkerDetails) ((UsernamePasswordAuthenticationToken) principal).getDetails();
    }
}
