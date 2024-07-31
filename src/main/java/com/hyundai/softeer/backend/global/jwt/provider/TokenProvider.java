package com.hyundai.softeer.backend.global.jwt.provider;

import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class TokenProvider {

    private final Key key;

    public TokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createJwt(Map<String, Object> claims) {
        String accessToken = createToken(claims, getExpireDateAccessToken());
        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public Date getExpireDateAccessToken() {
        long expireTimeMils = 1000 * 60 * 60;
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }

    public String createToken(Map<String, Object> claims, Date expireDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (SecurityException | MalformedJwtException e) {
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            e.getClaims();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUsername(Claims claims) {
        return null;
    }
}

