package com.hope.sps;


import com.hope.sps.admin.AdminService;
import com.hope.sps.common.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpsApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpsApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            AdminService adminService) {
        return args -> {
            createInitialAdmin(adminService);
        };
    }

    private static void createInitialAdmin(AdminService adminService){
        try{
            adminService.registerAdmin(new RegisterRequest("admin","boss","admin@admin.com","123456789"));
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
