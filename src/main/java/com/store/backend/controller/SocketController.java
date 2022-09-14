package com.store.backend.controller;

import com.store.backend.data.dto.Message;
import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;


@Controller
public class SocketController {
    @Autowired
    @Lazy
    private SocketService socketService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
    public Message sendGlobalMessage(@Payload Message chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/secured/room")
    public void sendSpecific(
            @Payload Message msg,
            Principal user,
            @Header("simpSessionId") String sessionId) throws Exception {

        simpMessagingTemplate.convertAndSendToUser(
                msg.getSender(), "queue/specific-user", msg);
    }


    private WorkerDetails workerDetailsByPrincipal(Principal principal) {
        return (WorkerDetails) ((UsernamePasswordAuthenticationToken) principal).getDetails();
    }
}
