package min.taskflow.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void addHttpOnlyCookie(HttpServletResponse response, String value) {

        ResponseCookie cookie = ResponseCookie.from(JwtConsts.REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(JwtConsts.REFRESH_TOKEN_EXPIRATION / 1000)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(JwtConsts.REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String getCookieValue(HttpServletRequest request) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(JwtConsts.REFRESH_TOKEN)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
