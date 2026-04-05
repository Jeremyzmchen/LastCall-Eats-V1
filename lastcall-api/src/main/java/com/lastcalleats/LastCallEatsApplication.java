package com.lastcalleats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LastCallEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastCallEatsApplication.class, args);
    }
}
