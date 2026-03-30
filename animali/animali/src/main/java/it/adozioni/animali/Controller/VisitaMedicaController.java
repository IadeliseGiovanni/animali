package it.adozioni.animali.Controller;

import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Service.VisitaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("VisitaMedica")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitaMedicaController extends AbstractController<VisitaMedicaDto> {

    @Autowired
    private VisitaMedicaService service;

    @GetMapping("/findByData")
    public List<VisitaMedicaDto> findByData(@RequestParam("data") LocalDateTime data) {
        return service.findByData(data);
    }

    @GetMapping("/findByEsito")
    public List<VisitaMedicaDto> findByEsito(@RequestParam("esito") String esito) {
        return service.findByEsito(esito);
    }

    @GetMapping("/findByVeterinario")
    public List<VisitaMedicaDto> findByVeterinario(@RequestParam("veterinario") String veterinario) {
        return service.findByVeterinario(veterinario);
    }

    @GetMapping("/findByDataAndVeterinario")
    public List<VisitaMedicaDto> findByDataAndVeterinario(@RequestParam("data") LocalDateTime data, @RequestParam("veterinario") String veterinario) {
        return service.findByDataAndVeterinario(data, veterinario);
    }

    @GetMapping("/findByDataAndEsito")
    public List<VisitaMedicaDto> findByDataAndEsito(@RequestParam("data") LocalDateTime data, @RequestParam("esito") String esito) {
        return service.findByDataAndEsito(data, esito);
    }

}
