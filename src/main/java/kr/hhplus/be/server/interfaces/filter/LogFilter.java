package kr.hhplus.be.server.interfaces.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        boolean exceptionOccurred = false;
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            if (!exceptionOccurred) {
                logRequest(requestWrapper);
            }
            logResponse(responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }
    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        try {
            log.info("Request URL: {}", requestWrapper.getRequestURL());
            String parameters = getParametersAsString(requestWrapper);
            log.info("Request Parameters: {}", parameters);
            String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("Request Body: {}", requestBody);
        } catch (Exception e) {
            log.warn("Failed to log request details", e);
        }
    }
    private String getParametersAsString(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder params = new StringBuilder();
        try {
            Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
            parameterMap.forEach((key, values) -> {
                for (String value : values) {
                    if (params.length() > 0) {
                        params.append("&");
                    }
                    params.append(key).append("=").append(value);
                }
            });
        } catch (Exception e) {
            log.warn("Failed to retrieve request parameters", e);
        }
        return params.toString();
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) {
        try {
            log.info("Response Status: {}", responseWrapper.getStatus());
            String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("Response Body: {}", responseBody);
        } catch (Exception e) {
            log.warn("Failed to log response details", e);
        }
    }
}
