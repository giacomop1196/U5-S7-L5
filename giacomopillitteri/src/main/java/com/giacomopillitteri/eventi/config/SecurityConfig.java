package com.giacomopillitteri.eventi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // hashare le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                 http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Tutti possono registrarsi e vedere la lista di tutti gli eventi
                        .requestMatchers("/api/auth/**", "/api/eventi").permitAll()

                        // Creazione, Modifica, Eliminazione - Organizzatori
                        .requestMatchers(HttpMethod.POST, "/api/eventi/**").hasAuthority("ORGANIZZATORE_EVENTI")
                        .requestMatchers(HttpMethod.PUT, "/api/eventi/**").hasAuthority("ORGANIZZATORE_EVENTI")
                        .requestMatchers(HttpMethod.DELETE, "/api/eventi/**").hasAuthority("ORGANIZZATORE_EVENTI")

                        // Prenotazione e qualsiasi altra GET che non sia /api/eventi
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}