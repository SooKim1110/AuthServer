package sookim.authServer.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider implements InitializingBean {
    private static final long serialVersionUID = 1234567890123456L;

    public final static long ACCESS_TOKEN_VALIDATION_SEC = 1000;
    //    public final static long ACCESS_TOKEN_VALIDATION_SEC = 1000 * 60L * 60L * 2L; //2시간
    public static final long REFRESH_TOKEN_VALIDATION_SEC = 1000 * 60 * 60 * 24 * 14; // 2주

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication, long validation_sec){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date expire = new Date((new Date()).getTime() + validation_sec);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expire)
                .compact();
    }

    public String createAccessToken(Authentication authentication){
        return createToken(authentication, ACCESS_TOKEN_VALIDATION_SEC);
    }

    public String createRefreshToken(Authentication authentication){
        return createToken(authentication, REFRESH_TOKEN_VALIDATION_SEC);
    }

    public Claims extractClaims(String token) throws ExpiredJwtException {
        Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        return claims;
    }

    public Authentication getAuthentication(String token){
        Claims claims = extractClaims(token);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token){
        try{
            Claims claims = extractClaims(token);
            System.out.println("claims.getExpiration() = " + claims.getExpiration());
            return claims.getExpiration().after(new Date());
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            //에러 처리
            System.out.println("e = " + e);
        }
        return false;
    }

}
