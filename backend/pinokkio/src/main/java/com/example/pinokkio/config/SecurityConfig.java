package com.example.pinokkio.config;

import com.example.pinokkio.config.jwt.JwtAuthenticationFilter;
import com.example.pinokkio.config.jwt.LogoutService;
import com.example.pinokkio.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final LogoutService logoutService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private static final String[] SWAGGER_URL = {
    };

    private final String[] GET_PERMIT_API_URL = {
            "/",
            "/refresh",
    };

    private final String[] POST_PERMIT_API_URL = {
            "/register/pos",
            "/register/teller",
            "/login/**",
            "/refresh",
            "/users/auth/token/"
    };

    private final String[] POS_API_URL = {
            "/pos/**"
    };

    private final String[] TELLER_API_URL = {
            "/tellers/**"
    };

    private final String[] KIOSK_API_URL = {
            "/kiosk/**"
    };


    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(c -> c.disable())
                .formLogin(c -> c.disable())
                .httpBasic(c -> c.disable())
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeRequests(c -> c.requestMatchers(SWAGGER_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PERMIT_API_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, POST_PERMIT_API_URL).permitAll()
                        .requestMatchers(POS_API_URL).hasRole("POS")
                        .requestMatchers(TELLER_API_URL).hasRole("TELLER")
                        .requestMatchers(KIOSK_API_URL).hasRole("KIOSK")
                        .anyRequest().authenticated()
                )

                .logout(c -> c.logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))

                .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of("*"));

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}