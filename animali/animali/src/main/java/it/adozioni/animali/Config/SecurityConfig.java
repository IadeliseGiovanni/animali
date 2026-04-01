package it.adozioni.animali.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Costruttore per iniettare il filtro JWT
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disabilita CSRF (necessario per API REST che usano POST/PUT)
                .csrf(csrf -> csrf.disable())

                // 2. Gestione sessione STATELESS: non salviamo dati su server, usiamo solo Token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Permessi delle rotte
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()    // Login e Register sempre aperti
                        .requestMatchers("/api/animali/**").permitAll() // Lista animali aperta (per test)
                        .anyRequest().authenticated()                   // Tutto il resto protetto
                )

                // 4. Inseriamo il nostro filtro JWT prima di quello standard
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 5. Abilitiamo Basic Auth (per test rapidi su Postman con user/pass)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * NOTA: Il PasswordEncoder è stato spostato in PasswordConfig.java
     * per evitare riferimenti circolari.
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}