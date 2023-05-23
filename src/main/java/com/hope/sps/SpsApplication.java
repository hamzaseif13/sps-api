package com.hope.sps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpsApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(SpsApplication.class, args);
    }
}
