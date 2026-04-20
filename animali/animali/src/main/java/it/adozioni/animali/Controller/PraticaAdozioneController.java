package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.PraticaAdozioneDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.StatoPratica;
import it.adozioni.animali.Repository.AdottanteRepository;
import it.adozioni.animali.Service.PraticaAdozioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pratiche")
@CrossOrigin(origins = "*") // Opzionale: per gestire CORS se necessario
public class PraticaAdozioneController {

    @Autowired
    private PraticaAdozioneService praticaService;

    @Autowired
    private AdottanteRepository adottanteRepository;

    /**
     * Avvia una nuova pratica di adozione.
     * Il Principal viene iniettato automaticamente da Spring Security tramite il token JWT.
     */
    @PostMapping("/avvia/{animaleId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> avviaPratica(@PathVariable Integer animaleId, Principal principal) {
        try {
            // 1. Recupero l'utente loggato partendo dall'email nel token
            Adottante utenteLoggato = adottanteRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Utente non trovato nel sistema."));

            // 2. Chiamo il service per la logica di business
            PraticaAdozioneDto nuovaPratica = praticaService.avviaPratica(utenteLoggato, animaleId);

            // 3. Restituisco la pratica creata con stato 201 Created
            return new ResponseEntity<>(nuovaPratica, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // Se il service lancia un errore (es. non idoneo), mandiamo un 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Errore generico del server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore imprevisto durante l'avvio della pratica.");
        }
    }

    /**
     * Endpoint per l'Admin: recupera tutte le pratiche nel sistema.
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PraticaAdozioneDto>> getAllPratiche() {
        return ResponseEntity.ok(praticaService.getAllPratiche()); // Assicurati di avere questo metodo nel service
    }

    /**
     * Endpoint per l'utente: recupera solo le proprie pratiche.
     */
    @GetMapping("/mie-pratiche")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PraticaAdozioneDto>> getMiePratiche(Principal principal) {
        Adottante utente = adottanteRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return ResponseEntity.ok(praticaService.getPraticheByUtente(utente.getId()));
    }

    /**
     * Endpoint per l'Admin: Approva o Rifiuta una pratica.
     */
    /**
     * Endpoint per l'Admin per approvare o rifiutare una pratica.
     * Esempio: PATCH /api/pratiche/admin/10/stato?nuovoStato=APPROVATA
     */
    @PatchMapping("/admin/{id}/stato")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cambiaStatoPratica(
            @PathVariable Integer id,
            @RequestParam StatoPratica nuovoStato,
            @RequestBody(required = false) String note) {
        try {
            PraticaAdozioneDto aggiornata = praticaService.aggiornaStatoPratica(id, nuovoStato, note);
            return ResponseEntity.ok(aggiornata);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento della pratica.");
        }
    }


}