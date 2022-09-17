package com.store.backend.controller;

import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping
    public List<String> getAllExistingChats() {
        return socketService.getAllExistingChats();
    }

    private WorkerDetails workerDetailsByPrincipal(Principal principal) {
        return (WorkerDetails) ((UsernamePasswordAuthenticationToken) principal).getDetails();
    }
}
