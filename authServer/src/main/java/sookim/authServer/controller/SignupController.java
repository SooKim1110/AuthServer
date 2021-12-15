package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserRepository userRepository;

    @PostMapping("")
    public void postSignup(@RequestBody User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}