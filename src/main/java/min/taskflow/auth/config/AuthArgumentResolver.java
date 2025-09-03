package min.taskflow.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.user.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

//Auth Parameter를 쓰기위한..
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean haveAuth = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isValidType = parameter.getParameterType().equals(AuthUser.class);

        if (haveAuth != isValidType) {
            throw new IllegalArgumentException("Auth 와 AuthUser 가 함께 사용되어야 합니다.");
        }
        return haveAuth;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        UserRole userRole = (UserRole) httpServletRequest.getAttribute("userRole");

        if (userId != null && userRole != null) {
            return new AuthUser(userId, userRole);
        }

        return null; // 또는 인증되지 않은 사용자에 대한 예외 처리
    }
}