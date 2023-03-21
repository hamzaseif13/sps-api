package com.hope.sps.admin;

import com.hope.sps.UserDetails.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class AdminController {
    @GetMapping("/api/admin/hello")
    public UserDetailsImpl helloWorld(Authentication auth){
        return (UserDetailsImpl)auth.getPrincipal();
    }
    @GetMapping("/api/customer/hello")
    public String helloWorldPesant(){
        return "hello world";
    }


}
