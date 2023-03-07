package com.mauricio.inventory.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTGenerator {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                           .setSubject(username)
                           .claim("authorities", userDetails.getAuthorities())
                           .setIssuedAt(new Date())
                           .setExpiration(expireDate)
                           .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                           .compact();
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(SecurityConstants.JWT_SECRET)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException(ex.getMessage());
        }
    }

}
