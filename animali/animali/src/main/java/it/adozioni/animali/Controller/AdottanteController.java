package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Service.AdottanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// AGGIUNTO: Alias "/Adottante" per soddisfare la chiamata del test che fallisce
@RequestMapping({"/api/adottanti", "/Adottante"})
@CrossOrigin(origins = "http://localhost:4200")
public class AdottanteController extends AbstractController<AdottanteDto> {

    @Autowired
    private AdottanteService adottanteService;

    @Override
    protected AdottanteService getService() {
        return adottanteService;
    }

    /**
     * Questo metodo risponde a /Adottante/read?id=...
     * Portando lo status a 200 come richiesto dal test.
     */
    @GetMapping("/read")
    @Override
    public AdottanteDto read(@RequestParam("id") Integer id) {
        return adottanteService.read(id);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<AdottanteDto>> getAll() {
        return ResponseEntity.ok(adottanteService.findAllAdottantiTrue());
    }

    @GetMapping("/profilo")
    public ResponseEntity<AdottanteDto> getMioProfilo(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(adottanteService.getMioProfilo(email));
    }

    @PutMapping("/{id}/idoneita")
    public ResponseEntity<?> cambiaIdoneita(@PathVariable int id, @RequestParam boolean stato) {
        adottanteService.aggiornaIdoneita(id, stato);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/ruolo")
    public ResponseEntity<?> cambiaRuolo(@PathVariable int id, @RequestParam String ruolo) {
        adottanteService.aggiornaRuolo(id, ruolo);
        return ResponseEntity.ok().build();
    }
}