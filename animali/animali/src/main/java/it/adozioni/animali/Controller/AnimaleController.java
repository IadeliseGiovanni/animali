package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdozioneRequestDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.AnimaleService;
import it.adozioni.animali.Service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animali")
public class AnimaleController {

    @Autowired
    private AnimaleService animaleService;

    @Autowired
    private AdottanteService adottanteService;

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/genera-contratto")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public ResponseEntity<?> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        // LOG DI CONTROLLO: Vedrai questi dati nella console di IntelliJ
        System.out.println("--- INIZIO GENERAZIONE CONTRATTO ---");
        System.out.println("Ricerca Animale ID: " + adozioneDto.getIdAnimale());
        System.out.println("Ricerca Adottante ID: " + adozioneDto.getIdAdottante());

        // Recupero delle entità tramite i Service
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        // VERIFICA PUNTUALE: Ti dice esattamente cosa manca
        if (animale == null) {
            System.err.println("ERRORE: Animale con ID " + adozioneDto.getIdAnimale() + " non trovato nel database!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: Animale non trovato.");
        }

        if (adottante == null) {
            System.err.println("ERRORE: Adottante con ID " + adozioneDto.getIdAdottante() + " non trovato nel database!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: Adottante non trovato.");
        }

        try {
            // Se arriviamo qui, entrambi esistono!
            System.out.println("SUCCESSO: Entità trovate. Avvio creazione PDF per " + animale.getNome());

            byte[] pdf = documentoService.creaPdf(animale, adottante);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Pulisce il nome dell'animale per il nome del file
            String nomeFile = "contratto_" + (animale.getNome() != null ? animale.getNome() : "adozione") + ".pdf";
            headers.setContentDispositionFormData("attachment", nomeFile);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("ERRORE CRITICO GENERAZIONE PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno nel generare il file.");
        }
    }
}