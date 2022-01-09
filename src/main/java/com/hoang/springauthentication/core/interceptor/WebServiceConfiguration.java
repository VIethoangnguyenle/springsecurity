package com.hoang.springauthentication.core.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebServiceConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebServiceInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins( "http://localhost:[*]")
                .allowCredentials(true)
                .allowedMethods("POST", "GET", "HEAD", "OPTIONS", "DELETE", "PUT")
                .allowedHeaders("Origin, Authorization, Cache-Control, Pragma, Content-Type, Accept, Accept-Encoding, X-Requested-With, remember-me");
    }
}
