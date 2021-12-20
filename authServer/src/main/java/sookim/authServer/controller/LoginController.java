package sookim.authServer.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;
import sookim.authServer.service.LoginService;
import sookim.authServer.service.RedisService;
import sookim.authServer.util.CookieUtil;
import sookim.authServer.util.ErrorCode;
import sookim.authServer.util.ErrorResponse;
import sookim.authServer.util.jwt.JwtProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

import static sookim.authServer.util.jwt.JwtProvider.ACCESS_TOKEN_VALIDATION_SEC;
import static sookim.authServer.util.jwt.JwtProvider.REFRESH_TOKEN_VALIDATION_SEC;

@Controller
//@RequestMapping("")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String authorize(User user, HttpServletResponse response, Model model){
        try {
            loginService.loginUser(user, response);
            return "redirect:/test/login";
        }
        catch (InternalAuthenticationServiceException | BadCredentialsException e){
            ResponseEntity responseEntity = new ResponseEntity(ErrorResponse.of(ErrorCode.LOGIN_CREDENTIAL_INVALID), HttpStatus.UNAUTHORIZED);
            model.addAttribute("responseEntity", responseEntity);
            return "login";
        }
    }

    @PostMapping("/logoutuser")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            loginService.logoutUser(request, response);
        }
        catch(Exception e){
            System.out.println("e = " + e);
            ResponseEntity responseEntity = new ResponseEntity(ErrorResponse.of(ErrorCode.LOGOUT_INVALID), HttpStatus.INTERNAL_SERVER_ERROR);
            model.addAttribute("responseEntity", responseEntity);
            return "error";
        }
        return "redirect:/";
    }
}
