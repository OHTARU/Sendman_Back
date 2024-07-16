package sendman.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import sendman.backend.filter.JwtAuthenticationFilter;

import java.util.Collections;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] ALLOWED_URIS = {"/login/**","/auth/success","/error"};
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors ->{
                    //CORS 설정
                    cors.configurationSource( request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:5000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    });
                })
                //CSRF 설정 off
                .csrf(AbstractHttpConfigurer::disable)
                //httpBasic off
                .httpBasic(AbstractHttpConfigurer::disable)
                //기본 로그인 폼 off
                .formLogin(AbstractHttpConfigurer::disable)
                //기본 로그아웃 off
                .logout(AbstractHttpConfigurer::disable)
                //X-Frame-Options off
                .headers(c-> c.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .authorizeHttpRequests((requests)-> requests
                        //파라미터 권한 없음 설정
                        .requestMatchers(ALLOWED_URIS).permitAll()
                        //설정 외 모든 파라미터 권한 설정
                        .anyRequest().authenticated())

                //세션 매니저 off
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //tokenFilter - 서버 입장 전 검증하는 클래스
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    //Security 파라미터 권한 무시 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/swagger-ui/**",
                "/swagger-config",
                "/swagger.yaml",
                "/swagger-*.yaml",
                "/error",
                "/favicon.ico"
        );
    }
}
