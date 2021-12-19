package sookim.authServer.util;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieUtil {
    public static String getCookieValue(HttpServletRequest request, String key) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key))
                return cookie.getValue();
        }
        return null;
    }

    public static Cookie createCookie(String name, String val, long expire){
        Cookie cookie = new javax.servlet.http.Cookie(name, val);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)expire/1000);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie deleteCookie(String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}
