package com.test_app_movil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("user") // Nombre de usuario
                .password(passwordEncoder().encode("password")) // Contraseña
                .roles("USER") // Rol del usuario
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Deshabilitar CSRF para pruebas con Postman
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll() // Permitir login y registro
            .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Permitir Swagger
            .antMatchers("/api/usuarios/registro").permitAll() // Permitir registro
            .anyRequest().authenticated() // Proteger todos los demás endpoints
            .and()
            .httpBasic(); // Autenticación básica

        return http.build();
    }
}
