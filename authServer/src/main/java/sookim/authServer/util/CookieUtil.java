package sookim.authServer.util;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class CookieUtil {
    public static Cookie createCookie(String name, String val, long expire){
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(name, val);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)expire/1000);
        cookie.setPath("/");
        return cookie;
    }
}
