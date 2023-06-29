package com.fantasy.policy_search_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PolicySearchServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolicySearchServerApplication.class, args);
    }

}