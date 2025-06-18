package com.example.videorental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Конфигурация Spring Security
@EnableWebSecurity // Включение веб-безопасности
@EnableGlobalMethodSecurity(prePostEnabled = true) // Разрешение глобальной безопасности методов (аннотации @PreAuthorize)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Отключаем CSRF для REST-API
                .authorizeRequests()
                .antMatchers("/api/**").authenticated() // Запросы к /api/** требуют аутентификации
                .and()
                .httpBasic(); // Используется базовая HTTP-авторизация
        return http.build(); // Построенная цепочка фильтров
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // Шифрование паролей с помощью BCrypt
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        // Создаем пользователей в памяти для демонстрации
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN") // Роль ADMIN
                .build();
        UserDetails client = User.withUsername("user")
                .password(passwordEncoder.encode("userpass"))
                .roles("USER") // Роль USER
                .build();
        return new InMemoryUserDetailsManager(admin, client); // Менеджер учетных записей
    }
}