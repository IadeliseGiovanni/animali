package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Service.AdottanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Manteniamo entrambi gli alias per sicurezza con i test e il frontend
@RequestMapping({"/api/Adottante", "/Adottante"})
@CrossOrigin(origins = "http://localhost:4200")
public class AdottanteController extends AbstractController<AdottanteDto> {

    @Autowired
    private AdottanteService adottanteService;

    @Override
    protected AdottanteService getService() {
        return adottanteService;
    }

    /**
     * Endpoint per il profilo dell'utente loggato.
     * Permette sia all'ADOTTANTE che all'ADMIN di vedere i propri dati.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADOTTANTE', 'ADMIN')")
    public ResponseEntity<AdottanteDto> getMe(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(403).build();
        }
        String email = authentication.getName();
        return ResponseEntity.ok(adottanteService.getMioProfilo(email));
    }

    /**
     * Endpoint richiesto dai test per la lettura tramite ID
     */
    @GetMapping("/read")
    @Override
    @PreAuthorize("hasAnyRole('ADOTTANTE', 'ADMIN')")
    public AdottanteDto read(@RequestParam("id") Integer id) {
        return adottanteService.read(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdottanteDto>> getAll() {
        return ResponseEntity.ok(adottanteService.findAllAdottantiTrue());
    }

    @PatchMapping("/{id}/idoneita")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cambiaIdoneita(@PathVariable int id, @RequestParam boolean stato) {
        adottanteService.aggiornaIdoneita(id, stato);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/ruolo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cambiaRuolo(
            @PathVariable int id,
            @RequestParam("nuovoRuolo") String ruolo // Specifica il nome qui
    ) {
        adottanteService.aggiornaRuolo(id, ruolo);
        return ResponseEntity.ok().build();
    }
}