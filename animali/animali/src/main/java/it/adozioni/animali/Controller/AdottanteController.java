package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Service.AdottanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Adottante") // Aggiunto lo slash iniziale
@CrossOrigin(origins = "http://localhost:4200")
public class AdottanteController extends AbstractController<AdottanteDto> {

    @Autowired
    private AdottanteService service;

    /**
     * Ricerca per cognome.
     * URL su Postman: GET http://localhost:8080/Adottante/findByCognome?cognome=Rossi
     */
    @GetMapping("/findByCognome")
    public List<AdottanteDto> findByCognome(@RequestParam("cognome") String cognome) {
        return service.findByCognome(cognome);
    }
}