package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Service.AdottanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/Adottante") // Aggiunto lo slash iniziale
@CrossOrigin(origins = "http://localhost:4200")
public class AdottanteController extends AbstractController<AdottanteDto> {

    @Autowired
    private AdottanteService service;

    /**
     * Ricerca per cognome.
     * URL su Postman: GET http://localhost:8080/Adottante/findByCognome?cognome=Rossi
     */
    @GetMapping("/findByCognome")
    public List<AdottanteDto> findByCognome(@RequestParam("cognome") String cognome) {
        return service.findByCognome(cognome);
    }
//
    @GetMapping("/all") // Oppure /lista, basta che coincida con Angular
    public List<AdottanteDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<AdottanteDto> getMioProfilo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // principal.getName() restituisce l'email dell'utente dal JWT
        AdottanteDto dto = service.getMioProfilo(principal.getName());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/idoneita")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateIdoneita(@PathVariable int id, @RequestParam boolean stato) {
        service.aggiornaIdoneita(id, stato);
        return ResponseEntity.ok().build();
    }

    // Endpoint per aggiornare il ruolo (USER/ADMIN)
    @PatchMapping("/{id}/ruolo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRuolo(@PathVariable int id, @RequestParam String nuovoRuolo) {
        service.aggiornaRuolo(id, nuovoRuolo);
        return ResponseEntity.ok().build();
    }
}