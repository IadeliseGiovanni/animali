package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdozioneRequestDto;
import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Dto.ResultDto; // Import del nuovo Wrapper
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animali")
@CrossOrigin(origins = "http://localhost:4200")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        // --- VALIDAZIONE INPUT (Evita l'errore 400 generico) ---
        if (adozioneDto == null || adozioneDto.getIdAnimale() == null || adozioneDto.getIdAdottante() == null) {
            return ResponseEntity.badRequest()
                    .body(ResultDto.error("Dati della richiesta incompleti o JSON malformato."));
        }

        // 1. Recupero entità dal database
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        if (animale == null || adottante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResultDto.error("Errore: Animale o Adottante non trovati nel database."));
        }
//
        try {
            // 2. Generazione del PDF Professionale (Verde Salvia) tramite DocumentoService
            byte[] pdf = documentoService.creaPdf(animale, adottante);

            // 3. INVIO EMAIL ALL'ADOTTANTE (Mittente: iadelisegiovanni2000@gmail.com)
            emailService.inviaContrattoConAllegato(adottante.getEmail(), animale.getNome(), pdf);

            // 4. INVIO NOTIFICA AL CENTRO (Feedback Loop per l'amministratore)
            emailService.inviaNotificaRicezioneAlCentro(adottante.getEmail(), animale.getNome());

            // 5. RITORNO DEL PDF CON HEADERS (Per visualizzazione immediata su Postman)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String fileName = "Contratto_" + animale.getNome().replace(" ", "_") + ".pdf";
            headers.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultDto.error("Errore critico durante il processo: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        // Cambiato: restituisce solo quelli disponibili per la vetrina
        return ResponseEntity.ok(animaleService.findAllDisponibili());
    }

    @GetMapping("/search")
    public List<AnimaleDto> searchAnimali(
            @RequestParam(required = false) String specie,
            @RequestParam(required = false) String genere,
            @RequestParam(required = false) Long centroId){
        return animaleService.filterAnimali(specie, genere, centroId);
    }

    @GetMapping("/centro/{idCentro}")
    public ResponseEntity<List<AnimaleDto>> getByCentro(@PathVariable Long idCentro) {
        // Chiama il repository per trovare gli animali con quel centro_id
        List<AnimaleDto> animali = animaleService.getAnimaliByCentro(idCentro);
        return ResponseEntity.ok(animali);
    }
}