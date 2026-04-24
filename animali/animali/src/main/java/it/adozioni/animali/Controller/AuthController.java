package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.LoginRequest;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.PasswordResetToken;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.EmailService;
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
import java.util.UUID;

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

    @Autowired
    private EmailService emailService;

    /**
     * Registrazione Adottante.
     * Risolve l'errore su "registra" richiamando il metodo pubblico del Service.
     */
    @PostMapping("/register/adottante")
    public ResponseEntity<?> register(@RequestBody AdottanteDto dto) {
        try {
            System.out.println("DEBUG: Tentativo di registrazione per " + dto.getEmail());
            AdottanteDto nuovoUtente = adottanteService.registra(dto);
            return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Errore registrazione: " + e.getMessage());
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }

    /**
     * Registrazione Volontario.
     */
    @PostMapping("/register/volontario")
    public ResponseEntity<?> registerVolontario(@RequestBody VolontarioDto dto) {
        try {
            VolontarioDto nuovoVolontario = volontarioService.registra(dto);
            return new ResponseEntity<>(nuovoVolontario, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore registrazione volontario: " + e.getMessage());
        }
    }

    /**
     * Login centralizzato con gestione dei ruoli dinamica (RBAC).
     */
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

            // Riconoscimento del tipo di utente nel payload di risposta
            if (principal instanceof Adottante) {
                Adottante a = (Adottante) principal;
                response.put("nome", a.getNome());
                response.put("ruolo", a.getRuolo()); // USER o ADMIN
            } else if (principal instanceof Volontario) {
                Volontario v = (Volontario) principal;
                response.put("nome", v.getNome());
                response.put("ruolo", v.getRuolo());
            }

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Errore: Conferma la tua email per attivare l'account.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Errore: Email o password errati.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore server: " + e.getMessage());
        }
    }

    /**
     * Endpoint per la verifica del token via mail.
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
        boolean verified = adottanteService.verifyToken(token);
        if (verified) {
            return ResponseEntity.ok("Account verificato con successo!");
        }
        return ResponseEntity.badRequest().body("Token non valido o scaduto.");
    }

    /**
     * Reinvio mail di attivazione.
     * Risolve l'errore "cannot find symbol" richiamando findByEmailEntity.
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        try {
            // Cerca l'entità Adottante direttamente sul DB
            Adottante adottante = adottanteService.findByEmailEntity(email);

            if (adottante == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: Utente non trovato.");
            }

            if (adottante.isEnabled()) {
                return ResponseEntity.badRequest().body("Errore: L'account è già attivo.");
            }

            // Invia nuovamente la mail tramite EmailService
            adottanteService.inviaEmailVerifica(adottante.getEmail(), adottante.getVerificationToken());

            return ResponseEntity.ok("Email di verifica reinviata con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il reinvio: " + e.getMessage());
        }
    }

    // AuthController.java

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            // 1. Cerchiamo l'utente (Adottante o Volontario)
            Adottante adottante = adottanteService.findByEmailEntity(email);
            if (adottante == null) {
                return ResponseEntity.ok("Se l'email è registrata, riceverai un link a breve.");
            }

            // 2. Generiamo un token univoco (UUID)
            String token = UUID.randomUUID().toString();

            // 3. Salviamo il token nel DB associato all'utente
            // (Assicurati di avere un campo 'resetToken' nel model Adottante)
            adottanteService.salvaResetToken(adottante.getId(), token);

            // 4. Inviamo l'email tramite il tuo EmailService
            emailService.sendResetEmail(email, token);

            return ResponseEntity.ok("Email di reset inviata.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password-confirm")
    public ResponseEntity<?> resetPasswordConfirm(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        boolean success = adottanteService.aggiornaPasswordConToken(token, newPassword);

        if (success) {
            // Restituisci una mappa, che Spring trasformerà in JSON { "message": "..." }
            return ResponseEntity.ok(Map.of("message", "Password aggiornata con successo!"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Token non valido o scaduto."));
    }
}