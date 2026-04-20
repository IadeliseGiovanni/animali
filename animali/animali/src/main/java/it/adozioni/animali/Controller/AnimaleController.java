package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdozioneRequestDto;
import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Dto.ResultDto;
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
public class AnimaleController extends AbstractController<AnimaleDto> {

    @Autowired
    private AnimaleService animaleService;
    @Autowired
    private AdottanteService adottanteService;
    @Autowired
    private DocumentoService documentoService;
    @Autowired
    private EmailService emailService;

    @Override
    protected AdottanteService getService() {
        return adottanteService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnimaleDto>> getAll() {
        return ResponseEntity.ok(animaleService.findAllDisponibili());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnimaleDto>> searchAnimali(
            @RequestParam(required = false) String specie,
            @RequestParam(required = false) String genere,
            @RequestParam(required = false) Long centroId) {
        return ResponseEntity.ok(animaleService.filterAnimali(specie, genere, centroId));
    }

    @GetMapping("/centro/{idCentro}")
    public ResponseEntity<List<AnimaleDto>> getByCentro(@PathVariable Long idCentro) {
        return ResponseEntity.ok(animaleService.getAnimaliByCentro(idCentro));
    }

    @PostMapping("/genera-contratto")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        if (adozioneDto == null || adozioneDto.getIdAnimale() == null || adozioneDto.getIdAdottante() == null) {
            // FIX: Usiamo ResultDto.error invece della stringa
            return ResponseEntity.badRequest().body(ResultDto.error("Errore: Dati incompleti."));
        }

        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        if (animale == null || adottante == null) {
            // FIX: Incapsuliamo il messaggio nel ResultDto
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResultDto.error("Errore: Animale o Adottante non trovati nel database."));
        }

        try {
            byte[] pdf = documentoService.creaPdf(animale, adottante);

            if (pdf == null) throw new RuntimeException("Errore PDF");

            emailService.inviaContrattoConAllegato(adottante.getEmail(), animale.getNome(), pdf);
            emailService.inviaNotificaRicezioneAlCentro(adottante.getEmail(), animale.getNome());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Contratto_" + animale.getNome() + ".pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            // FIX: Il test vuole un'istanza di ResultDto anche qui
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultDto.error("Errore critico durante il processo: Errore PDF"));
        }
    }
}