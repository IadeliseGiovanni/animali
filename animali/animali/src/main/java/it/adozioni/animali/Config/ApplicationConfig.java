package it.adozioni.animali.Config;

import it.adozioni.animali.Repository.AdottanteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final AdottanteRepository repository;

    public ApplicationConfig(AdottanteRepository repository) {
        this.repository = repository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // La Lambda corretta non vuole il tipo "String" esplicito prima del nome variabile
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 1. Costruttore vuoto (risolve l'errore "cannot be applied to given types")
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // 2. Metodo corretto: setUserDetailsService (senza la 's' di troppo)
        authProvider.setUserDetailsService(userDetailsService());

        // 3. Configura l'encoder per BCrypt
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}