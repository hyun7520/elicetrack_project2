package io.elice.shoppingmall.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Service
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Key key;

    public JwtAuthenticationProvider(Key key) {
        this.key = key;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
        String token = jwtAuthentication.getToken();

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        String username = claims.getBody().getSubject();
        boolean isAdmin = claims.getBody().get("isAdmin", Boolean.class);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (isAdmin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    //...
}
