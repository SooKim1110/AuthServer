package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;

@Controller
//@RequiredArgsConstructor
//@RequestMapping("/user")
public class UserController {
//    private final UserRepository userRepository;
//
//    @PostMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void postUser(@RequestBody User user){
//        System.out.println("user = " + user);
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        user.setRole("ROLE_ADMIN");
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}

