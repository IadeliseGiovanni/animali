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

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> register(@RequestBody AdottanteDto dto) {
        try {
            System.out.println("DEBUG: Tentativo di registrazione per " + dto.getEmail());
            AdottanteDto nuovoUtente = adottanteService.registra(dto);
            return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Errore registrazione: " + e.getMessage());
            return ResponseEntity.badRequest().body("Errore: Email già esistente o dati non validi.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("--- DEBUG LOGIN START ---");
            System.out.println("Email ricevuta: " + request.getEmail());

            // 1. L'AuthenticationManager usa il BCryptPasswordEncoder (da ApplicationConfig)
            // per confrontare la password di Postman con l'hash nel DB.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            System.out.println("DEBUG: Autenticazione riuscita per " + request.getEmail());

            // 2. Carichiamo l'utente e generiamo il Token
            UserDetails user = adottanteService.loadUserByUsername(request.getEmail());
            String token = jwtService.generateToken(user);

            // Restituiamo un JSON pulito con il token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            System.out.println("DEBUG: Token inviato a Postman!");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            System.err.println("DEBUG ERROR: Password sbagliata o hash non corrispondente!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: Credenziali non valide.");
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Errore imprevisto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore server.");
        }
    }
}