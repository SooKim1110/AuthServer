package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;
import sookim.authServer.service.UserService;
import sookim.authServer.service.VerifyEmailService;

import javax.management.InvalidAttributeValueException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;
    private final VerifyEmailService verifyEmailService;

    @GetMapping("")
    public String getSignup() {
        return "signup";
    }

    @PostMapping("")
    public String postSignup(User user) {
        try {
            userService.saveUser(user);
            verifyEmailService.sendVerifyMail(user);
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
        }
        return "redirect:/login";
    }

    @GetMapping("/verify/{key}")
    public ResponseEntity getVerify(@PathVariable String key) {
        ResponseEntity response;
        try {
            verifyEmailService.verifyUserEmail(key);
            response = new ResponseEntity("이메일 인증이 성공했습니다.", HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("e = " + e);
            response = new ResponseEntity("이메일 인증이 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}