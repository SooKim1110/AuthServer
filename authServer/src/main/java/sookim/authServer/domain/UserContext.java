package sookim.authServer.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Getter
public class UserContext extends org.springframework.security.core.userdetails.User {
    private final User user;

    public UserContext(User user) {
        super(user.getUsername(), user.getPassword(), getAuthorities(user.getRole()));
        this.user = user;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(String role) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role));
        return authorityList;
    }
}