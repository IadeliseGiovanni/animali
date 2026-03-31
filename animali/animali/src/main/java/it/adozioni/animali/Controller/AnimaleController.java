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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animali")
public class AnimaleController {

    @Autowired
    private AnimaleService animaleService;

    @Autowired
    private AdottanteService adottanteService;

    @Autowired
    private DocumentoService documentoService;

    // 1. Lista di tutti gli animali (Restituisce DTO)
    @GetMapping("/lista")
    public List<AnimaleDto> getAll() {
        return animaleService.findAll();
    }

    // 2. Cerca per nome (Restituisce DTO)
    @GetMapping("/cerca/{nome}")
    public List<AnimaleDto> findByNome(@PathVariable String nome) {
        return animaleService.findByNome(nome);
    }

    // 3. IL CUORE DEL PROGETTO: Generazione del Contratto PDF
    @PostMapping("/genera-contratto")
    public ResponseEntity<byte[]> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        // 1. Uso i dati dentro il DTO per cercare le Entity nel DB
        // Nota: Assicurati di avere questi metodi 'findByIdEntity' nei tuoi Service
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        // Controllo di sicurezza: se uno dei due non esiste, ritorno 404
        if (animale == null || adottante == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 2. Chiamo il service che "disegna" il PDF (il byte[] è il file fisico)
        byte[] pdf = documentoService.creaPdf(animale, adottante);

        // 3. Rispondo inviando il file PDF con gli Header corretti
        HttpHeaders headers = new HttpHeaders();
        // Dico al client che il contenuto è un PDF
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Dico al browser di scaricarlo come allegato chiamato 'contratto.pdf'
        headers.setContentDispositionFormData("attachment", "contratto_adozione_" + animale.getNome() + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}