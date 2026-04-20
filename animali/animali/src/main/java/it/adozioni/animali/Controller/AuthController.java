package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.LoginRequest;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.JwtService;
import it.adozioni.animali.Service.VolontarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AdottanteService adottanteService;

    @Autowired
    private VolontarioService volontarioService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register/adottante")
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

    @PostMapping("/register/volontario")
    public ResponseEntity<?> registerVolontario(@RequestBody VolontarioDto dto) {
        try {
            System.out.println("DEBUG: Registrazione Volontario per " + dto.getEmail());
            // Usa il metodo registra che abbiamo appena sistemato nel VolontarioService
            VolontarioDto nuovoVolontario = volontarioService.registra(dto);
            return new ResponseEntity<>(nuovoVolontario, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore registrazione volontario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Object principal = authentication.getPrincipal();
            String token = jwtService.generateToken((UserDetails) principal);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            // Controllo dinamico del tipo di utente
            if (principal instanceof Adottante) {
                Adottante a = (Adottante) principal;
                response.put("nome", a.getNome());
                response.put("ruolo", a.getRuolo()); // es. "USER"
            } else if (principal instanceof Volontario) {
                Volontario v = (Volontario) principal;
                response.put("nome", v.getNome());
                response.put("ruolo", "VOLONTARIO");
            }

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            // Questo scatta se isEnabled() nel tuo modello Adottante ritorna false
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Errore: Conferma la tua email per attivare l'account.");
        } catch (LockedException e) {
            // Questo scatta se isAccountNonLocked() ritorna false (utente schedato)
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body("Errore: Il tuo account è stato sospeso.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Errore: Email o password errati.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore server.");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
        boolean verified = adottanteService.verifyToken(token); // Logica per cercare il token e settare enabled = true
        if (verified) {
            return ResponseEntity.ok("Account verificato con successo!");
        }
        return ResponseEntity.badRequest().body("Token non valido o scaduto.");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        try {
            // Cerchiamo l'adottante tramite email
            Adottante adottante = adottanteService.findByEmailEntity(email);

            if (adottante == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Errore: Utente non trovato.");
            }

            if (adottante.isEnabled()) {
                return ResponseEntity.badRequest()
                        .body("Errore: L'account è già attivo.");
            }

            // Recuperiamo il token esistente (o puoi rigenerarlo se preferisci)
            String token = adottante.getVerificationToken();

            // Usiamo il tuo emailService per spedire
            adottanteService.inviaEmailVerifica(adottante.getEmail(), token);

            return ResponseEntity.ok("Email di verifica reinviata con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il reinvio: " + e.getMessage());
        }
    }
}