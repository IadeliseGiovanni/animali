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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AdottanteService adottanteService;
    @Autowired
    private VolontarioService volontarioService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Verifica credenziali (usa BCrypt internamente)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails user = adottanteService.loadUserByUsername(request.getEmail());
            String token = jwtService.generateToken(user);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide.");
        }
    }

    @PostMapping("/register/adottante")
    public ResponseEntity<?> register(@RequestBody AdottanteDto dto) {
        try {
            return new ResponseEntity<>(adottanteService.registra(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}