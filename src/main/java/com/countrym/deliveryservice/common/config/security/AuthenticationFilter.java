package com.countrym.deliveryservice.common.config.security;

import com.countrym.deliveryservice.common.exception.ForbiddenException;
import com.countrym.deliveryservice.common.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.countrym.deliveryservice.common.exception.ResponseCode.FORBIDDEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String url = request.getRequestURI();
        String method = request.getMethod();

        if (!url.isBlank() && !isExcludePath(url, method)) {
            // 그 외에는 토큰 체크
            String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (!bearerToken.isBlank()) {
                // 토큰 검증
                String accessToken = jwtUtils.resolveToken(bearerToken);

                TokenStatus tokenStatus = jwtUtils.verifyToken(accessToken);
                switch (tokenStatus) {
                    case USUAL -> {
                        UserInfo userInfo = jwtUtils.getUserInfo(accessToken);

                        if (userInfo.getId() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            AuthenticationToken authenticationToken = new AuthenticationToken(userInfo);
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        } else {
                            log.error("JWT 파싱 실패");
                        }
                    }
                    case UNUSUAL -> throw new ForbiddenException(FORBIDDEN);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcludePath(String path, String method) {
        return path.startsWith("/swagger-ui/") ||
                path.equals("/api/auth/sign-up") ||
                path.equals("/api/auth/sign-in") ||
                (path.startsWith("/api/stores") && method.equals(HttpMethod.GET.toString())) ||
                path.matches("^/api-docs(/.*)?$");
    }
}
