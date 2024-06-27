package com.travel.global.security.config;

import com.travel.global.security.filter.JwtAuthenticationFilter;
import com.travel.global.security.handler.JwtAuthenticationFailureHandler;
import com.travel.global.security.handler.JwtAuthenticationSuccessHandler;
import com.travel.global.security.handler.JwtLogoutHandler;
import com.travel.global.security.handler.JwtLogoutSuccessHandler;
import com.travel.global.security.provider.JwtProvider;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationSuccessHandler successHandler;

    private final JwtAuthenticationFailureHandler failureHandler;

    private final JwtProvider jwtProvider;

    private final Map<String, String> excludeUrls = Map.of(
        "/auth/signup", HttpMethod.POST.name(),
        "/auth/login", HttpMethod.POST.name(),
        "/api/accommodations", HttpMethod.GET.name()
    );

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return new ProviderManager(List.of(jwtProvider)); // JwtProvider를 AuthenticationProvider로
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        AuthenticationConfiguration configuration) throws Exception {
        http
            .httpBasic(HttpBasicConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                .configurationSource(corsConfigurationSource()));

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/auth/signup", "/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/accommodations/**").permitAll()
                .anyRequest().authenticated());

        http
            .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .frameOptions(FrameOptionsConfig::sameOrigin));

        http
            .logout((logout) -> logout
                .logoutUrl("/auth/logout")
                .addLogoutHandler(new JwtLogoutHandler())
                .logoutSuccessHandler(new JwtLogoutSuccessHandler()));

        http.addFilterBefore(
            new JwtAuthenticationFilter(authenticationManager(configuration)
                , successHandler,failureHandler, excludeUrls)
            , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
