package com.countrym.deliveryservice.common.util;

import com.countrym.deliveryservice.common.config.security.TokenStatus;
import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.domain.user.enums.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.countrym.deliveryservice.common.config.security.TokenConst.*;

@Slf4j
@Component
public class JwtUtils {
    private final SecretKey secretKey;

    public JwtUtils(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String createToken(long userId, String authority, long expire) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expire);

        return Jwts.builder()
                .claim(USER_ID, userId)
                .claim(USER_AUTHORITY, authority)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private Jws<Claims> getClaimsFromToken(String jwtToken) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);
    }

    public UserInfo getUserInfo(String jwtToken) {
        Jws<Claims> claims = getClaimsFromToken(jwtToken);

        Long userId = Long.parseLong(claims.getPayload().get(USER_ID).toString());
        Authority userAuthority = Authority.of(claims.getPayload().get(USER_AUTHORITY).toString());

        return UserInfo.of(userId, userAuthority);
    }

    public TokenStatus verifyToken(String jwtToken) {
        try {
            getClaimsFromToken(jwtToken);
            return TokenStatus.USUAL;
        } catch (Exception e) {
            log.error("JWT 토큰이 비정상입니다.");
            return TokenStatus.UNUSUAL;
        }
    }

    /**
     * Bearer 토큰에서 'Bearer '를 제외한 토큰 추출
     *
     * @param bearerToken
     * @return
     */
    public String resolveToken(String bearerToken) {
        if (!bearerToken.isBlank() && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.warn("Authorization 헤더가 없거나 BEARER 가 없습니다");
        return null;
    }
}
