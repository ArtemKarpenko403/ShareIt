package ru.practicum.shareit.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (handler instanceof HandlerMethod) {
            log.info("Incoming request: {} {} from {}", 
                request.getMethod(), 
                request.getRequestURI(),
                request.getRemoteAddr());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, 
                               Exception ex) {
        if (handler instanceof HandlerMethod) {
            log.info("Completed request: {} {} - Status {}", 
                request.getMethod(), 
                request.getRequestURI(),
                response.getStatus());
        }
    }
}