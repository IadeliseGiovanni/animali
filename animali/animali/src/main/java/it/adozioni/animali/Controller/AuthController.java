package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.LoginRequest;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdottanteService adottanteService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AdottanteDto> register(@RequestBody AdottanteDto dto) {
        System.out.println("DEBUG: Ricevuta richiesta di registrazione per " + dto.getEmail());
        return ResponseEntity.ok(adottanteService.registra(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("--- DEBUG LOGIN START ---");
            System.out.println("Email ricevuta: " + request.getEmail());
            System.out.println("Password ricevuta: " + request.getPassword());

            // Tentativo di autenticazione
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            System.out.println("DEBUG: Autenticazione riuscita per " + request.getEmail());

            UserDetails user = adottanteService.loadUserByUsername(request.getEmail());
            String token = jwtService.generateToken(user);

            System.out.println("DEBUG: Token generato con successo!");
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException e) {
            System.err.println("DEBUG ERROR: Password sbagliata o utente non trovato!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: Email o Password errati.");

        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Errore generico durante il login: " + e.getMessage());
            e.printStackTrace(); // Questo stampa tutto l'errore rosso in console
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Errore login: " + e.getMessage());
        }
    }
}