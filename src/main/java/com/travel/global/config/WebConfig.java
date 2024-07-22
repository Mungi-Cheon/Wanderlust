package com.travel.global.config;

import com.travel.global.interceptor.AuthorizationInterceptor;
import com.travel.global.resolver.TokenMemberIdResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final TokenMemberIdResolver tokenMemberIdResolver;

    private final List<String> PASS_URL = List.of(
        "/api/auth/login",
        "/api/auth/signup",
        "/api/accommodations/**"
    );

    private final List<String> DEFAULT_EXCLUDE = List.of(
        "/",
        "favicon.ico",
        "/error"
    );

    private final List<String> SWAGGER = List.of(
        "/swagger-ui/index.html",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인증 인터셉터 등록
        registry.addInterceptor(authorizationInterceptor)
            .excludePathPatterns(PASS_URL)
            .excludePathPatterns(DEFAULT_EXCLUDE)
            .excludePathPatterns(SWAGGER);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenMemberIdResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "https://wanderlust-mini4.netlify.app")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowCredentials(true)
            .maxAge(1728000L)
            .allowedHeaders("*")
            .exposedHeaders("*");
    }
}
