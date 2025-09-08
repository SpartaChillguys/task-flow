package min.taskflow.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import min.taskflow.auth.dto.info.UserInfo;
import min.taskflow.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.equals("/api/auth/register") || uri.equals("/api/auth/login")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {

            try {
                UserInfo userInfo = jwtUtil.parseUserInfo(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userInfo.userId(),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + userInfo.userRole().name())) // 권한
                        );

                // SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // HttpServletRequest의 attribute에 저장
                request.setAttribute("userId", userInfo.userId());
                request.setAttribute("userRole", userInfo.userRole());

            } catch (Exception e) {
                log.error("JWT 토큰 검증 실패: ", e);
                //필터에서 터진 예외처리는 GlobalHandler에서 잡지 못하기때문에 만들어줌
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                ApiResponse<Void> errorResponse = ApiResponse.error(
                        "인증이 필요합니다",
                        HttpStatus.UNAUTHORIZED
                );

                response.getWriter().write(mapper.writeValueAsString(errorResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        return null;
    }
}