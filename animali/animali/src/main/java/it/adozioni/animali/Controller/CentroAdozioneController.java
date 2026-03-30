package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Service.CentroAdozioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/centri") // L'URL base per questo controller
public class CentroAdozioneController {

    @Autowired
    private CentroAdozioneService centroService;

    // Endpoint per ottenere la lista di tutti i centri (restituisce i DTO)
    @GetMapping("/lista")
    public List<CentroAdozioneDto> getAllCentri() {
        return centroService.listaTuttiICentri();
    }
}