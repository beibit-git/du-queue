package kz.dulaty.queue.core.configs.security;

import jakarta.annotation.PostConstruct;
import kz.dulaty.queue.feature.auth.config.BearerAuthEntryPoint;
import kz.dulaty.queue.feature.auth.config.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final BearerAuthEntryPoint authEntryPoint;
    public static final String ALLOW_ALL_MATCHER = "/**";
    private final String[] INSECURE_ENDPOINTS = new String[]{
            "/api/v1/auth/sign-in",
            "/api/v1/auth/sign-up",
            "/api/v1/auth/sign-out",
            "/api/v1/auth/confirm-email",
            "/api/v1/auth/forgot-password/**",
            "/api/v1/auth/reset-password/**",
            "/swagger/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v1/queue/ticket/generate",
            "/api/v1/queue/department",
            "/api/v1/queue/department/*"
    };

    @PostConstruct
    void postConstruct() {
        log.info("Insecure endpoints {}", Arrays.toString(INSECURE_ENDPOINTS));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Включаем CORS
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                        .maximumSessions(3)
                        .maxSessionsPreventsLogin(true))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(INSECURE_ENDPOINTS).permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(authEntryPoint))
                .authenticationManager(authenticationManager)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSIONID", "JSESSIONID"));
        return http.build();
    }

    // Метод для конфигурации CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Разрешаем отправку куков
        config.addAllowedOriginPattern("*"); // Разрешаем запросы с любых источников
        config.addAllowedMethod("*"); // Разрешаем все методы (GET, POST, PUT, DELETE и др.)
        config.addAllowedHeader("*"); // Разрешаем все заголовки
        config.setMaxAge(3600L); // Указываем время кэширования preflight-запросов (в секундах)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            HttpSecurity http
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}
