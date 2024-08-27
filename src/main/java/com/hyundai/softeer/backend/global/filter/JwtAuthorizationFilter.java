package com.hyundai.softeer.backend.global.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.domain.user.dto.AuthenticateUser;
import com.hyundai.softeer.backend.global.jwt.provider.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {
    private final String[] whiteListUris = new String[]{
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/v1/oauth2/**",
            "/api/v1/url/**",
            "/api/v1/expectation/land",
            "/api/v1/expectation/page",
            "/api/v1/firstcome/quiz/land",
            "/api/v1/lottery/drawing/land",
            "/api/v1/lottery/drawing/rank",
            "/api/v1/lottery/drawing/preview",
            "/api/v1/land",
            "/preview/**"
    };

    private final TokenProvider jwtProvider;

    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (
                whiteListCheck(httpServletRequest.getRequestURI()) ||
                        isPreflight(httpServletRequest)
        ) {
            chain.doFilter(request, response);
            return;
        }
        if (!isContainToken(httpServletRequest)) {
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "인증 오류");
            return;
        }
        try {
            String token = getToken(httpServletRequest);
            AuthenticateUser authenticateUser = getAuthenticateUser(token);
            chain.doFilter(request, response);
        } catch (JsonParseException e) {
            log.error("JsonParseException");
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Json Parse 에러가 발생했습니다");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            log.error("JwtException");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "인증 오류");
        } catch (ExpiredJwtException e) {
            log.error("JwtTokenExpired");
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "토큰이 만료 되었습니다");
        }
    }

    private static boolean isPreflight(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getMethod().equals("OPTIONS");
    }

    private boolean whiteListCheck(String uri) {
        return PatternMatchUtils.simpleMatch(whiteListUris, uri);
    }

    private boolean isContainToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Bearer ");
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization.substring(7);
    }

    private AuthenticateUser getAuthenticateUser(String token) throws JsonProcessingException {
        Claims claims = jwtProvider.getClaims(token);
        String authenticateUserJson = claims.get("email", String.class);
        return new AuthenticateUser(authenticateUserJson);
    }
}
