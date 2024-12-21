package com.techbellys.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/public/health")
    public ResponseEntity<String> healthCheck(){return ResponseEntity.ok("Ok Health");}
}

