package com.hope.sps;

import com.hope.sps.officer.Schedule;
import com.hope.sps.officer.ScheduleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.DayOfWeek;
import java.util.Set;

@SpringBootApplication
public class SpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpsApplication.class, args);
    }
    @Bean
    CommandLineRunner runner() {
        return args -> {

        };
    }

}
