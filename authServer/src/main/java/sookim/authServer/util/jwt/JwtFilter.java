package sookim.authServer.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import sookim.authServer.service.CustomUserDetailsService;
import sookim.authServer.service.RedisService;
import sookim.authServer.util.CookieUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static sookim.authServer.util.jwt.JwtProvider.ACCESS_TOKEN_VALIDATION_SEC;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtProvider jwtProvider;
    private RedisService redisService;
    private CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JwtProvider jwtProvider, RedisService redisService, CustomUserDetailsService customUserDetailsService){
        this.jwtProvider = jwtProvider;
        this.redisService = redisService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = CookieUtil.getCookieValue(request, "AccessToken");
        String refreshToken = CookieUtil.getCookieValue(request, "RefreshToken");

        try{
            if (accessToken == null && refreshToken == null){
                System.out.println("No Token");
            }
            else if (accessToken != null && !jwtProvider.validateToken(accessToken)) {
                System.out.println("Invalid AccessToken");
            }
            else if (accessToken != null && jwtProvider.validateToken(accessToken) && SecurityContextHolder.getContext().getAuthentication() == null){
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{
                if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
                    System.out.println("No RefreshToken");
                }
                else if (jwtProvider.validateToken(refreshToken)){
                    Authentication authentication = jwtProvider.getAuthentication(refreshToken);
                    String redisToken = redisService.getData("RefreshToken:"+ authentication.getName());
                    if (!refreshToken.equals(redisToken)) {
                        System.out.println("refresh token이 redis의 토큰과 일치하지 않습니다.");
                    }
                    else {
                        System.out.println("refresh token이 올바릅니다");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String newAccessToken = jwtProvider.createAccessToken(SecurityContextHolder.getContext().getAuthentication());
                        Cookie newAccessTokenCookie = CookieUtil.createCookie("AccessToken", newAccessToken, ACCESS_TOKEN_VALIDATION_SEC);
                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse.addCookie(newAccessTokenCookie);
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("JWT filter error = " + e);
        };
        chain.doFilter(request, response);
    }
}
