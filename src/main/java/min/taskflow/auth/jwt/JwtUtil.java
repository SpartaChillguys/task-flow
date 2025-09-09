package min.taskflow.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import min.taskflow.auth.dto.info.UserInfo;
import min.taskflow.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/*
토큰을 생성하고 검증하는 유틸 클래스
 */
@Component
public class JwtUtil {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        // HS256 → 최소 32바이트 이상의 key 필요
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 액세스 토큰 생성
    public String createAccessToken(Long userId, UserRole userRole) {

        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userRole", userRole.name())
                .setExpiration(new Date(date.getTime() + JwtConsts.ACCESS_TOKEN_EXPIRATION))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(Long userId, UserRole userRole) {

        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userRole", userRole.name())
                .setExpiration(new Date(date.getTime() + JwtConsts.REFRESH_TOKEN_EXPIRATION))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // JWT 검증
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 리프레시 토큰에 담긴 유저정보 반환
    public UserInfo parseUserInfo(String token) {

        Claims claims = validateToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        String userRoleString = claims.get("userRole", String.class);
        UserRole userRole = UserRole.valueOf(userRoleString);

        return new UserInfo(userId, userRole);
    }
}
