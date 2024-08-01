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
            throw new IllegalArgumentException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException("JWT 파싱 중 오류가 발생했습니다.");
        }
    }

    public User getUsername(Claims claims) {
        return null;
    }
}

