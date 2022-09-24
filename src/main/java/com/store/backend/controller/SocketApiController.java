package com.store.backend.controller;

import com.store.backend.data.dto.WorkerDetails;
import com.store.backend.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> addToChatId(@PathVariable String chatId, Principal user) {
        socketService.addManager(chatId, workerDetailsByPrincipal(user).getWorkerId());
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<String>> getAllExistingChats() {
        return ResponseEntity.ok(socketService.getAllExistingChats());
    }

    private WorkerDetails workerDetailsByPrincipal(Principal principal) {
        return (WorkerDetails) ((UsernamePasswordAuthenticationToken) principal).getDetails();
    }
}
