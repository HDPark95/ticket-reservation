package kr.hhplus.be.server.intrastructure.config;

import kr.hhplus.be.server.interfaces.handler.TokenUserArgumentResolver;
import kr.hhplus.be.server.interfaces.interceptor.WaitingTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final WaitingTokenInterceptor waitingTokenInterceptor;

    private final TokenUserArgumentResolver tokenUserArgumentResolver;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(waitingTokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/token");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenUserArgumentResolver);
    }
}
