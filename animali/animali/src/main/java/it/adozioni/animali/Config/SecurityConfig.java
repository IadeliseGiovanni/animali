package it.adozioni.animali.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Abilita l'uso di @PreAuthorize sui controller
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilitato per semplificare i test (es. Postman)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Login e registrazione sempre aperti
                        .requestMatchers("/h2-console/**").permitAll() // Se usi H2 per il database
                        .anyRequest().authenticated() // Tutto il resto richiede il login
                )
                .httpBasic(Customizer.withDefaults()) // Permette il login tramite Basic Auth
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Necessario per H2

        return http.build();
    }

    // Fondamentale: definisce come criptare/decriptare le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Serve se vuoi creare un controller di login personalizzato
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}