package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Service.CentroAdozioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centri")
public class CentroAdozioneController {

    @Autowired
    private CentroAdozioneService centroService;

    // Ottiene tutti i centri
    @GetMapping("/lista")
    public List<CentroAdozioneDto> getAllCentri() {
        return (List<CentroAdozioneDto>) centroService.listaTuttiICentri();
    }

    // Cerca per città: /api/centri/citta/Roma
    @GetMapping("/citta/{citta}")
    public List<CentroAdozioneDto> findByCitta(@PathVariable String citta) {
        return centroService.findByCitta(citta);
    }

    // Cerca per NoProfit: /api/centri/noprofit/true
    @GetMapping("/noprofit/{noProfit}")
    public List<CentroAdozioneDto> findByIsNoProfit(@PathVariable boolean noProfit) {
        return centroService.findByIsNoProfit(noProfit);
    }

    // Cerca per nome: /api/centri/nome/RifugioSperanza
    @GetMapping("/nome/{nome}")
    public CentroAdozioneDto findByNomeCentro(@PathVariable String nome) {
        return centroService.findByNomeCentro(nome);
    }
}