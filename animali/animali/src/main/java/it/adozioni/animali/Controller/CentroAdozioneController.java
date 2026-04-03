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

    // VISTA USER: "Voglio vedere gli animali"

    @GetMapping("/user/lista")
    public String visualizzaComeUser() {
        List<CentroAdozioneDto> lista = centroService.listaTuttiICentri();
        return "--- SONO USER --- \n" +
                "Benvenuto nel catalogo! Ecco i centri disponibili: \n" + lista.toString();
    }

    @GetMapping("/user/cerca/{citta}")
    public String cercaComeUser(@PathVariable String citta) {
        List<CentroAdozioneDto> risultati = centroService.findByCitta(citta);
        return "--- SONO USER --- \n" +
                "Ricerca per la città di " + citta + ": \n" + risultati.toString();
    }

    // VISTA ADMIN: "Voglio gestire il database"


    @PostMapping("/admin/nuovo")
    public String creaComeAdmin(@RequestBody CentroAdozioneDto dto) {
        CentroAdozioneDto salvato = centroService.salvaNuovo(dto);
        return "--- SONO ADMIN --- \n" +
                "OPERAZIONE COMPLETATA: Il centro '" + salvato.getNomeCentro() + "' è stato inserito nel sistema.";
    }

    @DeleteMapping("/admin/elimina/{id}")
    public String eliminaComeAdmin(@PathVariable Integer id) {
        centroService.elimina(id);
        return "--- SONO ADMIN --- \n" +
                "ATTENZIONE: Il centro con ID " + id + " è stato rimosso definitivamente.";
    }
}