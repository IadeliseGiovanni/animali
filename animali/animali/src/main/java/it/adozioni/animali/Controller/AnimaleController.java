package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdozioneRequestDto;
import it.adozioni.animali.Dto.ResultDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animali")
@CrossOrigin("*")
public class AnimaleController {

    @Autowired private AnimaleService animaleService;
    @Autowired private AdottanteService adottanteService;
    @Autowired private DocumentoService documentoService;
    @Autowired private EmailService emailService;

    @PostMapping("/genera-contratto")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        // Verifica esistenza dati
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        if (animale == null || adottante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultDto.error("Dati non trovati."));
        }

        try {
            byte[] pdf = documentoService.creaPdf(animale, adottante);

            // Invio Email
            emailService.inviaContrattoConAllegato(adottante.getEmail(), animale.getNome(), pdf);
            emailService.inviaNotificaRicezioneAlCentro(adottante.getEmail(), animale.getNome());

            // Risposta PDF per Postman
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Contratto_" + animale.getNome() + ".pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResultDto.error("Errore: " + e.getMessage()));
        }
    }
}