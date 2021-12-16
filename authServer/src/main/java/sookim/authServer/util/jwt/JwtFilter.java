package sookim.authServer.util.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
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

public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String accessToken = null, refreshToken = null;
        final Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AccessToken"))
                    accessToken = cookie.getValue();
                else if (cookie.getName().equals("RefreshToken"))
                    refreshToken = cookie.getValue();
            }
        }
        System.out.println("accessToken = " + accessToken);
        try{
            if (accessToken != null && jwtProvider.validateToken(accessToken)){
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("인증 정보를 저장했습니다");
            } else{
                System.out.println("유효한 access token 없습니다");
                if (refreshToken != null && jwtProvider.validateToken(refreshToken)){
                    Authentication authentication = jwtProvider.getAuthentication(refreshToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String newAccessToken = jwtProvider.createAccessToken(SecurityContextHolder.getContext().getAuthentication());
                    Cookie newAccessTokenCookie = CookieUtil.createCookie("AccessToken", newAccessToken, ACCESS_TOKEN_VALIDATION_SEC);
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.addCookie(newAccessTokenCookie);
                }
            }
        }catch(Exception e){}
        chain.doFilter(request, response);
    }

}
