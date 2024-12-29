package com.techbellys.controller;

import com.techbellys.chat.service.ChatService;
import com.techbellys.chat.service.dto.QueryRequest;
import com.techbellys.chat.service.dto.QueryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.techbellys.constants.ApiEndpoints;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.CHAT)
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(ApiEndpoints.RAG)
    public ResponseEntity<Object> processQuery(@Valid @RequestBody QueryRequest query) {
        QueryResponse response = chatService.processQuery(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiEndpoints.AGENT)
    public ResponseEntity<Object> processQueryByAgent(@Valid @RequestBody QueryRequest query) {
        QueryResponse response = chatService.processQueryByAgent(query);
        return ResponseEntity.ok(response);
    }


}
