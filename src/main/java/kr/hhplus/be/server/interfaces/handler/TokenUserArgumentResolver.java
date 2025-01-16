package kr.hhplus.be.server.interfaces.handler;

import com.querydsl.core.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class TokenUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final WaitingTokenService waitingTokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasTokenUserIdAnnotation = parameter.hasParameterAnnotation(TokenUserId.class);
        boolean isLongType = parameter.getParameterType().equals(Long.class);
        return hasTokenUserIdAnnotation && isLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String token = StringUtils.nullToEmpty(request.getHeader("X-Waiting-Token"));
        return waitingTokenService.getUserId(token);
    }
}
