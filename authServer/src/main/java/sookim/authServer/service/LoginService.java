package sookim.authServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sookim.authServer.domain.User;
import sookim.authServer.dto.Tokens;
import sookim.authServer.util.CookieUtil;
import sookim.authServer.util.jwt.JwtProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static sookim.authServer.util.jwt.JwtProvider.ACCESS_TOKEN_VALIDATION_SEC;
import static sookim.authServer.util.jwt.JwtProvider.REFRESH_TOKEN_VALIDATION_SEC;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;

    public Tokens loginUser (User user, HttpServletResponse response){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            String accessToken = jwtProvider.createAccessToken(authentication);
            String refreshToken = jwtProvider.createRefreshToken(authentication);

            redisService.setData("RefreshToken:" + authentication.getName(), refreshToken, REFRESH_TOKEN_VALIDATION_SEC);

            Cookie accessTokenCookie = CookieUtil.createCookie("AccessToken", accessToken, ACCESS_TOKEN_VALIDATION_SEC);
            Cookie refreshTokenCookie = CookieUtil.createCookie("RefreshToken", refreshToken, REFRESH_TOKEN_VALIDATION_SEC);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            return Tokens.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    }

    public void logoutUser (HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.getCookieValue(request, "AccessToken");
        System.out.println("accessToken = " + accessToken);
        if (accessToken != null && jwtProvider.validateToken(accessToken)){
            Authentication authentication = jwtProvider.getAuthentication(accessToken);

            if (authentication != null && authentication.isAuthenticated()) {
                Cookie accessCookie = CookieUtil.deleteCookie("AccessToken");
                Cookie refreshCookie = CookieUtil.deleteCookie("RefreshToken");
                response.addCookie(accessCookie);
                response.addCookie(refreshCookie);

                SecurityContextHolder.clearContext();
            }

            if (redisService.getData("RefreshToken:" + authentication.getName()) != null) {
                redisService.deleteData("RefreshToken:" + authentication.getName());
            }
        }
    }
}
