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

    @GetMapping("/lista")
    @PreAuthorize("isAuthenticated()")
    public List<AnimaleDto> getAll() {
        return animaleService.findAll();
    }

    @GetMapping("/cerca/{nome}")
    @PreAuthorize("isAuthenticated()")
    public List<AnimaleDto> findByNome(@PathVariable String nome) {
        return animaleService.findByNome(nome);
    }

    @PostMapping("/genera-contratto")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<byte[]> generaContratto(@RequestBody AdozioneRequestDto adozioneDto) {

        // Ora findByIdEntity non è più rosso perché l'abbiamo aggiunto ai Service
        Animale animale = animaleService.findByIdEntity(adozioneDto.getIdAnimale());
        Adottante adottante = adottanteService.findByIdEntity(adozioneDto.getIdAdottante());

        if (animale == null || adottante == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] pdf = documentoService.creaPdf(animale, adottante);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String nomeAnimale = (animale.getNome() != null) ? animale.getNome() : "animale";
        headers.setContentDispositionFormData("attachment", "contratto_adozione_" + nomeAnimale + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}