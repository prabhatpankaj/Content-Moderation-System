package com.techbellys;

import io.mongock.runner.springboot.EnableMongock;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"com.techbellys","com.techbellys.utility"})
@EnableMongock
@EnableAsync
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Set the default timezone to IST
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));
    }
}