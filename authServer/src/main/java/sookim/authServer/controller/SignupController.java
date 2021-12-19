package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;
import sookim.authServer.service.UserService;

import javax.management.InvalidAttributeValueException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    @GetMapping("")
    public String getSignup(){
        return "signup";
    }

    @PostMapping("")
    public void postSignup(User user){
        userService.saveUser(user);
    }
}