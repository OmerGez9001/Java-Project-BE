package com.store.backend.controller;

import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/chat")
public class SocketApiController {
    @Autowired
    @Lazy
    private SocketService socketService;


    @PostMapping("/{chatId}")
    public void addToChatId(@PathVariable String chatId, Principal user) {
        socketService.addManager(chatId, workerDetailsByPrincipal(user).getWorkerId());

    }

    private WorkerDetails workerDetailsByPrincipal(Principal principal) {
        return (WorkerDetails) ((UsernamePasswordAuthenticationToken) principal).getDetails();
    }
}
