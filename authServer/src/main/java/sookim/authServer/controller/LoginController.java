package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;
import sookim.authServer.util.CookieUtil;
import sookim.authServer.util.jwt.JwtProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static sookim.authServer.util.jwt.JwtProvider.ACCESS_TOKEN_VALIDATION_SEC;
import static sookim.authServer.util.jwt.JwtProvider.REFRESH_TOKEN_VALIDATION_SEC;

@Controller
//@RequestMapping("")
@RequiredArgsConstructor
public class LoginController {
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String authorize(User user, HttpServletResponse res){
//        if (userRepository.findByUsername(user.getUsername()).orElse(null) == null) {
//            return "redirect:/";
//        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.createAccessToken(authentication);
        String refreshToken = jwtProvider.createRefreshToken(authentication);

        Cookie accessTokenCookie = CookieUtil.createCookie("AccessToken", accessToken, ACCESS_TOKEN_VALIDATION_SEC);
        Cookie refreshTokenCookie = CookieUtil.createCookie("RefreshToken", refreshToken, REFRESH_TOKEN_VALIDATION_SEC);


        res.addCookie(accessTokenCookie);
        res.addCookie(refreshTokenCookie);

        return "redirect:/test";
    }
}
