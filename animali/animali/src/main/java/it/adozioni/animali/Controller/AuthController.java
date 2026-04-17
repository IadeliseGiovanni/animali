package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.LoginRequest;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.JwtService;
import it.adozioni.animali.Service.VolontarioService;
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
            System.out.println("--- DEBUG LOGIN START ---");
            System.out.println("Email ricevuta: " + request.getEmail());

            // 1. Carichiamo l'utente PRIMA dell'autenticazione per controllare lo stato della verifica
            UserDetails user = adottanteService.loadUserByUsername(request.getEmail());

            // --- CONTROLLO VERIFICA EMAIL ---
            // Se l'utente esiste ma non ha cliccato sul link, blocchiamo l'accesso
            if (!user.isEnabled()) {
                System.err.println("DEBUG ERROR: Utente non abilitato (email non verificata): " + request.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Errore: Devi confermare la tua email prima di accedere a PetFlow.");
            }
//
            // 2. L'AuthenticationManager controlla la password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            System.out.println("DEBUG: Autenticazione riuscita per " + request.getEmail());

            // 3. Generiamo il Token
            String token = jwtService.generateToken(user);

            // Restituiamo un JSON pulito con il token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            System.out.println("DEBUG: Token inviato a Postman!");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            System.err.println("DEBUG ERROR: Password sbagliata!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore: Credenziali non valide.");
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Errore imprevisto: " + e.getMessage());
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
}