package com.hope.sps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpsApplication.class, args);
    }
    /*@Bean
    CommandLineRunner runner(OfficerRepository customerRepository) {
        return args -> {
            customerRepository.findAll().forEach(System.out::println);
        };
    }
*/
}
