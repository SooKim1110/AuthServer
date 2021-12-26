package sookim.authServer.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
import org.springframework.web.bind.annotation.*;
import sookim.authServer.domain.User;
import sookim.authServer.dto.Tokens;
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

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Tokens> login(@RequestBody User user, HttpServletResponse response){
        Tokens tokens = loginService.loginUser(user, response);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logoutuser")
    @ResponseBody
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.logoutUser(request, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    @ResponseBody
    public ResponseEntity authenticate(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/authenticate/user")
    @ResponseBody
    public ResponseEntity authenticateUser(){
        return new ResponseEntity(HttpStatus.OK);
    }



}
