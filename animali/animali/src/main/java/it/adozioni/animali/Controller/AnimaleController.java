package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AnimaleDto;
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

import java.util.List;

@RestController
@RequestMapping("/api/animali")
public class AnimaleController extends AbstractController<AnimaleDto> {

    @Autowired
    private AnimaleService animaleService;

    @Autowired
    private AdottanteService adottanteService;

    @Autowired
    private DocumentoService documentoService;

    // 1. Lista di tutti gli animali (Restituisce DTO)
    @GetMapping("/lista")
    @PreAuthorize("isAuthenticated()")
    public List<AnimaleDto> getAll() {
        return animaleService.findAll();
    }

    // 2. Cerca per nome (Restituisce DTO)
    @GetMapping("/cerca/{nome}")
    @PreAuthorize("isAuthenticated()")
    public List<AnimaleDto> findByNome(@PathVariable String nome) {
        return animaleService.findByNome(nome);
    }

    // 3. IL CUORE DEL PROGETTO: Generazione del Contratto PDF
    @PostMapping("/genera-contratto")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<byte[]> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        // 1. Recupero le Entity reali dal DB.
        // Grazie alla modifica nel Service, se l'ID non esiste, qui avremo un valore null.
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        // 2. Controllo di sicurezza: ora se l'ID è sbagliato (es. mandi 1 invece di 9),
        // riceverai un errore 404 su Postman invece di un PDF con "null".
        if (animale == null || adottante == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 3. Generazione fisica del PDF tramite byte array
        byte[] pdf = documentoService.creaPdf(animale, adottante);

        // 4. Configurazione degli Header per il download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // Costruisco il nome del file in modo dinamico
        String nomeAnimale = (animale.getNome() != null) ? animale.getNome() : "animale";
        headers.setContentDispositionFormData("attachment", "contratto_adozione_" + nomeAnimale + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}