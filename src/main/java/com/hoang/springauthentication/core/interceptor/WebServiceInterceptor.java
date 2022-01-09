package com.hoang.springauthentication.core.interceptor;

import com.hoang.springauthentication.jwtprovider.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class WebServiceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("{}", response.getStatus());
        JwtProvider.getRoleFromCurrentToken();
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }

    private String getRemoteIp(HttpServletRequest context) {
        String ipAddress = context.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = context.getHeader("x-forwarded-for");
        }

        if (ipAddress == null) {
            ipAddress = context.getRemoteAddr();
        }

        return ipAddress;
    }
}
