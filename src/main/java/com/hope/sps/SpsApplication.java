package com.hope.sps;

import com.hope.sps.admin.AdminService;
import com.hope.sps.common.RegisterRequest;
import com.hope.sps.customer.CustomerRegisterRequest;
import com.hope.sps.customer.CustomerService;
import com.hope.sps.officer.OfficerRegisterRequest;
import com.hope.sps.officer.OfficerService;
import com.hope.sps.zone.ZoneRegistrationRequest;
import com.hope.sps.zone.ZoneService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class SpsApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpsApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(AdminService adminService, ZoneService zoneService, OfficerService officerService, CustomerService customerService) {
        return args -> {
            createInitialAdmin(adminService, zoneService,officerService,customerService);
        };
    }

    private static void createInitialAdmin(AdminService adminService, ZoneService zoneService, OfficerService officerService, CustomerService customerService) {
        try {
            adminService.registerAdmin(new RegisterRequest("admin", "boss", "admin@admin.com", "123456789"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            zoneService.registerZone(new ZoneRegistrationRequest("to-101", "demo zone title", 1.0, "amman", 35.90991335449219, 31.951419360715086, 6, java.sql.Time.valueOf("00:00:00"), java.sql.Time.valueOf("23:59:59")));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            officerService.registerOfficer(new OfficerRegisterRequest("officer","officer","officer@officer.com","123456789",java.sql.Time.valueOf("08:00:00"),
                    java.sql.Time.valueOf("16:00:00"), List.of("SUNDAY","MONDAY"), Collections.emptyList(),"0776216787"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            customerService.registerCustomer(new CustomerRegisterRequest("customer","dear","customer@customer.com","123456789","0776216787"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
