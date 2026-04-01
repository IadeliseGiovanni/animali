package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdozioneRequestDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.AnimaleService;
import it.adozioni.animali.Service.DocumentoService;
import it.adozioni.animali.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animali")
@CrossOrigin("*")
public class AnimaleController {

    @Autowired
    private AnimaleService animaleService;

    @Autowired
    private AdottanteService adottanteService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/genera-contratto")
    public ResponseEntity<?> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        System.out.println("--- INIZIO PROCESSO CONTRATTUALE ---");

        // 1. Recupero entità dal DB
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        // 2. Validazione esistenza
        if (animale == null || adottante == null) {
            System.err.println("ERRORE: ID non trovati nel database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: Animale o Adottante non esistente.");
        }

        try {
            // 3. Creazione del PDF
            System.out.println("LOG: Generazione PDF per " + animale.getNome() + "...");
            byte[] pdf = documentoService.creaPdf(animale, adottante);

            // 4. INVIO EMAIL (PROTETTO DA TRY-CATCH LOCALE)
            // Questo blocco evita che l'errore di autenticazione email blocchi il PDF
            try {
                if (adottante.getEmail() != null && !adottante.getEmail().isEmpty()) {
                    System.out.println("LOG: Tentativo invio email a " + adottante.getEmail());
                    emailService.inviaContrattoConAllegato(adottante.getEmail(), animale.getNome(), pdf);
                }
            } catch (Exception mailException) {
                // Se la mail fallisce, logghiamo l'errore ma procediamo con la restituzione del PDF
                System.err.println("AVVISO: Invio email fallito (Authentication Error), ma procedo con il download.");
            }

            // 5. Configurazione Header per il download del file
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String fileName = "Contratto_Adozione_" + animale.getNome() + ".pdf";
            headers.setContentDispositionFormData("attachment", fileName);

            System.out.println("--- SUCCESSO: Contratto inviato alla risposta HTTP ---");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("ERRORE CRITICO SISTEMA: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore tecnico nella generazione del contratto.");
        }
    }
}