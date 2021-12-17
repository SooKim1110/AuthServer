package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sookim.authServer.repository.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    @GetMapping("/user")
    public String getTestUser(){
        return "test/user";
    }

    @GetMapping("/admin")
    public String getTestAdmin(){
        return "test/admin";
    }
}


