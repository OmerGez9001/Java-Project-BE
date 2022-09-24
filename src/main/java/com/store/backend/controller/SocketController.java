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
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;


@Controller
public class SocketController {
    @Autowired
    @Lazy
    private SocketService socketService;


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
    @EventListener
    public void onUserSubscribe(SessionSubscribeEvent sessionSubscribeEvent)
    {
        WorkerDetails workerDetails = workerDetailsByPrincipal(sessionSubscribeEvent.getUser());
        String userDestination = ((String) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination"));
        if (userDestination != null && userDestination.startsWith("/user")) {
            this.socketService.returnToChat(workerDetails);
        }
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Message sendGlobalMessage(@Payload Message chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/secured/room")
    public void sendSpecific(
            @Payload Message msg,
            Principal user) {

        socketService.sendToUserByShopId(workerDetailsByPrincipal(user), msg);
    }


    private WorkerDetails workerDetailsByPrincipal(Principal principal) {
        return (WorkerDetails) ((UsernamePasswordAuthenticationToken) principal).getDetails();
    }
}
