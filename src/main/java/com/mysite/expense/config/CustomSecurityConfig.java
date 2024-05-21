package com.mysite.expense.config;

import com.mysite.expense.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 를 종료
                .authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers("/js/**", "/css/**").permitAll()
                                .requestMatchers("/", "/login", "/register").permitAll()
                                .anyRequest().authenticated()
                ) // 리퀘스트매처의 주소들은 허가하고 그 외의 주소는 인증을 요구함
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/")
                        .defaultSuccessUrl("/expenses")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // 인증시 로그인 페이지를 지정하고 실패시 주소 지정 성공주소 지정 (username => email, password)
                )
        ;

        return http.build();
    }

    // 시큐리티 인증 담당 객체
    @Bean
    public AuthenticationManager AuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 정적 파일을 예외로 처리하기 (시큐리티의 예외)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/error");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
