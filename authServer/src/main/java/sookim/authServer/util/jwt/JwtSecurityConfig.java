package sookim.authServer.util.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sookim.authServer.service.CustomUserDetailsService;
import sookim.authServer.service.RedisService;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private JwtProvider jwtProvider;
    private RedisService redisService;
    private CustomUserDetailsService customUserDetailsService;

    public JwtSecurityConfig(JwtProvider jwtProvider, RedisService redisService, CustomUserDetailsService customUserDetailsService){
        this.jwtProvider = jwtProvider;
        this.redisService = redisService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(jwtProvider, redisService, customUserDetailsService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
